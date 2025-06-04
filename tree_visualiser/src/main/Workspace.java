package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Scrollable;
import javax.swing.Timer;

import objects.Edge;
import objects.Popup;
import objects.Transition;
import objects.Vertex;
import ui.ObjectPanel;
import ui.TransitionWindow;

public class Workspace extends JPanel implements ActionListener, MouseListener, MouseMotionListener, Scrollable {

	private static final long serialVersionUID = 1L;

	Timer timer;
	ArrayList<Vertex> vertices = new ArrayList<>();
	ArrayList<Vertex> recentVertices = new ArrayList<>();

	ArrayList<Edge> edges = new ArrayList<>();

	Popup popup;
	String activeMsg = "";
	
	private Vertex draggedVertex = null;
	Vertex vertexA, vertexB;
	boolean addingEdge = false;
	int edgeState = 0;

	private double zoom = 1.0;
	private Color exportBackgroundOverride = null;

	public Workspace() {
		timer = new Timer(8, this);
		timer.start();

		addMouseListener(this);
		addMouseMotionListener(this);

		initUI();
		initKeyBindings();
	}

	private void initUI() {
		popup = new Popup();
		setPreferredSize(new Dimension((int) (2000 * zoom), (int) (1000 * zoom)));
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		Color bg = exportBackgroundOverride != null ? exportBackgroundOverride : new Color(32, 35, 45);
		g.setColor(bg);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g2d.scale(zoom, zoom);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Objects ______________________________________

		for (Edge edge : edges)
			edge.draw(g);

		for (Transition transition : ObjectPanel.transitions)
			transition.draw(g);

		for (Vertex vertex : vertices)
			vertex.draw(g);

		ObjectPanel.vertices = vertices;

		g.setColor(Color.yellow);
		g.setFont(new Font("Calibri", Font.PLAIN, 15));

		if (noVertexIsActive())
			activeMsg = "None";
		g2d.scale(1 / zoom, 1 / zoom);
		g.drawString("Active Node: " + activeMsg, 20, getHeight() - 100);
		g.drawString("Node Log:    " + recentVertices, 20, getHeight() - 80);
		g.drawString("# Nodes:     " + vertices.size(), 20, getHeight() - 60);

		// Menus ________________________________________ ___ ___ __ __ _ _

		if (popup.isVisible())
			popup.draw(g);

		g2d.dispose();
	}

	private boolean noVertexIsActive() {
		for (Vertex vertex : vertices)
			if (vertex.isActive())
				return false;

		return true;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int mx = (int) (e.getX() / zoom);
		int my = (int) (e.getY() / zoom);
		
		draggedVertex = null;
		for (int i = vertices.size() - 1; i >= 0; i--) { // Top-down priority
			Vertex vertex = vertices.get(i);
			if (!popup.isVisible() && vertex.isIn(mx, my)) {
				draggedVertex = vertex;
				
				Window.instance.pushUndo(true);
				
				for (Vertex v : vertices) {
					if (v != vertex) {
						v.setActive(false);
						v.revertColors();
					}
				}
				
				vertex.setActive(true);
				activeMsg = vertex.getData();
				if (!recentVertices.contains(vertex))
					recentVertices.add(vertex);

				int currentCount = 1;
				if (addingEdge) {
					if (currentCount == 1)
						vertexA = vertex;
					if (currentCount == 2) {
						vertexB = vertex;
						addEdge();
					}
					currentCount++;
				}

				vertex.invertColors();
				break; // stop at the topmost matching vertex
			} else {
				vertex.setActive(false);
				vertex.revertColors();
			}
		}

		for (Popup.PopupItem item : popup.getItems())
			if (item.isIn(e.getX(), e.getY())) {
				switch (item.getText()) {
				case "Add Vertex":
					String demoData = "!";
					demoData = "q" + vertices.size();
					Window.instance.pushUndo(true);
					vertices.add(new Vertex(e.getX(), e.getY(), demoData));
					recentVertices.clear();
					break;
				case "Add Edge":
					addingEdge = true;
					addEdge();
					break;
				case "Add Transition":
					new TransitionWindow();
					break;
				default:
					popup.setVisible(false);
				}
				popup.setVisible(false);
			}

		if (canHidePopup(e)) {
			popup.setVisible(false);

			if (e.getX() < getWidth() - popup.getWidth()) {
				popup.setX(e.getX() + popup.getWidth() / 2);
				popup.setY(e.getY() - popup.getHeight() / 2);
			} else {
				popup.setX(getWidth() - popup.getWidth());
				popup.setY(e.getY() - popup.getHeight() / 2);
			}
		}

	}

