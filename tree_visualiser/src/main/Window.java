package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import handlers.UITheme;
import objects.Transition;
import objects.Vertex;
import ui.ExportDialog;
import ui.Frame;
import ui.ObjectPanel;
import ui.Ribbon;
import ui.TerminalPanel;
import java.util.ArrayDeque;
import java.util.Deque;

public class Window extends Frame {
	public static Window instance;

	private File lastDirectory = null;
	private File thisFile = null;
	private Workspace workspace;

	private final Deque<WorkspaceState> undoStack = new ArrayDeque<>();
	private final Deque<WorkspaceState> redoStack = new ArrayDeque<>();

	private record WorkspaceState(List<Vertex> vertices, List<Transition> transitions) {
	}

	private static final long serialVersionUID = 1L;
	private JMenuBar mb = new JMenuBar();

	public Window() {
		SwingUtilities.invokeLater(() -> {

			instance = this;
			workspace = new Workspace();
			TerminalPanel terminal = new TerminalPanel();

			UITheme.darkTheme();
			JScrollPane scrollPane = new JScrollPane(workspace);
			scrollPane.getViewport().setBackground(new Color(40, 50, 75)); // optional match
			scrollPane.setBorder(BorderFactory.createEmptyBorder());

			add(new SideBar(), BorderLayout.WEST);

			JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, terminal);
			// splitPane.setResizeWeight(0.8);
			splitPane.setDividerLocation(400);
			splitPane.setDividerSize(8);
			splitPane.setBackground(new Color(30, 30, 40));
			BasicSplitPaneDivider divider = ((BasicSplitPaneUI) splitPane.getUI()).getDivider();
			divider.setBackground(new Color(30, 30, 40));
			divider.setBorder(BorderFactory.createEmptyBorder());
			add(splitPane);

			mb.setBorder(BorderFactory.createEmptyBorder());

			initMenuBar();

			add(new Ribbon(this), BorderLayout.NORTH);

			setTitle("Sim-Aton");
			setJMenuBar(mb);
			setSize(900, 600);
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);

			bindKey(getRootPane(), "control E", "export", this::export);
			bindKey(getRootPane(), "control S", "save", this::save);
			bindKey(getRootPane(), "control shift S", "saveAs", this::saveAs);
			bindKey(getRootPane(), "control Z", "undo", this::undo);
			bindKey(getRootPane(), "control Y", "redo", this::redo);

			SwingUtilities.invokeLater(() -> splitPane.setDividerLocation(0.75));
		});
	}

	private void initMenuBar() {

		String[][] menuData = { { "File", "New", "Open", "-", "Save", "Save As...", "-", "Export", "-", "Exit" },
				{ "Edit", "Preferences", "-", "Undo", "Redo", "-", "Cut", "Copy", "Paste", "-", "Delete" } };

		for (String[] menu : menuData)
			mb.add(createMenu(menu));

	}

	private JMenu createMenu(String... items) {
		JMenu menu = new JMenu(items[0]);

		for (int i = 1; i < items.length; i++) {
			if (items[i].equals("-")) {
				menu.addSeparator();
			} else {
				JMenuItem menuItem = new JMenuItem(items[i]);
				menu.add(menuItem);
				addMenuListeners(menuItem);
			}
		}

		return menu;
	}

	private void addMenuListeners(JMenuItem menuItem) {
		String text = menuItem.getText().toLowerCase();

		switch (text) {
		case "open":
			menuItem.setAccelerator(KeyStroke.getKeyStroke("control O"));
			break;
		case "save":
			menuItem.setAccelerator(KeyStroke.getKeyStroke("control S"));
			break;
		case "save as...":
			menuItem.setAccelerator(KeyStroke.getKeyStroke("control shift S"));
			break;
		case "export":
			menuItem.setAccelerator(KeyStroke.getKeyStroke("control E"));
			break;
		case "exit":
			menuItem.setAccelerator(KeyStroke.getKeyStroke("control Q"));
			break;
		}

		menuItem.addActionListener(e -> {
			switch (text) {
			case "open":
				open();
				break;
			case "save":
				save();
				break;
			case "save as...":
				saveAs();
				break;
			case "export":
				export();
				break;
			case "exit":
				exit();
				break;
			}
		});
	}

	private void exit() {
		System.exit(0);
	}

	public void open() {
		UITheme.darkTheme();
		JFileChooser fileChooser = new JFileChooser();

		if (lastDirectory != null) {
			fileChooser.setCurrentDirectory(lastDirectory);
		}

		FileNameExtensionFilter filter = new FileNameExtensionFilter("Workspace Files (*.dfax)", "dfax");
		fileChooser.setFileFilter(filter);

		int result = fileChooser.showOpenDialog(this);

		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			thisFile = selectedFile;
			setTitle("Sim-Aton - " + selectedFile.getAbsolutePath());

			lastDirectory = selectedFile.getParentFile();

			loadWorkspace(selectedFile, ObjectPanel.vertices, ObjectPanel.transitions);
		}

	}

	public void save() {
		if (thisFile == null)
			saveAs();
		else
			saveWorkspace(thisFile, ObjectPanel.vertices, ObjectPanel.transitions);
	}

	public void saveAs() {
		JFileChooser fileChooser = new JFileChooser();

		int result = fileChooser.showOpenDialog(null);

		FileNameExtensionFilter filter = new FileNameExtensionFilter("Workspace Files (*.dfax)", "dfax");
		fileChooser.setFileFilter(filter);

		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			saveWorkspace(file, ObjectPanel.vertices, ObjectPanel.transitions);
			thisFile = file;
			setTitle("Sim-Aton - " + file.getAbsolutePath());
		}
	}

	public void saveWorkspace(File file, List<Vertex> vertices, List<Transition> transitions) {
		try (PrintWriter writer = new PrintWriter(file)) {
			for (Vertex v : vertices) {
				writer.printf("<vertex name=\"%s\" x=\"%d\" y=\"%d\"/>%n", v.getData(), v.getX(), v.getY());
			}
			writer.println();
			for (Transition t : transitions) {
				writer.printf("<transition from=\"%s\" to=\"%s\" input=\"%s\" output=\"%s\"/>%n",
						t.getStartVertex().getData(), t.getEndVertex().getData(), t.getInput(), t.getOutput());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadWorkspace(File file, List<Vertex> vertices, List<Transition> transitions) {
		Map<String, Vertex> vertexMap = new HashMap<>();
		vertices.clear();
		transitions.clear();

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("<vertex")) {
					String name = extractAttr(line, "name");
					int x = Integer.parseInt(extractAttr(line, "x"));
					int y = Integer.parseInt(extractAttr(line, "y"));

					Vertex v = new Vertex(x, y, name);
					vertices.add(v);
					vertexMap.put(name, v);
				} else if (line.startsWith("<transition")) {
					String from = extractAttr(line, "from");
					String to = extractAttr(line, "to");
					String input = extractAttr(line, "input");
					String output = extractAttr(line, "output");

					Vertex start = vertexMap.get(from);
					Vertex end = vertexMap.get(to);

					if (start != null && end != null) {
						transitions.add(new Transition(start, end, input, output));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String extractAttr(String line, String attr) {
		int i = line.indexOf(attr + "=\"");
		if (i == -1)
			return "";
		int start = i + attr.length() + 2;
		int end = line.indexOf("\"", start);
		return line.substring(start, end);
	}

	public void export() {
		ExportDialog dialog = new ExportDialog(this);
		dialog.setVisible(true);

		if (dialog.isConfirmed()) {
			ExportDialog.ExportOptions opts = dialog.getOptions();
			BufferedImage img = workspace.exportAsImage(opts.backgroundColor, opts.transparent);

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setSelectedFile(new File("dfa_export." + opts.format));
			int result = fileChooser.showSaveDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				try {
					File file = fileChooser.getSelectedFile();
					ImageIO.write(img, opts.format, file);
					JOptionPane.showMessageDialog(this, "Exported successfully!");
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(this, "Failed to export: " + ex.getMessage());
				}
			}
		}

	}

	public void cut() {
		JOptionPane.showMessageDialog(this, "Cut action not yet implemented.");
	}

	public void copy() {
		JOptionPane.showMessageDialog(this, "Copy action not yet implemented.");
	}

	public void paste() {
		JOptionPane.showMessageDialog(this, "Paste action not yet implemented.");
	}

	public void undo() {
		if (!undoStack.isEmpty()) {
			WorkspaceState current = new WorkspaceState(
				ObjectPanel.copyVertices(),
				ObjectPanel.copyTransitions(ObjectPanel.vertices)
			);
			redoStack.push(current);

			WorkspaceState prev = undoStack.pop();
			restoreState(prev);
		}
	}

	public void redo() {
		if (!redoStack.isEmpty()) {
			pushUndo(false); // Don't clear redo during redo

			WorkspaceState next = redoStack.pop();
			restoreState(next);
		}
	}


	public void runSimulation() {
		JOptionPane.showMessageDialog(this, "Run simulation not yet implemented.");
	}

	public void pushUndo(boolean clearRedo) {
		List<Vertex> copyVertices = ObjectPanel.copyVertices();
		List<Transition> copyTransitions = ObjectPanel.copyTransitions(copyVertices);
		undoStack.push(new WorkspaceState(copyVertices, copyTransitions));
		if (clearRedo) {
			redoStack.clear();
		}
	}


	private void restoreState(WorkspaceState state) {
		ObjectPanel.vertices.clear();
		ObjectPanel.vertices.addAll(state.vertices());

		ObjectPanel.transitions.clear();
		ObjectPanel.transitions.addAll(state.transitions());

		workspace.repaint();
	}

	
	@SuppressWarnings("serial")
	public void bindKey(JComponent component, String keyStroke, String actionName, Runnable action) {
		InputMap im = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = component.getActionMap();

		im.put(KeyStroke.getKeyStroke(keyStroke), actionName);
		am.put(actionName, new AbstractAction() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				action.run();
			}
		});
	}

}
