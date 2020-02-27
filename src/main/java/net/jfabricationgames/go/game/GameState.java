package net.jfabricationgames.go.game;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameState {
	
	private Integer id;
	private PlayerColor[][] state;
	private PlayerColor playersTurn;
	private boolean over;
	private double points;
	private double comi;
	private int blackStonesCaptured;
	private int whiteStonesCaptured;
}