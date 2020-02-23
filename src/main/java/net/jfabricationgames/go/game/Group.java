package net.jfabricationgames.go.game;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class Group {
	
	private Set<FieldPosition> stones;
	private PlayerColor color;
	
	public Group(Set<FieldPosition> stones, PlayerColor color) {
		this.stones = stones;
		this.color = color;
	}
	public Group(PlayerColor color) {
		this.color = color;
		this.stones = new HashSet<FieldPosition>();
	}
	
	public static Group findGroup(Referee ref, FieldPosition startingPosition) {
		PlayerColor groupColor = ref.getStoneColor(startingPosition);
		final int boardSize = ref.getBoardSize();
		if (groupColor == null) {
			throw new IllegalArgumentException("The field of the starting position is empty (position was: " + startingPosition + ")");
		}
		
		//complete the group by adding stones using a flood fill
		Set<FieldPosition> allStones = new HashSet<FieldPosition>();
		
		//initialize the current queue and the next step queue
		Set<FieldPosition> currentStones = new HashSet<FieldPosition>();
		Set<FieldPosition> nextStones = new HashSet<FieldPosition>();
		
		//add the starting position
		currentStones.add(startingPosition);
		
		//add the fields with the iterative flood fill till the queues are empty 
		while (!currentStones.isEmpty()) {
			for (FieldPosition currentField : currentStones) {
				for (FieldPosition near : currentField.getFieldsNear(boardSize)) {
					if (near.exists(boardSize) && //the field exists on the board
							ref.getStoneColor(near) != null && //prevent NPE 
							ref.getStoneColor(near).equals(groupColor) && //the field contains a stone of the group color
							!allStones.contains(near) && !currentStones.contains(near)) {//the field is not already added
						nextStones.add(near);
					}
				}
			}
			//add the current stones to the group, copy the next steps to the queue and clear next steps
			allStones.addAll(currentStones);
			currentStones = nextStones;
			nextStones = new HashSet<FieldPosition>();
		}
		
		return new Group(allStones, groupColor);
	}
	
	public boolean isBeaten(Referee ref) {
		final int boardSize = ref.getBoardSize();
		Set<FieldPosition> nearFreeFields = stones.stream().flatMap(s -> s.getFieldsNear(boardSize).stream()) //all fields near
				.filter(field -> field.exists(boardSize) && ref.isFieldEmpty(field)) //existing and empty
				.collect(Collectors.toSet());
		
		return nearFreeFields.isEmpty();
	}
	
	public void addStone(FieldPosition pos) {
		stones.add(pos);
	}
	public void addStones(List<FieldPosition> positions) {
		stones.addAll(positions);
	}
	public void remove(FieldPosition pos) {
		stones.remove(pos);
	}
	public void clear() {
		stones.clear();
	}
}