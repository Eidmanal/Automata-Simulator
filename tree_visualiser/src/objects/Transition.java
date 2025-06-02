package objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

public class Transition {

	private Vertex startVertex = null;
	private Vertex endVertex = null;
	private String input, output;

	public Transition() {

	}

	public Transition(Vertex startVertex, Vertex endVertex, String input, String output) {
		this.startVertex = startVertex;
		this.endVertex = endVertex;
		this.input = input;
		this.output = output;
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g.setColor(new Color(230, 236, 245));
		g2d.setStroke(new BasicStroke(3));

		if (!startVertex.equals(endVertex)) {
			int vX1 = startVertex.getX() + startVertex.getSize() / 2;
			int vY1 = startVertex.getY() + startVertex.getSize() / 2;

			int vX2 = endVertex.getX() + endVertex.getSize() / 2;
			int vY2 = endVertex.getY() + endVertex.getSize() / 2;

			drawArrow(g2d, vX1, vY1, startVertex.getRadius(), vX2, vY2, endVertex.getRadius());

			g.setColor(Color.yellow);
			g.drawString(input + " / " + output, vX1 + (vX2 - vX1) / 2, (vY1 + (vY2 - vY1) / 2) - 10);
		} else {
			int vX1 = startVertex.getX() + startVertex.getRadius();
			int vY1 = startVertex.getY() + startVertex.getRadius();

			drawCircularLoop(g2d, vX1, vY1, startVertex.getRadius());

			g.setColor(Color.yellow);
			g.drawString(input + " / " + output, vX1 - startVertex.getRadius() / 2, vY1 - 60);
		}
	}

	private void drawArrow(Graphics2D g2d, int x1, int y1, int r1, int x2, int y2, int r2) {

		double dx = x2 - x1;
		double dy = y2 - y1;
		double dist = Math.hypot(dx, dy);

		if (dist == 0)
			return; // Prevent divide-by-zero

		// Normalized direction vector
		double unitX = dx / dist;
		double unitY = dy / dist;

		// Compute new start and end points (on the edge of each circle)
		int startX = (int) (x1 + unitX * r1);
		int startY = (int) (y1 + unitY * r1);
		int endX = (int) (x2 - unitX * r2) - 3;
		int endY = (int) (y2 - unitY * r2) - 3;

		// Main line
		g2d.drawLine(startX, startY, endX, endY);

		// Arrowhead
		double phi = Math.toRadians(30);
		int barb = 10;

		double theta = Math.atan2(endY - startY, endX - startX);
		for (int j = 0; j < 2; j++) {
			double rho = theta + (j == 0 ? phi : -phi);
			double x = endX - barb * Math.cos(rho);
			double y = endY - barb * Math.sin(rho);
			g2d.drawLine(endX, endY, (int) x, (int) y);
		}

	}

	private void drawCircularLoop(Graphics2D g2d, int cx, int cy, int radius) {
		int loopRadius = radius - 4;
		int offset = 12;

		double arcX = cx - loopRadius;
		double arcY = cy - 2 * loopRadius - offset;
		double arcDiameter = loopRadius * 2;

		Arc2D.Double arc = new Arc2D.Double(arcX, arcY, arcDiameter, arcDiameter, 212, -240, Arc2D.OPEN);
		g2d.draw(arc);

		// Actual arc end point
		Point2D end = arc.getEndPoint();
		double endX = end.getX();
		double endY = end.getY();

		// Arc center
		double centerX = arcX + arcDiameter / 2;
		double centerY = arcY + arcDiameter / 2;

		// Angle from center to end point
		double dx = endX - centerX;
		double dy = endY - centerY;
		double theta = Math.atan2(dy, dx);

		// Clockwise tangent vector
		double tangentAngle = theta + Math.PI / 2;

		drawArrowHead(g2d, endX, endY, tangentAngle);
	}

	private void drawArrowHead(Graphics2D g2d, double x, double y, double angle) {
		double phi = Math.toRadians(40); // Angle of the arrow wings
		int barb = 10; // Length of the arrow wings

		for (int j = 0; j < 2; j++) {
			double rho = angle + (j == 0 ? phi : -phi);
			double x2 = x - barb * Math.cos(rho);
			double y2 = y - barb * Math.sin(rho);
			g2d.drawLine((int) x, (int) y, (int) x2, (int) y2);
		}
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
	
	public String getInput() {
		return input;
	}
	
	public String getOutput() {
		return output;
	}
	

}
