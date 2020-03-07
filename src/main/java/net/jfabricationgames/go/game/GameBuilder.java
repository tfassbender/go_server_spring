package net.jfabricationgames.go.game;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import net.jfabricationgames.go.server.data.User;

@Getter
public class GameBuilder {
	
	private LocalDate started;
	private LocalDate lastPlayed;
	
	private User playerBlack;
	private User playerWhite;
	
	private String moves;
	private List<Move> moveList;
	
	private int boardSize;
	
	private boolean resigned;
	private boolean over;
	private double points;
	private double comi;
	private int handycap;
	
	private int blackStonesCaptured;
	private int whiteStonesCaptured;
	
	public GameBuilder() {
		
	}
	
	public Game build() {
		return new Game(0, started, lastPlayed, playerBlack, playerWhite, moves, moveList, boardSize, resigned, over, points, comi, handycap,
				blackStonesCaptured, whiteStonesCaptured);
	}
	
	public GameBuilder setStarted(LocalDate started) {
		this.started = started;
		return this;
	}
	
	public GameBuilder setLastPlayed(LocalDate lastPlayed) {
		this.lastPlayed = lastPlayed;
		return this;
	}
	
	public GameBuilder setPlayerBlack(User playerBlack) {
		this.playerBlack = playerBlack;
		return this;
	}
	
	public GameBuilder setPlayerWhite(User playerWhite) {
		this.playerWhite = playerWhite;
		return this;
	}
	
	public GameBuilder setMoves(String moves) {
		this.moves = moves;
		return this;
	}
	
	public GameBuilder setMoveList(List<Move> moveList) {
		this.moveList = moveList;
		return this;
	}
	
	public GameBuilder setBoardSize(int boardSize) {
		this.boardSize = boardSize;
		return this;
	}
	
	public GameBuilder setResigned(boolean resigned) {
		this.resigned = resigned;
		return this;
	}
	
	public GameBuilder setOver(boolean over) {
		this.over = over;
		return this;
	}
	
	public GameBuilder setPoints(double points) {
		this.points = points;
		return this;
	}
	
	public GameBuilder setComi(double comi) {
		this.comi = comi;
		return this;
	}
	
	public GameBuilder setHandycap(int handycap) {
		this.handycap = handycap;
		return this;
	}
	
	public GameBuilder setBlackStonesCaptured(int blackStonesCaptured) {
		this.blackStonesCaptured = blackStonesCaptured;
		return this;
	}
	
	public GameBuilder setWhiteStonesCaptured(int whiteStonesCaptured) {
		this.whiteStonesCaptured = whiteStonesCaptured;
		return this;
	}
}