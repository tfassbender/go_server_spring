package net.jfabricationgames.go.game;

public enum PlayerColor {
	
	BLACK, WHITE;
	
	public static PlayerColor getOpposizeColor(PlayerColor lastMove) {
		switch (lastMove) {
			case BLACK:
				return WHITE;
			case WHITE:
				return BLACK;
			default:
				return null;
		}
	}
}