package il.nirpis.astar.algorithm;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import java.awt.Point;

public class Node {
	private final int xIndex, yIndex;
	private double f, g, h;
	private Node parent;
	
	public Node(int xIndex, int yIndex) {
		this(xIndex, yIndex, null);
	}
	
	public Node(int xIndex, int yIndex, Node parent) {
		this.xIndex = xIndex;
		this.yIndex = yIndex;
		
		this.f = 0;
		this.g = 0;
		this.h = 0;
		
		this.parent = parent;
	}
	
	public double getF() {
		return f;
	}

	public void setF(double f) {
		this.f = f;
	}

	public double getG() {
		return g;
	}

	public void setG(double g) {
		this.g = g;
	}

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}
	
	public int getxIndex() {
		return xIndex;
	}

	public int getyIndex() {
		return yIndex;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public double distance(Node target) {
		return distance(new Point(target.xIndex, target.yIndex));
	}
	
	public double distance(Point target) {
		int dx, dy;
		double distance;
		if (xIndex == target.x) {
			distance = abs(yIndex - target.y);
		} else if (yIndex == target.y) {
			distance = abs(xIndex - target.x);
		} else {
			dx = xIndex - target.x;
			dy = yIndex - target.y;
			
			distance = sqrt(dx * dx + dy * dy);// / sqrt(2);
		}
		
		return distance;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Node))
			return false;
		Node check = (Node) obj;
		return check.xIndex == this.xIndex &&
				check.yIndex == this.yIndex;
	}
	
	public boolean onPoint(Point point) {
		return this.xIndex == point.x && this.yIndex == point.y;
	}
	
	@Override
	public int hashCode() {
		int yIndexLength = (int) Math.log10(this.yIndex) + 1;
		int hash = (int) (xIndex * Math.pow(10, yIndexLength)) + this.yIndex;
		return hash;
	}

	public void calcF() {
		this.f = this.g + this.h;
	}
	
	@Override
	public String toString() {
		return String.format("[x: %d, y: %d]", xIndex, yIndex);
	}
	
}
