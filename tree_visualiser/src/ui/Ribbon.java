package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import handlers.UITheme;
import main.Window;

public class Ribbon extends JPanel {
	private static final long serialVersionUID = 1L;

	public Ribbon(Window window) {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setPreferredSize(new Dimension(1000, 75));
		setBackground(new Color(60, 60, 65));

		RibbonSection file = new RibbonSection(1, 3, "File");
		file.addTool(new Tool("new", window));
		file.addTool(new Tool("save", window));
		file.addTool(new Tool("open", window));

		RibbonSection edit = new RibbonSection(1, 5, "Edit");
		edit.addTool(new Tool("paste", window));
		edit.addTool(new Tool("cut", window));
		edit.addTool(new Tool("copy", window));
		edit.addTool(new Tool("undo", window));
		edit.addTool(new Tool("redo", window));

		RibbonSection state = new RibbonSection(1, 2, "State");
		state.addTool(new Tool("add state", window));
		state.addTool(new Tool("delete state", window));

		RibbonSection transition = new RibbonSection(1, 2, "Transition");
		transition.addTool(new Tool("add transition", window));
		transition.addTool(new Tool("delete transition", window));

		RibbonSection run = new RibbonSection(1, 1, "Run");
		run.addTool(new Tool("run", window));

		add(file);
		add(edit);
		add(state);
		add(transition);
		add(run);
	}
}

class RibbonSection extends JPanel {
	private static final long serialVersionUID = 1L;

	public RibbonSection(int rows, int columns, String label) {
		setLayout(new BorderLayout(2, 2));
		setBackground(new Color(60, 60, 65));

		JPanel grid = new JPanel(new GridLayout(rows, columns, 5, 5));
		grid.setOpaque(false);
		add(grid, BorderLayout.CENTER);

		JPanel titleArea = new JPanel(new FlowLayout(FlowLayout.CENTER));

		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(90, 90, 90)),
				new EmptyBorder(10, 10, 0, 10)));

		JLabel titleLabel = new JLabel(label);
		titleLabel.setForeground(new Color(170, 170, 170));

		titleArea.setPreferredSize(new Dimension(0, 20));
		titleArea.setOpaque(false);
		titleArea.add(titleLabel);
		add(titleArea, BorderLayout.SOUTH);
	}

	public void addTool(Tool tool) {
		((JPanel) getComponent(0)).add(tool);
	}

	public void removeBorder() {
		setBorder(BorderFactory.createEmptyBorder());
	}
}

class Tool extends JButton {
	private static final long serialVersionUID = 1L;
	private String text;
	private boolean hovered = false;
	private boolean pressed = false;

	public Tool(String text, Window window) {
		this.text = text.toLowerCase();

		UITheme.darkTheme();
		ImageIcon icon = new ImageIcon("img/" + text + ".png");
		Image scaledImage = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
		setIcon(new ImageIcon(scaledImage));

		setFocusPainted(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setOpaque(false);
		setToolTipText(text);
		setPreferredSize(new Dimension(36, 36));

		addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent e) {
				hovered = true;
				repaint();
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				hovered = false;
				pressed = false;
				repaint();
			}

			@Override
			public void mousePressed(java.awt.event.MouseEvent e) {
				pressed = true;
				repaint();
			}

			@Override
			public void mouseReleased(java.awt.event.MouseEvent e) {
				pressed = false;
				repaint();
				handleAction(window);
			}
		});
	}

	private void handleAction(Window window) {
		if (text.equals("add state") || text.equals("delete state") || text.equals("add transition")
				|| text.equals("delete transition") || text.equals("cut") || text.equals("paste")) {
			window.pushUndo(true);
		}

		switch (text) {
		case "new":
			JOptionPane.showMessageDialog(null, "New action is not implemented yet.");
			break;
		case "open":
			window.open();
			break;
		case "save":
			window.save();
			break;
		case "save as":
			window.saveAs();
			break;
		case "export":
			window.export();
			break;
		case "cut":
			window.cut();
			break;
		case "copy":
			window.copy();
			break;
		case "paste":
			window.paste();
			break;
		case "undo":
			window.undo();
			break;
		case "redo":
			window.redo();
			break;
		case "run":
			window.runSimulation();
			break;
		case "add state":
		case "delete state":
		case "add transition":
		case "delete transition":
			JOptionPane.showMessageDialog(null, "Tool '" + text + "' not yet implemented.");
			break;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (hovered || pressed) {
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Color highlight = pressed ? new Color(255, 255, 255, 80) : new Color(255, 255, 255, 40);
			g2d.setColor(highlight);
			g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
			g2d.dispose();
		}
		super.paintComponent(g);
	}

	public String getToolText() {
		return text;
	}
}
