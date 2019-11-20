package il.nirpis.astar.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

import javax.swing.JFrame;

import il.nirpis.astar.graphics.AstarMap;
import il.nirpis.astar.graphics.CellType;
import il.nirpis.astar.graphics.packman.utils.MapBuilder;

public class AstarTest extends AstarMap implements MouseListener {
	private static final long serialVersionUID = 1L;
	
	private Point currentlyClickedCell;
	
	//
	public static void main(String[] args) {
		JFrame frame = new JFrame("A* Algorithm Test");
		
		AstarTest map = new AstarTest(30, MapBuilder.getMap(MapExamples.PackMan_UTF8));
		
		frame.getContentPane().add(map);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public AstarTest(int nodeSideSize, CellType[][] map) {
		super(nodeSideSize, map);
		setGridView(false);
		setWithDiagonal(false);
		
		addMouseListener(this);
		currentlyClickedCell = new Point(0, 0);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Point startNode = new Point(0, 0);
		
		int nodeSideSize = getNodeSideSize();
		int targetX = e.getX() / nodeSideSize, 
				targetY = e.getY() / nodeSideSize;
		
		this.currentlyClickedCell = new Point(targetX, targetY);
		LinkedList<Point> path = updateAndPrintaStar(startNode, this.currentlyClickedCell, true);
		
		if (path == null)
			System.out.println("No path found..");
		
		repaint();
	}
	
	@Override
	protected void paintComponent2D(Graphics2D g) {
		super.paintComponent2D(g);
		
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Tahoma", Font.BOLD, 32));
		g.drawString(String.format("(%d, %d)", 
				this.currentlyClickedCell.x,
				this.currentlyClickedCell.y), 60, 27);
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
