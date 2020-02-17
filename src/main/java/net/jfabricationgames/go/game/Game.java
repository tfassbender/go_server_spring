package net.jfabricationgames.go.game;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.jfabricationgames.go.server.data.GameCreation;
import net.jfabricationgames.go.server.data.User;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private LocalDate started;
	private LocalDate lastPlayed;
	
	//references users.id
	//	private long playerBlack;
	//references users.id
	//	private long playerWhite;
	@ManyToOne
	private User playerBlack;
	@ManyToOne
	private User playerWhite;
	
	private String moves;
	@Transient
	private List<Move> moveList;
	
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
	public List<Move> getMovesAsList() {
		//TODO
		return null;
	}
	
	public void startGame(GameCreation gameCreation) {
		boardSize = gameCreation.getBoardSize();
		comi = gameCreation.getComi();
		handycap = gameCreation.getHandycap();
		resigned = false;
		over = false;
	}
}