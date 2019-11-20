package il.nirpis.astar.algorithm;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import il.nirpis.astar.graphics.CellType;

public class AstarAlgoritm {

	public static LinkedList<Point> findPath(CellType[][] map, Point startPoint, Point targetPoint, boolean withDiagonal) {
		Node startNode = new Node(startPoint.x, startPoint.y, null);
		// Node targetNode = new Node(targetPoint.x, targetPoint.y, null);

		if (!walkable(map, startPoint) || !walkable(map, targetPoint))
			return null;

		PriorityQueue<Node> openSet = new PriorityQueue<Node>(map.length * map[0].length,
				(Node n1, Node n2) -> Double.compare(n1.getF(), n2.getF()));
		HashSet<Node> closedSet = new HashSet<Node>();
		openSet.add(startNode);

		Node currentNode;
		while (!openSet.isEmpty()) {
			currentNode = openSet.poll();
			closedSet.add(currentNode);

			if (currentNode.onPoint(targetPoint)) {
				return buildPath(currentNode);
			}

			List<Node> neighbours = getNeighbours(map, currentNode, withDiagonal);
			for (Node neighbour : neighbours) {
				if (!walkable(map, neighbour) || closedSet.contains(neighbour))
					continue;

				double newMovementCost = currentNode.getG() + currentNode.distance(neighbour);
				boolean containsNeighbour = false;

				if (newMovementCost < neighbour.getG() || !(containsNeighbour = openSet.contains(neighbour))) {
					neighbour.setG(newMovementCost);
					// System.out.printf("%s %.2f\n", neighbour.toString(), newMovementCost);
					neighbour.setH(neighbour.distance(targetPoint));
					neighbour.calcF();
					if (!containsNeighbour)
						openSet.add(neighbour);
					else {
						openSet.remove(neighbour);
						openSet.add(neighbour);
					}
				}
			}
		}

		return null;
	}

	private static LinkedList<Point> buildPath(Node currentNode) {
		LinkedList<Point> path = new LinkedList<Point>();
		
		while (currentNode != null) {
			path.addFirst(new Point(currentNode.getxIndex(), currentNode.getyIndex()));
			currentNode = currentNode.getParent();
		}
		return path;
	}

	private static boolean walkable(CellType[][] map, Point check) {
		return map[check.y][check.x].isWalkable();
	}

	private static boolean walkable(CellType[][] map, Node check) {
		return map[check.getyIndex()][check.getxIndex()].isWalkable();
	}

	private static List<Node> getNeighbours(CellType[][] map, Node node, boolean withDiagonal) {
		List<Node> children = new ArrayList<Node>();
		int xIndex_child, yIndex_child;

		for (int y = -1; y <= 1; y++) {
			yIndex_child = node.getyIndex() + y;
			if (yIndex_child == -1)
				continue;
			if (yIndex_child == map.length)
				break;

			for (int x = -1; x <= 1; x++) {
				if (x == 0 && y == 0)
					continue;
				
				if (!withDiagonal && x != 0 && y != 0) // only straight lines
					continue;

				xIndex_child = node.getxIndex() + x;
				if (xIndex_child == -1 || xIndex_child == map[0].length)
					continue;

				Node child = new Node(xIndex_child, yIndex_child, node);
				children.add(child);
			}
		}

		return children;
	}
}