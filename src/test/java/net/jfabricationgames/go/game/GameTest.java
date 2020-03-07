package net.jfabricationgames.go.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class GameTest {
	
	@Test
	public void testFromMoveString() {
		String moveString = "B2,2;W6,6;B3,6;W5,3";
		List<Move> moves = Game.fromMoveString(moveString);
		
		List<Move> expected = Arrays.asList(new Move(2, 2, PlayerColor.BLACK), new Move(6, 6, PlayerColor.WHITE), new Move(3, 6, PlayerColor.BLACK),
				new Move(5, 3, PlayerColor.WHITE));
		assertEquals(expected, moves);
	}
	@Test
	public void testFromMoveString_delemiterAtTheEnd() {
		String moveString = "B2,2;W6,6;B3,6;W5,3;";
		List<Move> moves = Game.fromMoveString(moveString);
		
		List<Move> expected = Arrays.asList(new Move(2, 2, PlayerColor.BLACK), new Move(6, 6, PlayerColor.WHITE), new Move(3, 6, PlayerColor.BLACK),
				new Move(5, 3, PlayerColor.WHITE));
		assertEquals(expected, moves);
	}
	@Test
	public void testFromMoveString_emptyString() {
		String moveString = "";
		List<Move> moves = Game.fromMoveString(moveString);
		List<Move> expected = new ArrayList<Move>();
		assertEquals(expected, moves);
	}
	@Test
	public void testFromMoveString_withPassMove() {
		String moveString = "B2,2;W6,6;B3,6;W5,3;BP;WP;";
		List<Move> moves = Game.fromMoveString(moveString);
		
		List<Move> expected = Arrays.asList(new Move(2, 2, PlayerColor.BLACK), new Move(6, 6, PlayerColor.WHITE), new Move(3, 6, PlayerColor.BLACK),
				new Move(5, 3, PlayerColor.WHITE), Move.getPassMove(PlayerColor.BLACK), Move.getPassMove(PlayerColor.WHITE));
		assertEquals(expected, moves);
	}
}