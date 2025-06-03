package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import handlers.UITheme;

public class Ribbon extends JPanel {
	private static final long serialVersionUID = 1L;

	public Ribbon() {
		
		RibbonSection file = new RibbonSection(1, 2, "File");
		file.addTool(new Tool("new"));
		file.addTool(new Tool("save"));
		file.addTool(new Tool("open"));
		
		RibbonSection edit = new RibbonSection(1, 3, "Edit");
		edit.addTool(new Tool("paste"));
		edit.addTool(new Tool("cut"));
		edit.addTool(new Tool("copy"));
		
		RibbonSection state = new RibbonSection(1, 3, "State");
		state.addTool(new Tool("add state"));
		state.addTool(new Tool("delete state"));
		
		RibbonSection run = new RibbonSection(1, 3, "Run");
		run.addTool(new Tool("run"));
		
		add(file);
		add(edit);
		add(state);
		add(run);
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setPreferredSize(new Dimension(1000, 80));
		setBackground(new Color(60, 60, 65));
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
		
		setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(90, 90, 90)),
			    new EmptyBorder(10, 10, 0, 10)
		));
		
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
	
	public Tool(String name) {
		UITheme.darkTheme();
        ImageIcon icon = new ImageIcon("img/"+name+".png");

        Image scaledImage = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        setIcon(scaledIcon);

        //setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        
        setToolTipText(name);
	}
}