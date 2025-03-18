package objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Vertex {

	private int x;
	private int y;
	private int size = 50;

	private Color bgColor = new Color(80, 140, 200);
	private Color fgColor = new Color(255, 255, 255);

	private String data;
	private Vertex parent = null, child = null;
	private boolean isActive = false;

	public Vertex(int x, int y, String data) {
		this.x = x;
		this.y = y;
		this.data = data;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, size, size);
	}

	public boolean isIn(int X, int Y) {
		return getBounds().contains(X, Y) ? true : false;
	}

	public void draw(Graphics g) {
		g.setColor(getBgColor());
		g.fillOval(x, y, size, size);

		g.setColor(getFgColor());
		Font font = new Font("Arial Black", Font.PLAIN, 18);
		g.setFont(font);

		FontMetrics fm = g.getFontMetrics();
		int textWidth = fm.stringWidth(data);
		int textHeight = fm.getHeight();

		int textX = (size - textWidth) / 2;
		int textY = (size + textHeight) / 2 - fm.getDescent();

		g.drawString(data, x + textX, y + textY);
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

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Color getBgColor() {
		return bgColor;
	}

	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}

	public Color getFgColor() {
		return fgColor;
	}

	public void setFgColor(Color fgColor) {
		this.fgColor = fgColor;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Vertex getParent() {
		return parent;
	}

	public void setParent(Vertex parent) {
		this.parent = parent;
	}

	public Vertex getChild() {
		return child;
	}

	public void setChild(Vertex child) {
		this.child = child;
	}

	@Override
	public String toString() {
		// return "Vertex [data=" + data + ", parent=" + parent.getData() + ", child=" +
		// child.getData() + "]";
		return "Vertex [data=" + data + "]";
	}

	public void invertColors() {
		setBgColor(new Color(255, 255, 255));
		setFgColor(new Color(80, 140, 200));
	}

	public void revertColors() {
		setBgColor(new Color(80, 140, 200));
		setFgColor(new Color(255, 255, 255));
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
