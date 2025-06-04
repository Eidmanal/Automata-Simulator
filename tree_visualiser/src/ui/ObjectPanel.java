package ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import objects.Transition;
import objects.Vertex;

public class ObjectPanel {

	public static ArrayList<Vertex> vertices = new ArrayList<>();
	public static ArrayList<Transition> transitions = new ArrayList<>();
	
	public static List<Vertex> copyVertices() {
		List<Vertex> copy = new ArrayList<>();
		for (Vertex v : vertices) {
			copy.add(new Vertex(v.getX(), v.getY(), v.getData()));
		}
		return copy;
	}

	public static List<Transition> copyTransitions(List<Vertex> copyVertices) {
		Map<String, Vertex> nameMap = new HashMap<>();
		for (Vertex v : copyVertices) {
			nameMap.put(v.getData(), v);
		}

		List<Transition> copy = new ArrayList<>();
		for (Transition t : transitions) {
			Vertex start = nameMap.get(t.getStartVertex().getData());
			Vertex end = nameMap.get(t.getEndVertex().getData());
			if (start != null && end != null) {
				copy.add(new Transition(start, end, t.getInput(), t.getOutput()));
			}
		}
		return copy;
	}

}
