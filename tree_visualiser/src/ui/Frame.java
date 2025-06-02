package ui;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Frame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png"));
	
	public Frame() {
		setIconImage(icon);
	}
}
