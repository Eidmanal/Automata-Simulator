package objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Vertex {

	private int x;
	private int y;
	private int size = 50;

	private Color bgColor = new Color(90, 180, 180);
	private Color fgColor = new Color(255, 255, 255);

	private String data;
	private boolean isActive = false;
	private boolean justLinked = false;

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
		Graphics2D g2d = (Graphics2D)g;
		
		g.setColor(getBgColor());
		g.fillOval(x, y, size, size);

		g.setColor(getFgColor());
		Font font = new Font("Bauhaus 93", Font.PLAIN, 18);
		g.setFont(font);

		float dashPattern[] = {6, 6};
		
		if(justLinked) {
			g.setColor(new Color(255, 255, 255, 70));
			g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashPattern, 0));
			g.drawOval(x-8, y-8, size+16, size+16);
		}
		g.setColor(getFgColor());
		
		
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

	@Override
	public String toString() {
		// return "Vertex [data=" + data + ", parent=" + parent.getData() + ", child=" +
		// child.getData() + "]";
		return "Vertex [data=" + data + "]";
	}

	public void invertColors() {
		setBgColor(new Color(255, 255, 255));
		setFgColor(new Color(90, 180, 180));		
		justLinked = false;
	}

	public void revertColors() {
		setBgColor(new Color(90, 180, 180));
		setFgColor(new Color(255, 255, 255));
		
		//TODO Set for final state
		//setBgColor(new Color(255, 100, 100));
		//setFgColor(new Color(250, 240, 200));
		
		justLinked = false;
	}
	
	public void linkFx() {
		setBgColor(new Color(245, 200, 50));
		setFgColor(new Color(255, 255, 255));	
		justLinked = true;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public int getRadius() {
		return getSize()/2;
	}
	
	public boolean equals(Vertex vertex) {
		return this.getData().equals(vertex.getData());
	}

}
