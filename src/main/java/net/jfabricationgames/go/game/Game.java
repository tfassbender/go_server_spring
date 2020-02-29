package net.jfabricationgames.go.game;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.jfabricationgames.go.server.data.GameCreation;
import net.jfabricationgames.go.server.data.User;

@Entity
@Data
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
	
	private int blackStonesCaptured;
	private int whiteStonesCaptured;
	
	private static final String MOVE_STRING_DELEMITER = ";";
	private static final String MOVE_STRING_NUMBER_DELEMITER = ",";
	private static final char MOVE_STRING_WHITE = 'W';
	private static final char MOVE_STRING_BLACK = 'B';
	private static final char MOVE_STRING_PASS = 'P';
	
	public Game(int id, LocalDate started, LocalDate lastPlayed, User playerBlack, User playerWhite, String moves, List<Move> moveList, int boardSize,
			boolean resigned, boolean over, double points, double comi, int handycap, int blackStonesCaptured, int whiteStonesCaptured) {
		super();
		this.id = id;
		this.started = started;
		this.lastPlayed = lastPlayed;
		this.playerBlack = playerBlack;
		this.playerWhite = playerWhite;
		this.moves = moves;
		this.moveList = moveList;
		this.boardSize = boardSize;
		this.resigned = resigned;
		this.over = over;
		this.points = points;
		this.comi = comi;
		this.handycap = handycap;
		this.blackStonesCaptured = blackStonesCaptured;
		this.whiteStonesCaptured = whiteStonesCaptured;
		
		//create a new referee to test the preconditions of the game
		new Referee(this);
	}
	
	/**
	 * Build a list of moves from a move string.
	 * 
	 * Examples for move strings:
	 * <ul>
	 * <li>W0,5; (white on row 0, col 5)</li>
	 * <li>B5,18; (black on row 5, col 18)</li>
	 * <li>WP; (white passed)</li>
	 * </ul>
	 */
	public static List<Move> fromMoveString(String moveString) {
		List<Move> moveList = new ArrayList<Move>(moveString.length() / 5);//move strings have a minimum of 5 chars
		String[] moves = moveString.split(MOVE_STRING_DELEMITER);
		for (int i = 0; i < moves.length; i++) {
			if (moves[i].length() > 0) {//prevent empty moves (because the move string could end with ';')
				Move move = null;
				//get the player that made the move
				PlayerColor color = null;
				if (moves[i].charAt(0) == MOVE_STRING_BLACK) {
					color = PlayerColor.BLACK;
				}
				else if (moves[i].charAt(0) == MOVE_STRING_WHITE) {
					color = PlayerColor.WHITE;
				}
				else {
					throw new IllegalStateException("Unexpected player color char in movement code: '" + moves[i].charAt(0) + "'");
				}
				
				//check for passing moves
				if (moves[i].charAt(1) == MOVE_STRING_PASS) {
					move = Move.getPassMove(color);
				}
				else {
					//get the row and column of the move
					String fieldCode = moves[i].substring(1);
					String[] fields = fieldCode.split(MOVE_STRING_NUMBER_DELEMITER);
					int row = Integer.parseInt(fields[0]);
					int col = Integer.parseInt(fields[1]);
					
					move = new Move(row, col, color);
				}
				
				//add the new move
				moveList.add(move);
			}
		}
		return moveList;
	}
	
	public List<Move> getMovesAsList() {
		if (moveList == null) {
			if (moves == null) {
				moves = "";
			}
			moveList = fromMoveString(moves);
		}
		return moveList;
	}
	
	public void startGame(GameCreation gameCreation) {
		boardSize = gameCreation.getBoardSize();
		comi = gameCreation.getComi();
		handycap = gameCreation.getHandycap();
		resigned = false;
		over = false;
	}
	
	public GameState toGameState() {
		Referee referee = new Referee(this);
		return new GameState(id, referee.getBoardCopy(), referee.getNextMoveColor(), boardSize, over, points, comi, blackStonesCaptured,
				whiteStonesCaptured);
	}
	
	/**
	 * Check whether the move is valid and execute it if it's valid.
	 */
	public void makeMove(Move move) throws IllegalArgumentException {
		Referee referee = new Referee(this);
		if (referee.isValidMove(move)) {
			addMove(move);
		}
		else {
			throw new IllegalArgumentException("the move is not valid");
		}
	}
	
	/**
	 * Add a move without checking whether it's valid.
	 */
	public void addMove(Move move) {
		if (moveList == null) {
			moveList = new ArrayList<Move>();
		}
		moveList.add(move);
		moves += toMoveString(move);
	}
	
	/**
	 * Calculate the total points by the current comi, the current captured stones and the counted points in the GameResult object
	 */
	public void calculatePoints(GameResult countedPoints) {
		int pointsBlack = countedPoints.getPointsBlack();
		int pointsWhite = countedPoints.getPointsWhite();
		
		points = pointsBlack - pointsWhite - comi + whiteStonesCaptured - blackStonesCaptured;
	}
	
	public GameResult getResult() {
		PlayerColor winner = null;
		if (points < 0) {
			winner = PlayerColor.WHITE;
		}
		else if (points > 0) {
			winner = PlayerColor.BLACK;
		}
		return new GameResult(0, 0, blackStonesCaptured, whiteStonesCaptured, comi, points, winner);
	}
	
	private String toMoveString(Move move) {
		StringBuilder sb = new StringBuilder();
		if (move.getColor() == PlayerColor.BLACK) {
			sb.append(MOVE_STRING_BLACK);
		}
		else {
			sb.append(MOVE_STRING_WHITE);
		}
		if (move.isPass()) {
			sb.append(MOVE_STRING_PASS);
		}
		else {
			sb.append(move.getRow());
			sb.append(MOVE_STRING_NUMBER_DELEMITER);
			sb.append(move.getCol());
		}
		sb.append(MOVE_STRING_DELEMITER);
		
		return sb.toString();
	}
}