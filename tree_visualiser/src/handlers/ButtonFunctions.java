package handlers;

import java.util.ArrayList;
import objects.Vertex;

public class ButtonFunctions {

	public void addVertex(ArrayList<Vertex> vertices, int x, int y) {
		vertices.add(new Vertex(x, y, "x"));
	}
}
