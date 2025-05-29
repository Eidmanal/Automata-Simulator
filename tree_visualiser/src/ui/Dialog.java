package ui;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Dialog {
	private int x, y;
	private int width, height;

	public Dialog(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void paint(Graphics g) {
		g.setColor(new Color(250, 250, 250));
		g.fillRoundRect(x, y, width, 40, 20, 20);
		
		g.setColor(new Color(10, 18, 30));
		g.fillRect(x, y+20, width, 40);
		g.fillRoundRect(x, y+30, width, height-30, 20, 20);
	}
	
}
