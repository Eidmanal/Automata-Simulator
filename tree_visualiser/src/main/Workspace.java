package main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import objects.Edge;
import objects.Popup;
import objects.Vertex;

public class Workspace extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
	private static final long serialVersionUID = 1L;

	Timer timer;
	ArrayList<Vertex> vertices = new ArrayList<>();
	ArrayList<Vertex> recentVertices = new ArrayList<>();

	ArrayList<Edge> edges = new ArrayList<>();

	Popup popup;
	String activeMsg = "";
	Vertex vertexA, vertexB;
	boolean addingEdge = false;
	int edgeState = 0;

	public Workspace() {
		timer = new Timer(8, this);
		timer.start();

		addMouseListener(this);
		addMouseMotionListener(this);

		initUI();
	}

	private void initUI() {
		popup = new Popup();
	}

	public void paint(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(new Color(40, 50, 75));
		g.fillRect(0, 0, getWidth(), getHeight());

		// Objects ______________________________________

		for (Edge edge : edges)
			edge.draw(g);

		for (Vertex vertex : vertices)
			vertex.draw(g);

		g.setColor(Color.yellow);
		g.setFont(new Font("Calibri", Font.PLAIN, 19));

		if (noVertexIsActive())
			activeMsg = "None";

		g.drawString("Active Node: " + activeMsg, 20, getHeight() - 100);
		g.drawString("Node Log:    " + recentVertices, 20, getHeight() - 80);
		g.drawString("# Nodes:     " + vertices.size(), 20, getHeight() - 60);

		// Menus ________________________________________

		if (popup.isVisible())
			popup.draw(g);

		g.dispose();
	}

	private boolean noVertexIsActive() {
		for (Vertex vertex : vertices)
			if (vertex.isActive())
				return false;

		return true;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (Vertex vertex : vertices)
			if (!popup.isVisible())
				if (vertex.isIn(e.getX(), e.getY())) {
					vertex.setActive(true);
					activeMsg = vertex.getData();
					if(!recentVertices.contains(vertex))
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
				} else {
					vertex.setActive(false);
					vertex.revertColors();
				}

		for (Popup.PopupItem item : popup.getItems())
			if (item.isIn(e.getX(), e.getY())) {
				switch (item.getText()) {
				case "Add Vertex":
					vertices.add(new Vertex(e.getX(), e.getY(), recentVertices.size() + "x"));
					break;
				case "Add Edge":
					addingEdge = true;
					addEdge();
					break;
				default:
					popup.setVisible(false);
				}
				popup.setVisible(false);
			}

	}

	private void addEdge() {
		try {
			while(recentVertices.size() > 2)
				recentVertices.remove(0);
			
			vertexA = recentVertices.get(0);
			vertexB = recentVertices.get(1);
			
			Edge edge = new Edge(vertexA, vertexB);
			edges.add(edge);
			
			recentVertices.clear();
		} catch (Exception e) {

		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		if (e.getButton() == MouseEvent.BUTTON3)
			popup.setVisible(!popup.isVisible());

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		for (Vertex vertex : vertices)
			if (vertex.isIn(e.getX(), e.getY()) && vertex.isActive()) {
				vertex.setX(e.getX() - vertex.getSize() / 2);
				vertex.setY(e.getY() - vertex.getSize() / 2);
				vertex.invertColors();
			} else {
				vertex.revertColors();
			}

		for (Vertex vertex : vertices)
			if (vertex.isActive()) {
				vertex.setX(e.getX() - vertex.getSize() / 2);
				vertex.setY(e.getY() - vertex.getSize() / 2);
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

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
