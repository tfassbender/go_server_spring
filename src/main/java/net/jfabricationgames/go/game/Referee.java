package net.jfabricationgames.go.game;

import java.util.Collection;
import java.util.List;

import com.google.common.annotations.VisibleForTesting;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode
@ToString
public class Referee {
	
	private Game game;
	
	private PlayerColor[][] board;
	private PlayerColor[][] previousBoard;
	
	private PlayerColor lastMoveColor;
	private Move lastMove;
	
	public Referee(Game game) {
		this.game = game;
		//to load the move list from the string representation
		this.game.getMovesAsList();
		board = new PlayerColor[game.getBoardSize()][game.getBoardSize()];
		
		//if there are no stones set BLACK starts (lastMove is WHITE)
		if (game.getHandycap() == 0) {
			lastMoveColor = PlayerColor.WHITE;
		}
		else {
			lastMoveColor = PlayerColor.BLACK;
		}
		
		//reset the captured stones because the moves will be executed again
		game.setBlackStonesCaptured(0);
		game.setWhiteStonesCaptured(0);
		//fill the board my making all moves again
		fillBoard(game.getMoveList());
	}
	
	/**
	 * Execute all moves in the list to create the current board
	 */
	@VisibleForTesting
	/*private*/ void fillBoard(List<Move> moves) throws IllegalArgumentException {
		if (moves != null) {
			for (Move move : moves) {
				if (isValidMove(move)) {
					addMove(move);
				}
				else {
					throw new IllegalArgumentException("Invalid move. The given list of moves contains a move that is not valid: " + move);
				}
			}
		}
	}
	
	/**
	 * A deep copy of the current board
	 */
	@VisibleForTesting
	/*private*/ PlayerColor[][] getBoardCopy() {
		PlayerColor[][] newBoard = new PlayerColor[getBoardSize()][getBoardSize()];
		for (int i = 0; i < getBoardSize(); i++) {
			for (int j = 0; j < getBoardSize(); j++) {
				newBoard[i][j] = board[i][j];
			}
		}
		return newBoard;
	}
	
	/**
	 * Check whether the move is valid
	 * 
	 * Checked are:
	 * <ul>
	 * <li>color (players turn)</li>
	 * <li>position (on field and empty)</li>
	 * <li>no suicidal move (placed stone is not directly beaten)</li>
	 * <li>ko rule (move doesn't create the same board that was there in the last move)</li>
	 * </ul>
	 */
	public boolean isValidMove(Move move) {
		//log.info("Referee: {}", this);
		if (game.isOver()) {
			//game over
			log.info("invalid move: the game is over");
			return false;
		}
		if (move.getColor() != PlayerColor.getOpposizeColor(lastMoveColor)) {
			//wrong color
			log.info("invalid move: wrong color");
			return false;
		}
		if (move.isPass()) {
			//if color is correct pass is always valid
			return true;
		}
		else {//no passing move
			if (!move.getPos().exists(getBoardSize())) {
				//position doesn't exist
				log.info("invalid move: position doesn't exist");
				return false;
			}
			if (getStoneColor(move.getPos()) != null) {
				//field not empty
				log.info("invalid move: field not empty");
				return false;
			}
			
			//execute the move and check whether the state is valid
			PlayerColor[][] tmpBoard = getBoardCopy();//keep a copy of the board for a rollback
			//make the move
			board[move.getRow()][move.getCol()] = move.getColor();
			removeBeaten(move);
			
			//check the group of the new stone
			Group group = Group.findGroup(this, move.getPos());
			if (group.isBeaten(this)) {
				//added stone is directly beaten
				board = tmpBoard;
				log.info("invalid move: stone would directly be beaten");
				return false;
			}
			
			if (boardsEqual(board, previousBoard)) {
				//restores the board from the last move (ko rule violated)
				board = tmpBoard;
				log.info("invalid move: ko rule violation");
				return false;
			}
			
			board = tmpBoard;
		}
		return true;
	}
	
