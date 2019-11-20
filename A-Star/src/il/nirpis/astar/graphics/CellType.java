package il.nirpis.astar.graphics;

import java.awt.Color;

public enum CellType {
	NOTHING(0, AstarMap.backColor, true, false),
	START(1, Color.GREEN, true, true),
	TARGET(2, Color.GRAY, true, true),
	PATH(3, Color.CYAN/* AstarMap.backColor */, true, true),
	WALL(4, Color.BLACK, false, false),
	POINT(5, Color.DARK_GRAY, true, false);

	private final int typeNum;
	private final boolean walkable;
	private final Color typeColor;
	private final boolean resetable;

	CellType(int typeNum, Color typeColor, boolean walkable, boolean resetable) {
		this.typeNum = typeNum;
		this.typeColor = typeColor;
		this.walkable = walkable;
		this.resetable = resetable;
	}

	@SuppressWarnings("preview")
	public CellType getByTypeNum(int typeNum) {
		return switch (typeNum) {
 		case 1 -> START;
		case 2 -> TARGET;
		case 3 -> PATH;
		case 4 -> WALL;
		case 5 -> POINT;
		default -> NOTHING;
		};
	}

	public Color getTypeColor() {
		return typeColor;
	}

	public boolean isWalkable() {
		return walkable;
	}

	public boolean isResetable() {
		return resetable;
	}

	public int getTypeNum() {
		return typeNum;
	}

	public boolean equals(CellType type) {
		return this.typeNum == type.getTypeNum();
	}
}