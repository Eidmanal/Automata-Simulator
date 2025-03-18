package objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Popup {

	private boolean isVisible = false;
	
	private int x, y;
	private int width = 130;
	private int height = 200;
	
	Color bgColor = new Color(245, 245, 255);
	
	ArrayList<PopupItem> items = new ArrayList<>();
	
	public Popup() {
		initItems();
	}
	
	private void initItems() {
		items.add(new PopupItem("Add Edge", 0));
		items.add(new PopupItem("Add Vertex", 20));
		items.add(new PopupItem("Edit Vertex", 40));
		items.add(new PopupItem("Delete Vertex", 60));
		
		height = items.size() * 20;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	
	public boolean isIn(int X, int Y) {
		return getBounds().contains(X, Y)? true : false;
	}
	
	public ArrayList<PopupItem> getItems(){
		return items;
	}
	
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public void draw(Graphics g) {
		g.setColor(new Color(245, 245, 255));
		g.fillRoundRect(x, y, width, height, 10, 10);
		
		for(PopupItem item : items)
			item.draw(g);
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

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public class PopupItem{
		
		private int Y, height = 20;
		Font font = new Font("Calibri", Font.PLAIN, 12);
		String text;
		
		Color color = bgColor;
		
		public PopupItem(String text, int Y) {
			this.text = text;
			this.Y = Y;
		}
		
		public void draw(Graphics g) {
			g.setColor(color);
			g.fillRoundRect(x, y+Y, width, height, 10, 10);
			
			g.setColor(new Color(40, 30, 56));
			g.setFont(font);
			
			FontMetrics fm = g.getFontMetrics();
			int textWidth = fm.stringWidth(text);
			int textHeight = fm.getHeight();

			int textX = (width - textWidth) / 2;
			int textY = (height + textHeight) / 2 - fm.getDescent();

			g.drawString(text, x+textX, y+Y+textY);
		}
		
		public void activate() {
			color = new Color(130, 145, 168);
		}
		
		public void deactivate() {
			color = new Color(245, 245, 255);
		}
		

		public Rectangle getBounds() {
			return new Rectangle(x, y+Y, width, height);
		}
		
		public boolean isIn(int X, int Y) {
			return getBounds().contains(X, Y)? true : false;
		}
		
		public String getText() {
			return text;
		}
		
	}
}
