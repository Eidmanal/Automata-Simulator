package main;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Window extends JFrame {

	private static final long serialVersionUID = 1L;

	public Window() {
		
		add(new SideBar(), BorderLayout.WEST);
		add(new Workspace(), BorderLayout.CENTER);
		
		setTitle("AutoSim");
		setSize(900, 600);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
