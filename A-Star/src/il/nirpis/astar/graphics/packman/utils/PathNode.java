package il.nirpis.astar.graphics.packman.utils;

import java.awt.Point;

public class PathNode {
	private Point index;
	private Point location;

	public PathNode(Point index, Point location) {
		this.index = index;
		this.location = location;
	}

	public Point getIndex() {
		return index;
	}
	
	public void setIndex(Point index) {
		this.index = index;
	}
	
	public Point getLocation() {
		return location;
	}
	
	public void setLocation(Point location) {
		this.location = location;
	}
	
	
	@FunctionalInterface
	public static interface Transferer {
		Point positionOf(Point index);
	}
}
