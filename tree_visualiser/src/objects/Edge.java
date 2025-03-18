package objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Edge {

	private Vertex startVertex = null;
	private Vertex endVertex = null;
	
	public Edge() {
		
	}
	
	public Edge(Vertex startVertex, Vertex endVertex) {
		this.startVertex = startVertex;
		this.endVertex = endVertex;
	}
	
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		g.setColor(new Color(230, 236, 245));
		
		float dashPattern[] = {10, 5};
		
		if(getEndVertex() == null)
			g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashPattern, 0));
		else
			g2d.setStroke(new BasicStroke(3));
		
		
		int vX1 = startVertex.getX()+startVertex.getSize()/2;
		int vY1 = startVertex.getY()+startVertex.getSize()/2;
		
		int vX2 = endVertex.getX()+endVertex.getSize()/2;
		int vY2 = endVertex.getY()+endVertex.getSize()/2;
		
		g.drawLine(vX1, vY1, vX2, vY2);
		
	}
	

	
	public Vertex getStartVertex() {
		return startVertex;
	}

	public void setStartVertex(Vertex startVertex) {
		this.startVertex = startVertex;
	}

	public Vertex getEndVertex() {
		return endVertex;
	}

	public void setEndVertex(Vertex endVertex) {
		this.endVertex = endVertex;
	}
	
	
}
