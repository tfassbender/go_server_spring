package net.jfabricationgames.go.game;

import lombok.Data;

@Data
public class Move {
	
	public enum Type {
		STONE, PASS;
	}
	
	private int row;
	private int col;
	private Type type;
	private PlayerColor color;
}