	@VisibleForTesting
	/*private*/ static boolean boardsEqual(PlayerColor[][] board, PlayerColor[][] previousBoard) {
		if (previousBoard == null) {
			//previousBoard can be null if there was no previous move
			//in this case the ko-rule can't be violated and the boards are treated as not equal (so false is returned)
			return false;
		}
		boolean equal = true;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				equal &= board[i][j] == previousBoard[i][j];
			}
		}
		return equal;
	}
	
	/**
	 * Execute a move without checking whether it's valid. The new stone is added and beaten ones are removed
	 */
	public void addMove(Move move) {
		if (!move.isPass()) {
			//copy the board to the previous board field
			previousBoard = getBoardCopy();
			
			//set the new stone
			board[move.getRow()][move.getCol()] = move.getColor();
			//remove beaten stones (if any)
			removeBeaten(move, true);
		}
		
		//set the last move color
		lastMoveColor = move.getColor();
		
		if (move.isPass() && lastMove != null && lastMove.isPass()) {
			//both players passed -> game over
			game.setOver(true);
		}
		lastMove = move;
	}
	
	/**
	 * Remove all stones that were beaten by the move
	 */
	@VisibleForTesting
	/*private*/ void removeBeaten(Move move) {
		removeBeaten(move, false);
	}
	private void removeBeaten(Move move, boolean countBeaten) {
		//find the fields near to the move field
		List<FieldPosition> near = move.getPos().getFieldsNear(game.getBoardSize());
		for (FieldPosition pos : near) {
			//check whether the field has a stone of the different color on it
			if (getStoneColor(pos) == PlayerColor.getOpposizeColor(move.getColor())) {
				Group group = Group.findGroup(this, pos);
				if (group.isBeaten(this)) {
					removeStones(group.getStones(), countBeaten);
				}
			}
		}
	}
	
	/**
	 * Remove all stones in the collection from the field
	 */
	@VisibleForTesting
	/*private*/ void removeStones(Collection<FieldPosition> stones) {
		removeStones(stones, false);
	}
	private void removeStones(Collection<FieldPosition> stones, boolean countBeaten) {
		int stonesBeaten = stones.size();
		PlayerColor beatenStonesColor = null;
		for (FieldPosition pos : stones) {
			beatenStonesColor = board[pos.getRow()][pos.getCol()];
			board[pos.getRow()][pos.getCol()] = null;
		}
		
		if (countBeaten) {
			if (beatenStonesColor == PlayerColor.BLACK) {
				game.setBlackStonesCaptured(game.getBlackStonesCaptured() + stonesBeaten);
			}
			else if (beatenStonesColor == PlayerColor.WHITE) {
				game.setWhiteStonesCaptured(game.getWhiteStonesCaptured() + stonesBeaten);
			}
		}
	}
	
	public PlayerColor getLastMoveColor() {
		return lastMoveColor;
	}
	public PlayerColor getNextMoveColor() {
		return PlayerColor.getOpposizeColor(lastMoveColor);
	}
	
	public boolean isFieldEmpty(FieldPosition pos) {
		return board[pos.getRow()][pos.getCol()] == null;
	}
	
	public PlayerColor getStoneColor(FieldPosition pos) {
		return board[pos.getRow()][pos.getCol()];
	}
	public int getBoardSize() {
		return game.getBoardSize();
	}
	
	@VisibleForTesting
	/*private*/ void setBoard(PlayerColor[][] board) {
		this.board = board;
	}
	@VisibleForTesting
	/*private*/ void setPlayersTurn(PlayerColor playersTurn) {
		this.lastMoveColor = PlayerColor.getOpposizeColor(playersTurn);
	}
	@VisibleForTesting
	/*private*/ void setPreviouseBoard(PlayerColor[][] board) {
		this.previousBoard = board;
	}
}