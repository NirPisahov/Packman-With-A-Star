package il.nirpis.astar.graphics.packman.utils;

import il.nirpis.astar.graphics.CellType;

public class MapBuilder {	
	public static CellType[][] getMap(String[] UTF_8_map) {
		int rows = UTF_8_map.length, columns = UTF_8_map[0].length();
		
		CellType[][] returnedMap = new CellType[rows][columns];
		
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				char visual = UTF_8_map[row].charAt(column);
				CellType cellType = getCellType(visual);
				returnedMap[row][column] = cellType;
			}
		}
		
		return returnedMap;
	}

	@SuppressWarnings("preview")
	private static CellType getCellType(char visual) {
		return switch (visual) {
		case '•' -> CellType.NOTHING;
		case '◘' -> CellType.WALL;
		case '*' -> CellType.POINT;
		default -> null;
		};
	}
}
