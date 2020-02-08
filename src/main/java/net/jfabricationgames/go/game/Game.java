package net.jfabricationgames.go.game;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {
	
	private long id;
	private LocalDate started;
	private LocalDate lastPlayed;
	
	//references users.id
	private long playerBlack;
	//references users.id
	private long playerWhite;
	
	private List<Move> moves;
	
	//references boards.size
	private int boardSize;
	
	private boolean resigned;
	private boolean over;
	private double points;
	private double comi;
	private int handycap;
	
	public static List<Move> fromMoveString(String moveString) {
		//TODO
		return null;
	}
	
	public String getMovesAsString() {
		// TODO Auto-generated method stub
		return null;
	}
}