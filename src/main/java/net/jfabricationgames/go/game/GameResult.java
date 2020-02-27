package net.jfabricationgames.go.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResult {
	
	private int pointsBlack;
	private int pointsWhite;
	private int blackStonesCaptured;
	private int whiteStonesCaptured;
	private double comi;
	private double points;
	private PlayerColor winner;
}