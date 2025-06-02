package ui;

import java.awt.Font;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Label extends JLabel {
	
	public Label(String text) {
		setText(text);
		setFont(new Font("Calibri", Font.PLAIN, 14));
	}
}
