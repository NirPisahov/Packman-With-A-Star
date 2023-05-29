package il.nirpis.astar.graphics.packman;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import il.nirpis.astar.graphics.AstarMap;
import il.nirpis.astar.graphics.CellType;
import il.nirpis.astar.graphics.packman.utils.MapEvents;

public class GameMap extends AstarMap implements MapEvents, Runnable {
	private static final long serialVersionUID = 1L;
	private static final int FPS = 200;
	
	private final List<PackmanBot> packmen;
	private PackmanPlayer player;
	
	private Thread thread;
	private volatile boolean inGame;
	public final GameInfoPanel gameOverPanel;

	public GameMap(int nodeSideSize, CellType[][] map) {
		super(nodeSideSize, map);
		setWithDiagonal(false);
		setGridView(false);
		addMouseListener(this);
		addKeyListener(this);
		
		setFocusable(true);
		requestFocusInWindow();
		int R = getNodeSideSize() / 2 - 2;
		this.packmen = new ArrayList<PackmanBot>();
		generatePackmen(2, R);
		this.player = new PackmanPlayer(new Point(0, 0), R, this::indexToPosition);

		
		this.gameOverPanel = new GameInfoPanel(this::gameOverButtonsEvents);
		
		setLayout(new BorderLayout());
		add(this.gameOverPanel.newPanel("Start Game", "new_game", "exit"), BorderLayout.CENTER);
	}
	
	private void generatePackmen(int amount, int R) {
		PackmanBot pack;
		
		for (int i = 0; i < amount; i++) {
			pack = new PackmanBot(R, this::indexToPosition) {
				@Override
				public void onAchieved() {
					super.onAchieved();
					GameMap.this.inGame = false;
					GameMap.this.repaint();
				}
			};
			this.packmen.add(pack);
		}
	}
	
	@SuppressWarnings("preview")
	private void gameOverButtonsEvents(String buttonActionCommand) {
		switch (buttonActionCommand) {
		case "exit" -> System.exit(0);
		case "new_game" -> initGame();
		default -> System.out.println(buttonActionCommand + " is not recognized as action command!!");
		}
	}
	
	@Override
	@SuppressWarnings("preview")
	public void keyPressed(KeyEvent e) {		
		if (gameOverPanel.isVisible())
			return;
		buildThread();
		
		int keyCode = e.getKeyCode();
		boolean moved = switch (keyCode) {
		case KeyEvent.VK_UP -> player.tryMoveUp(getCells());
		case KeyEvent.VK_DOWN -> player.tryMoveDown(getCells());
		case KeyEvent.VK_LEFT -> player.tryMoveLeft(getCells());
		case KeyEvent.VK_RIGHT -> player.tryMoveRight(getCells());
		default -> false;
		};
		
		if (moved) {
			repaint();
			Point movedTo = new Point(player.getPosX(), player.getPosY());
			if (this.getCellOn(movedTo)
					.equals(CellType.POINT)) {
				player.scoreUp();
				System.out.println("score= " + player.getScore());
				clearAllCells(movedTo);
			}
			
			final Stack<Point> s = new Stack<Point>();
			s.push(new Point(3, 19));
			s.push(new Point(14, 9));
			
			
			this.packmen.parallelStream().forEach((packman) -> {
				Point init;
				if (packman.getPath() == null) { // first path
					init = s.pop(); // TODO has to be random
				}else if(packman.getPath().isEmpty()) // already finished some paths
					init = packman.getLastCellAchieved().getIndex();
				else // there were already path in a progress
					init = packman.getPath().peek().getIndex();
				
				
				LinkedList<Point> indexes = updateAndPrintaStar(init,
						new Point(player.getPosX(), player.getPosY()), false);
				if (indexes == null)
					System.out.println("no path found..");
				else {
					packman.setPath(indexes);
				}
			});
		}
	}
	
	@Override
	protected void paintComponent2D(Graphics2D g) {
		super.paintComponent2D(g);
		packmen.forEach((packman) -> packman.draw(g));
		player.draw(g);
		
		// score
		g.setColor(Color.GREEN);
		g.setFont(new Font("Tahoma", Font.BOLD, 32));
		g.drawString(Integer.toString(player.getScore()), 100, 100);
	}
	
	private Point indexToPosition(Point index) {
		int nodeSideSize = getNodeSideSize();
		int x = (nodeSideSize / 2) + (index.x * nodeSideSize),
				y = (nodeSideSize / 2) + (index.y * nodeSideSize);
		return new Point(x, y);
	}

	@Override
	public void run() {
		// game loop
		while (inGame) {
			try {
				packmen.forEach((packman) -> packman.update());
				repaint();
				Thread.sleep(1000 / FPS);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		
		// after game loop
		packmen.forEach((packman) -> packman.setPath(null));
		this.gameOverPanel.newPanel("Game Over", "new_game", "exit").setVisible(true);
	}
	
	private void initGame() {
		this.gameOverPanel.setVisible(false);
		this.player.moveTo(getCells(), new Point(0, 0));
	}
	
	private void buildThread() {
		if (thread == null || !thread.isAlive()) {
			inGame = true;
			thread = new Thread(this);
			thread.start();
		}
	}
}
