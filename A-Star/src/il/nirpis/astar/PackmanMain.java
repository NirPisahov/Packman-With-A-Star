package il.nirpis.astar;

import javax.swing.JFrame;

import il.nirpis.astar.graphics.packman.GameMap;
import il.nirpis.astar.graphics.packman.utils.MapBuilder;
import il.nirpis.astar.test.MapExamples;

public class PackmanMain {
	//
	public static void main(String[] args) {
		JFrame frame = new JFrame("Packman - A* Algorithm example");
		
		GameMap map = new GameMap(30, MapBuilder.getMap(MapExamples.UTF8_Map));
		
		frame.getContentPane().add(map);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
