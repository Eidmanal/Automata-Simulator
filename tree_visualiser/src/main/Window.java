package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import handlers.UITheme;
import objects.Transition;
import objects.Vertex;
import ui.Frame;
import ui.ObjectPanel;
import ui.TerminalPanel;

public class Window extends Frame {
	private File lastDirectory = null;
	private File thisFile = null;

	private static final long serialVersionUID = 1L;
	private JMenuBar mb = new JMenuBar();

	public Window() {
		SwingUtilities.invokeLater(() -> {

			Workspace workspace = new Workspace();
			TerminalPanel terminal = new TerminalPanel();

			UITheme.darkTheme();
			JScrollPane scrollPane = new JScrollPane(workspace);
			scrollPane.getViewport().setBackground(new Color(40, 50, 75)); // optional match
			scrollPane.setBorder(BorderFactory.createEmptyBorder());

			add(new SideBar(), BorderLayout.WEST);

			JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, terminal);
			splitPane.setResizeWeight(0.8);
			splitPane.setDividerLocation(400);
			splitPane.setDividerSize(8);
			splitPane.setBackground(new Color(30, 30, 40));
			BasicSplitPaneDivider divider = ((BasicSplitPaneUI) splitPane.getUI()).getDivider();
			divider.setBackground(new Color(30, 30, 40));
			divider.setBorder(BorderFactory.createEmptyBorder());
			add(splitPane);

			mb.setBorder(BorderFactory.createEmptyBorder());

			initMenuBar();

			setTitle("Sim-Aton");
			setJMenuBar(mb);
			setSize(900, 600);
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);

		});
	}

	private void initMenuBar() {

		String[][] menuData = { { "File", "New", "Open", "-", "Save", "Save As...", "-", "Exit" },
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
		menuItem.addActionListener(e -> {
			switch (menuItem.getText().toLowerCase()) {
			case "open":
				open();
				break;
			case "save":
				save();
				break;
			case "save as...":
				saveAs();
				break;	
			case "exit":
				System.exit(0);
				break;
			}
		});
	}

	private void open() {
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
			setTitle("Sim-Aton - "+selectedFile.getAbsolutePath());
			
			lastDirectory = selectedFile.getParentFile();

			loadWorkspace(selectedFile, ObjectPanel.vertices, ObjectPanel.transitions);
		}

	}

	
	public void save() {
		if(thisFile == null)
			saveAs();
		else
			saveWorkspace(thisFile, ObjectPanel.vertices, ObjectPanel.transitions);
	}

	public void saveAs() {
		JFileChooser fileChooser = new JFileChooser();
		
		int result = fileChooser.showOpenDialog(null);

		FileNameExtensionFilter filter = new FileNameExtensionFilter("Workspace Files (*.dfax)", "dfax");
		fileChooser.setFileFilter(filter);

		if(result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			saveWorkspace(file, ObjectPanel.vertices, ObjectPanel.transitions);
			thisFile = file;
			setTitle("Sim-Aton - "+file.getAbsolutePath());
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

}
