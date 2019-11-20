package il.nirpis.astar.graphics.packman;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Arc2D;

import il.nirpis.astar.graphics.AstarMap;
import il.nirpis.astar.graphics.CellType;
import il.nirpis.astar.graphics.packman.utils.Drawable;
import il.nirpis.astar.graphics.packman.utils.PathNode;

public class PackmanPlayer implements Drawable {
	private final int R;
	private final int mouthOpeningAngle = 40; // deg
	private int mouthPosition;
	private final PathNode.Transferer transferer;
	private int score;
	
	private int posX, posY;
	private Point location;
	
	public PackmanPlayer(Point initPos, int R, PathNode.Transferer transferer) {
		this.mouthPosition = 0;
		this.R = R;
		
		this.posX = initPos.x;
		this.posY = initPos.y;
		
		this.score = 0;
		
		this.location = transferer.positionOf(initPos);
		this.transferer = transferer;
	}
	
	public boolean moveTo(CellType[][] cells, Point target) {
		if (AstarMap.getCellOn(cells, target).equals(CellType.WALL))
			return false;
		
		mouthPosition = 0;
		posX = target.x;
		posY = target.y;
		location = transferer.positionOf(new Point(posX, posY));
		return true;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		Arc2D packman = new Arc2D.Double(location.x - R, location.y - R, R * 2, R * 2, (mouthOpeningAngle / 2) + (45 * mouthPosition),
				360 - mouthOpeningAngle, Arc2D.PIE);
		g.fill(packman);
		g.setColor(Color.BLACK);
		g.draw(packman);
	}
	
	public boolean tryMoveUp(CellType[][] cells) {
		mouthPosition = 2;
		
		if (posY == 0)
			return false;
		
		if (AstarMap.getCellOn(cells, new Point(posX, posY - 1)).equals(CellType.WALL))
			return false;
		
		posY--;
		location = transferer.positionOf(new Point(posX, posY));
		return true;
	}
	
	public boolean tryMoveDown(CellType[][] cells) {
		mouthPosition = 6;
		
		int rowsAmount = cells.length;
		if (posY == rowsAmount - 1)
			return false;
		
		if (AstarMap.getCellOn(cells, new Point(posX, posY + 1)).equals(CellType.WALL))
			return false;
		
		posY++;
		location = transferer.positionOf(new Point(posX, posY));
		return true;
	}
	
	public boolean tryMoveLeft(CellType[][] cells) {
		mouthPosition = 4;
		
		if (posX == 0)
			return false;
		
		if (AstarMap.getCellOn(cells, new Point(posX - 1, posY)).equals(CellType.WALL))
			return false;
		
		posX--;
		location = transferer.positionOf(new Point(posX, posY));
		return true;
	}
	
	public boolean tryMoveRight(CellType[][] cells) {
		mouthPosition = 0;
		
		int columnsAmount = cells[0].length;
		if (posX == columnsAmount - 1)
			return false;
		
		if (AstarMap.getCellOn(cells, new Point(posX + 1, posY)).equals(CellType.WALL))
			return false;
		
		posX++;
		location = transferer.positionOf(new Point(posX, posY));
		return true;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public Point getLocation() {
		return location;
	}

	public int getScore() {
		return score;
	}

	public void scoreUp() {
		this.score += 1;
	}
}
