package net.jfabricationgames.go.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

class GroupTest {
	
	private PlayerColor[][] createBoard_1() {
		final PlayerColor b = PlayerColor.BLACK;
		final PlayerColor w = PlayerColor.WHITE;
		final PlayerColor e = null;
		
		PlayerColor[][] board = new PlayerColor[][] {//
				{b, b, b, e, w},//
				{b, b, e, e, w},//
				{b, e, e, e, e},//
				{b, b, w, w, w},//
				{w, e, b, w, b},//
		};
		
		return board;
	}
	
	private PlayerColor[][] createBoard_2() {
		final PlayerColor b = PlayerColor.BLACK;
		final PlayerColor w = PlayerColor.WHITE;
		final PlayerColor e = null;
		
		PlayerColor[][] board = new PlayerColor[][] {//
				{b, b, b, e, w},//
				{b, b, e, e, w},//
				{b, e, e, b, b},//
				{b, b, b, w, w},//
				{w, e, b, w, b},//
		};
		
		return board;
	}
	
	/**
	 * Create field position objects from int values (as int[n][2])
	 */
	private Set<FieldPosition> toFieldPositions(int[][] positions) {
		Set<FieldPosition> fieldPositions = Stream.of(positions).map(pos -> new FieldPosition(pos[0], pos[1])).collect(Collectors.toSet());
		
		return fieldPositions;
	}
	
	/**
	 * Create a referee that holds the given board (getBoardSize(), getStoneColor(...) and isFieldEmpty(...) are mocked)
	 */
	private Referee createReferee(PlayerColor[][] board) {
		Referee ref = mock(Referee.class);
		when(ref.getBoardSize()).thenReturn(board.length);
		when(ref.getStoneColor(any(FieldPosition.class))).thenAnswer(new Answer<PlayerColor>() {
			
			@Override
			public PlayerColor answer(InvocationOnMock invocation) throws Throwable {
				//get the parameter
				FieldPosition pos = (FieldPosition) invocation.getArgument(0);
				//look up the position in the given board
				return board[pos.getRow()][pos.getCol()];
			}
		});
		when(ref.isFieldEmpty(any(FieldPosition.class))).thenAnswer(new Answer<Boolean>() {
			
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				//get the parameter
				FieldPosition pos = (FieldPosition) invocation.getArgument(0);
				//look up the position in the given board and check whether it's empty
				return board[pos.getRow()][pos.getCol()] == null;
			}
		});
		
		return ref;
	}
	
	@Test
	public void testFindGroup_blackTopLeft() {
		int[][] expectedPositions = new int[][] {{0, 0}, {0, 1}, {0, 2}, {1, 0}, {1, 1}, {2, 0}, {3, 0}, {3, 1}};
		
		//groups are equal for every starting position -> check some
		assertGroupsEqual(createBoard_1(), new int[] {0, 0}, expectedPositions);
		assertGroupsEqual(createBoard_1(), new int[] {0, 1}, expectedPositions);
		assertGroupsEqual(createBoard_1(), new int[] {3, 0}, expectedPositions);
		assertGroupsEqual(createBoard_1(), new int[] {1, 1}, expectedPositions);
	}
	
	@Test
	public void testFindGroup_blackDownMid() {
		//group of only one stone
		int[][] expectedPositions = new int[][] {{4, 2}};
		assertGroupsEqual(createBoard_1(), new int[] {4, 2}, expectedPositions);
	}
	
	@Test
	public void testFindGroup_blackDownRight() {
		//group of only one stone
		int[][] expectedPositions = new int[][] {{4, 4}};
		assertGroupsEqual(createBoard_1(), new int[] {4, 4}, expectedPositions);
	}
	
	@Test
	public void testFindGroup_whiteDownLeft() {
		int[][] expectedPositions = new int[][] {{3, 2}, {3, 3}, {3, 4}, {4, 3}};
		
		//groups are equal for every starting position -> check some
		assertGroupsEqual(createBoard_1(), new int[] {3, 2}, expectedPositions);
		assertGroupsEqual(createBoard_1(), new int[] {4, 3}, expectedPositions);
	}
	
	@Test
	public void testFindGroup_checkGroupColor_black() {
		//top left
		assertEquals(PlayerColor.BLACK, Group.findGroup(createReferee(createBoard_1()), new FieldPosition(0, 0)).getColor());
		//down mid
		assertEquals(PlayerColor.BLACK, Group.findGroup(createReferee(createBoard_1()), new FieldPosition(4, 2)).getColor());
		//down right
		assertEquals(PlayerColor.BLACK, Group.findGroup(createReferee(createBoard_1()), new FieldPosition(4, 4)).getColor());
	}
	
	@Test
	public void testFindGroup_checkGroupColor_white() {
		//top right
		assertEquals(PlayerColor.WHITE, Group.findGroup(createReferee(createBoard_1()), new FieldPosition(0, 4)).getColor());
		//down right
		assertEquals(PlayerColor.WHITE, Group.findGroup(createReferee(createBoard_1()), new FieldPosition(3, 3)).getColor());
		//down left
		assertEquals(PlayerColor.WHITE, Group.findGroup(createReferee(createBoard_1()), new FieldPosition(4, 0)).getColor());
	}
	
	@Test
	public void testFindGroup_emptyField() {
		assertThrows(IllegalArgumentException.class, () -> assertGroupsEqual(createBoard_1(), new int[] {0, 3}, new int[0][0]));
	}
	
	@Test
	public void testIsBeaten_unbeatenGroups() {
		//check some groups that are not beaten
		assertFalse(isGroupBeaten(createBoard_1(), 0, 0));
		assertFalse(isGroupBeaten(createBoard_1(), 4, 2));
		assertFalse(isGroupBeaten(createBoard_1(), 3, 3));
	}
	
	@Test
	public void testIsBeaten_beatenGroup_oneStone() {
		assertTrue(isGroupBeaten(createBoard_1(), 4, 4));
	}
	
	@Test
	public void testIsBeaten_beatenGroup_multipleStones() {
		assertTrue(isGroupBeaten(createBoard_2(), 4, 3));
	}
	
	/**
	 * Check whether a group on the given board is beaten (group is found by starting position row and col)
	 */
	private boolean isGroupBeaten(PlayerColor[][] board, int row, int col) {
		Referee ref = createReferee(board);
		Group group = Group.findGroup(ref, new FieldPosition(row, col));
		return group.isBeaten(ref);
	}
	
	private void assertGroupsEqual(PlayerColor[][] board, int[] startingPosition, int[][] expectedPositions) {
		Group group = Group.findGroup(createReferee(board), new FieldPosition(startingPosition[0], startingPosition[1]));
		Set<FieldPosition> expected = toFieldPositions(expectedPositions);
		
		assertEquals(expected, group.getStones());
	}
}