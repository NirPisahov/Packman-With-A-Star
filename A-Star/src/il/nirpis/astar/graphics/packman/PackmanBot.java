package il.nirpis.astar.graphics.packman;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.util.Iterator;
import java.util.LinkedList;

import il.nirpis.astar.graphics.packman.utils.Drawable;
import il.nirpis.astar.graphics.packman.utils.PathNode;
import il.nirpis.astar.graphics.packman.utils.Updateable;

public class PackmanBot implements Drawable, Updateable {
	private LinkedList<PathNode> path;
	private PathNode lastCellAchieved;
	private final PathNode.Transferer transferer;
	
	private final int R;
	private final int mouthOpeningAngle = 40; // deg
	private int mouthPosition;
	
	private int posX, posY;
	private int changeX, changeY;
	private int scaleX, scaleY;
	
	private final boolean showPathLine = false;
	private final float pathLineWidth = 3f;
	
	
	public PackmanBot(int R, PathNode.Transferer transferer) {
		this.transferer = transferer;
		this.mouthPosition = 0;
		this.scaleX = 1;
		this.scaleY = 1;
		
		this.R = R;
	}
	
	public PathNode.Transferer getTransferer() {
		return transferer;
	}

	public void setLastCellAchieved(PathNode lastCellAchieved) {
		this.lastCellAchieved = lastCellAchieved;
	}

	@Override
	public void update() {
		if (path == null || path.isEmpty())
			return;
		int dx = dx(), dy = dy();
		// update velocity vector

		if (dx == 0 && dy == 0) {
			lastCellAchieved = path.poll();
			if (!path.isEmpty()) {
				dx = dx();
				dy = dy();
				
				changeX = (dx == 0) ? 0 : dx / Math.abs(dx);
				changeY = (dy == 0) ? 0 : dy / Math.abs(dy);

				if (!(dx == 0 && dy == 0))
					configureMouthPosition();
			} else
				onAchieved();
		}
		
		// update location
		posX += changeX * scaleX;
		posY += changeY * scaleY;
	}
	
	public void onAchieved() {
		pause();
	}

	private int dx() {
		return path.peek().getLocation().x - posX;
	}
	
	private int dy() {
		return path.peek().getLocation().y - posY;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void draw(Graphics2D g) {
		if (path == null)
			return;
		g.setColor(Color.YELLOW);
		Arc2D packman = new Arc2D.Double(posX - R, posY - R, R * 2, R * 2, (mouthOpeningAngle / 2) + (45 * mouthPosition),
				360 - mouthOpeningAngle, Arc2D.PIE);
		g.fill(packman);
		g.setColor(Color.BLACK);
		g.draw(packman);
		
		if (showPathLine) {
			Stroke before = g.getStroke();
			g.setStroke(new BasicStroke(pathLineWidth));
			g.setColor(Color.BLUE);
			
			Iterator<PathNode> path = ((LinkedList<PathNode>) this.path.clone()).listIterator();
					//new LinkedList<>(this.path).listIterator();
			Point p1 = null, p2;
			if (path.hasNext())	
				p1 = path.next().getLocation();
			
			while (path.hasNext()) {
				p2 = path.next().getLocation();
				g.drawLine(p1.x, p1.y, p2.x, p2.y);
				p1 = p2;
			}
			
			g.setStroke(before);
		}
	}
	
	public void pause() {
		this.changeX = 0;
		this.changeY = 0;
	}

	public PathNode getLastCellAchieved() {
		return lastCellAchieved;
	}

	public LinkedList<PathNode> getPath() {
		return path;
	}

	public synchronized void setPath(LinkedList<Point> indexes) {
		if (indexes == null) {
			this.path = null;
			return;
		}
		
		LinkedList<PathNode> pathNew = new LinkedList<PathNode>();
		
		indexes.forEach((index) -> {
			Point pos = transferer.positionOf(index);
			PathNode node = new PathNode(index, pos);
			pathNew.add(node);
		});
		
		if (path != null && !path.isEmpty())
			pathNew.addFirst(path.peek());
		else if (path == null) {
			Point firstLocation = pathNew.peek().getLocation();
			posX = firstLocation.x;
			posY = firstLocation.y;
		}
		
		this.path = pathNew;
	}
	
	private void configureMouthPosition() {
		int new_mouthPosition = 0;
		if (changeX >= 0 && changeY == 0)
			new_mouthPosition = 0;
		else if (changeX == 1 && changeY == -1)
			new_mouthPosition = 1;
		else if (changeX == 0 && changeY == -1)
			new_mouthPosition = 2;
		else if (changeX == -1 && changeY == -1)
			new_mouthPosition = 3;
		else if (changeX == -1 && changeY == 0)
			new_mouthPosition = 4;
		else if (changeX == -1 && changeY == 1)
			new_mouthPosition = 5;
		else if (changeX == 0 && changeY == 1)
			new_mouthPosition = 6;
		else if (changeX == 1 && changeY == 1)
			new_mouthPosition = 7;

		mouthPosition = new_mouthPosition;
	}
}
