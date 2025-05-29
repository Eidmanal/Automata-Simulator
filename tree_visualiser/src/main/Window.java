package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import handlers.UITheme;
import ui.TerminalPanel;

public class Window extends JFrame {

	private static final long serialVersionUID = 1L;

	public Window() {
		SwingUtilities.invokeLater(() -> {
			Workspace workspace = new Workspace();
			TerminalPanel terminal = new TerminalPanel();

			add(new SideBar(), BorderLayout.WEST);
			add(workspace, BorderLayout.CENTER);
			add(terminal, BorderLayout.SOUTH);

			UITheme.windows();
			JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, workspace, terminal);
			splitPane.setResizeWeight(0.8);
			splitPane.setDividerLocation(400);
			splitPane.setDividerSize(8);
			splitPane.setBackground(new Color(30, 30, 40));
			BasicSplitPaneDivider divider = ((BasicSplitPaneUI)splitPane.getUI()).getDivider();
			divider.setBackground(new Color(30, 30, 40));
			divider.setBorder(BorderFactory.createEmptyBorder());
			add(splitPane);
			
			UITheme.crossPlatform();

			Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png"));
			setTitle("Auto VZ");
			setIconImage(icon);
			setSize(900, 600);
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
		});
	}
}
