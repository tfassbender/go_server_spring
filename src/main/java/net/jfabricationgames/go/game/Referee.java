package net.jfabricationgames.go.game;

import java.util.Collection;
import java.util.List;

import com.google.common.annotations.VisibleForTesting;

public class Referee {
	
	private Game game;
	
	private PlayerColor[][] board;
	private PlayerColor[][] previousBoard;
	
	private PlayerColor lastMove;
	
	public Referee(Game game) {
		this.game = game;
		board = new PlayerColor[game.getBoardSize()][game.getBoardSize()];
		
		//if there are no stones set BLACK starts (lastMove is WHITE)
		if (game.getHandycap() == 0) {
			lastMove = PlayerColor.WHITE;
		}
		else {
			lastMove = PlayerColor.BLACK;
		}
		
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
		if (move.getColor() != PlayerColor.getOpposizeColor(lastMove)) {
			//wrong color
			return false;
		}
		if (move.isPass()) {
			//if color is correct pass is always valid
			return true;
		}
		else {//no passing move
			if (!move.getPos().exists(getBoardSize())) {
				//position doesn't exist
				return false;
			}
			if (getStoneColor(move.getPos()) != null) {
				//field not empty
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
				return false;
			}
			
			if (boardsEqual(board, previousBoard)) {
				//restores the board from the last move (ko rule violated)
				board = tmpBoard;
				return false;
			}
			
			board = tmpBoard;
		}
		return true;
	}
	
	@VisibleForTesting
	/*private*/ static boolean boardsEqual(PlayerColor[][] board, PlayerColor[][] previousBoard) {
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
		//copy the board to the previous board field
		previousBoard = getBoardCopy();
		
		//set the new stone
		board[move.getRow()][move.getCol()] = move.getColor();
		//remove beaten stones (if any)
		removeBeaten(move);
		
		//set the last move color
		lastMove = move.getColor();
	}
	
	/**
	 * Remove all stones that were beaten by the move
	 */
	@VisibleForTesting
	/*private*/ void removeBeaten(Move move) {
		//find the fields near to the move field
		List<FieldPosition> near = move.getPos().getFieldsNear(game.getBoardSize());
		for (FieldPosition pos : near) {
			//check whether the field has a stone of the different color on it
			if (getStoneColor(pos) == PlayerColor.getOpposizeColor(move.getColor())) {
				Group group = Group.findGroup(this, pos);
				if (group.isBeaten(this)) {
					removeStones(group.getStones());
				}
			}
		}
	}
	
	/**
	 * Remove all stones in the collection from the field
	 */
	@VisibleForTesting
	/*private*/ void removeStones(Collection<FieldPosition> stones) {
		for (FieldPosition pos : stones) {
			board[pos.getRow()][pos.getCol()] = null;
		}
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
		this.lastMove = PlayerColor.getOpposizeColor(playersTurn);
	}
	@VisibleForTesting
	/*private*/ void setPreviouseBoard(PlayerColor[][] board) {
		this.previousBoard = board;
	}
}