	private void addEdge() {
		try {
			while (recentVertices.size() > 2)
				recentVertices.remove(0);

			vertexA = recentVertices.get(0);
			vertexB = recentVertices.get(1);

			vertexA.linkFx();
			vertexB.linkFx();
			
			Window.instance.pushUndo(true);
			Edge edge = new Edge(vertexA, vertexB);
			edges.add(edge);

			recentVertices.clear();
		} catch (Exception e) {

		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		draggedVertex = null;

		if (e.getButton() == MouseEvent.BUTTON3)
			popup.setVisible(!popup.isVisible());

	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int mx = (int) (e.getX() / zoom);
		int my = (int) (e.getY() / zoom);

		if (draggedVertex != null) {
			draggedVertex.setX(mx - draggedVertex.getSize() / 2);
			draggedVertex.setY(my - draggedVertex.getSize() / 2);
		}


	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!popup.isVisible()) {
			if (e.getX() < getWidth() - popup.getWidth()) {
				popup.setX(e.getX() + popup.getWidth() / 2);
				popup.setY(e.getY() - popup.getHeight() / 2);
			} else {
				popup.setX(getWidth() - popup.getWidth());
				popup.setY(e.getY() - popup.getHeight() / 2);
			}
		}

		if (popup.isVisible())
			for (Popup.PopupItem item : popup.getItems())
				if (item.isIn(e.getX(), e.getY()))
					item.activate();
				else
					item.deactivate();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();

		repaint();
	}

	public boolean canHidePopup(MouseEvent e) {
		for (Popup.PopupItem item : popup.getItems())
			if (item.isIn(e.getX(), e.getY()))
				return false;
		return true;
	}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 20; // fine-tune as needed
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 100; // fine-tune as needed
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
	
	@Override
	public void addNotify() {
	    super.addNotify();
	    requestFocusInWindow(); // only works *after* added to container
	}


	public void setZoom(double zoom) {
		this.zoom = Math.max(0.2, Math.min(zoom, 5.0)); // Clamp zoom
		setPreferredSize(new Dimension((int) (2000 * this.zoom), (int) (1000 * this.zoom)));
		revalidate();
		repaint();
	}
	
	public BufferedImage exportAsImage(Color bgColor, boolean transparentBackground) {
		int width = getWidth();
		int height = getHeight();
		int imageType = transparentBackground ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;

		BufferedImage image = new BufferedImage(width, height, imageType);
		Graphics2D g2d = image.createGraphics();

		exportBackgroundOverride = transparentBackground ? new Color(0, 0, 0, 0) : bgColor;

		paint(g2d);

		exportBackgroundOverride = null;
		g2d.dispose();
		return image;
	}

	@SuppressWarnings("serial")
	private void initKeyBindings() {
		InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = getActionMap();

		im.put(KeyStroke.getKeyStroke('+'), "zoomIn");
		im.put(KeyStroke.getKeyStroke('='), "zoomIn"); // for shift+ = (same as +)
		im.put(KeyStroke.getKeyStroke('-'), "zoomOut");

		am.put("zoomIn", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setZoom(zoom + 0.1);
			}
		});
		am.put("zoomOut", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setZoom(zoom - 0.1);
			}
		});
	}


}
