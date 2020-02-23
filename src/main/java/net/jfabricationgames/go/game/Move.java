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
	
	public Move(int row, int col, PlayerColor color) {
		this.row = row;
		this.col = col;
		this.color = color;
		this.type = Type.STONE;
	}
	
	public static Move getPassMove(PlayerColor color) {
		Move pass = new Move(-1, -1, color);
		pass.setType(Type.PASS);
		return pass;
	}
	
	public boolean isPass() {
		return type == Type.PASS;
	}
	
	public FieldPosition getPos() {
		return new FieldPosition(row, col);
	}
}