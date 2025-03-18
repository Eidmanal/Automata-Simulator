package objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class MenuButton {

	private String text;
	private Color hoverColor = new Color(30, 36, 45);
	private Color clickColor = new Color(80, 96, 115);
	private Color currentColor;

	private int x, y;


	private int width = 180, height = 25;

	private static final int IDLE = 0;
	private static final int HOVER = 1;
	private static final int CLICKED = 2;

	private int state;

	public MenuButton(String text) {
		this.text = text;
	}

	public void draw(Graphics g) {

		switch (state) {
		case IDLE:
			currentColor = new Color(20, 26, 30);
			break;
		case HOVER:
			currentColor = hoverColor;
			break;
		case CLICKED:
			currentColor = clickColor;
			break;
		default:
			currentColor = new Color(20, 26, 30);
			break;
		}

		g.setColor(currentColor);
		
		if (state != IDLE)
			g.fillRoundRect(x, y, width, height, 10, 10);

		Font font = new Font("Calibri", Font.PLAIN, 16);
		g.setFont(font);

		FontMetrics fm = g.getFontMetrics();
		int textWidth = fm.stringWidth(text);
		int textHeight = fm.getHeight();

		int textX = (width - textWidth) / 2;
		int textY = (height + textHeight) / 2 - fm.getDescent();

		g.setColor(new Color(235, 235, 240));
		g.drawString(text, x + textX, y + textY);
	}
	
	public void hover() {
		state = HOVER;
	}
	
	public void click() {
		state = CLICKED;
	}
	
	public void deactivate() {
		state = IDLE;
	}
		
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	
	public boolean isIn(int X, int Y) {
		return getBounds().contains(X, Y)? true : false;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
