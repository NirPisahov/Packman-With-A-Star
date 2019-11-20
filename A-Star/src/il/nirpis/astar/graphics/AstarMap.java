package il.nirpis.astar.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JPanel;

import il.nirpis.astar.algorithm.AstarAlgoritm;

public class AstarMap extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final RenderingHints ANTIALIASING = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
	private final int pointR;
	
	private final int nodeSideSize;
	private final int amountOfNodesX;
	private final int amountOfNodesY;
	public final Dimension size;
	
	private boolean gridView;
	
	private boolean withDiagonal;

	public static final Color backColor = new Color(0, 150, 255);
	private final CellType[][] cells;

	static {
		ANTIALIASING.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	}
	
	public AstarMap(int nodeSideSize, int amountOfNodesX, int amountOfNodesY) {
		this(nodeSideSize, new CellType[amountOfNodesY][amountOfNodesX]);
	}
	
	public int getAmountOfNodesX() {
		return amountOfNodesX;
	}

	public int getAmountOfNodesY() {
		return amountOfNodesY;
	}

	public AstarMap(int nodeSideSize, CellType[][] map) {
		this.nodeSideSize = nodeSideSize;
		this.amountOfNodesX = map[0].length;
		this.amountOfNodesY = map.length;
		this.cells = map;

		this.withDiagonal = true;
		
		this.gridView = true;
		
		this.pointR = nodeSideSize / 4;
		
		setPreferredSize(this.size = new Dimension(nodeSideSize * amountOfNodesX, nodeSideSize * amountOfNodesY));
		setBackground(backColor);
	}

	public LinkedList<Point> updateAndPrintaStar(Point startNode, Point targetNode, boolean paintPath) {
		resetCanvas();
		LinkedList<Point> path = AstarAlgoritm.findPath(this.cells, startNode, targetNode, this.withDiagonal);
		if (path == null)
			return null;

		if(paintPath) {
			paintAll(CellType.PATH, path);
			paintCell(startNode, CellType.START);
			paintCell(targetNode, CellType.TARGET);
		}
		repaint();
		
		return path;
	}
	
	public CellType[][] getCells() {
		return cells;
	}

	public boolean isWithDiagonal() {
		return withDiagonal;
	}

	public void setWithDiagonal(boolean withDiagonal) {
		this.withDiagonal = withDiagonal;
	}

	public void paintCell(Point cell, CellType type) {
		this.cells[cell.y][cell.x] = (CellType) type;
	}

	public void paintAll(CellType type, Point... cells) {
		for (Point cell : cells)
			paintCell(cell, type);
	}

	public void paintAll(CellType type, Collection<Point> cells) {
		for (Point cell : cells)
			paintCell(cell, type);
	}

	public void setWall(Point... cells) {
		paintAll(CellType.WALL, cells);
	}
	
	public void setWall(Collection<Point> cells) {
		paintAll(CellType.WALL, cells);
	}
	
	public void clearAllCells(Point... cells) {
		paintAll(CellType.NOTHING, cells);
	}
	
	public void clearAllCells(Collection<Point> cells) {
		paintAll(CellType.NOTHING, cells);
	}

	public void resetCanvas() {
		for (int i = 0; i < this.amountOfNodesY; i++) {
			for (int j = 0; j < this.amountOfNodesX; j++) {
				if (this.cells[i][j] == null || this.cells[i][j].isResetable())
					this.cells[i][j] = CellType.NOTHING;
			}
		}
	}

	@Override
	protected final void paintComponent(Graphics gr) {
		Graphics2D g = (Graphics2D) gr.create();
		super.paintComponent(g);
		g.addRenderingHints(ANTIALIASING);
		paintComponent2D(g);
	}
	
	protected void paintComponent2D(Graphics2D g) {
		CellType type;
		for (int i = 0; i < this.amountOfNodesY; i++) {
			for (int j = 0; j < this.amountOfNodesX; j++) {
				if ((type = this.cells[i][j]) != null && !type.equals(CellType.NOTHING)) {
					g.setColor(type.getTypeColor());
					if (type.equals(CellType.POINT))
						g.fillOval((int) ((j + 0.5) * this.nodeSideSize - this.pointR),
								(int) ((i + 0.5) * this.nodeSideSize - this.pointR),
								this.pointR * 2, this.pointR * 2);
					else
						g.fillRect(j * this.nodeSideSize, i * this.nodeSideSize, this.nodeSideSize, this.nodeSideSize);
				}
			}
		}
		
		// draw grid
		if (gridView) {
			g.setColor(Color.WHITE);
			int current;
			for (int xGrid = 1; xGrid < this.amountOfNodesX; xGrid++) {
				current = xGrid * this.nodeSideSize;
				g.drawLine(current, 0, current, getHeight());
			}

			for (int yGrid = 1; yGrid < this.amountOfNodesY; yGrid++) {
				current = yGrid * this.nodeSideSize;
				g.drawLine(0, current, getWidth(), current);
			}
		}
	}

	public static CellType getCellOn(CellType[][] map, Point check) {
		return map[check.y][check.x];
	}
	
	public CellType getCellOn(Point check) {
		return this.cells[check.y][check.x];
	}
	
	public boolean isGridView() {
		return gridView;
	}

	public void setGridView(boolean gridView) {
		this.gridView = gridView;
	}

	public int getNodeSideSize() {
		return nodeSideSize;
	}
}
