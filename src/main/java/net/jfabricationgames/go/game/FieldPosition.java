package net.jfabricationgames.go.game;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldPosition {
	
	private int row;
	private int col;
	
	public List<FieldPosition> getFieldsNear(int boardSize) {
		int[][] near = new int[][] {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
		List<FieldPosition> nearFields = new ArrayList<FieldPosition>();
		for (int[] nearDiff : near) {
			FieldPosition position = new FieldPosition(row + nearDiff[0], col + nearDiff[1]);
			if (position.exists(boardSize)) {
				nearFields.add(position);
			}
		}
		return nearFields;
	}
	
	public boolean exists(int boardSize) {
		return row >= 0 && col >= 0 && row < boardSize && col < boardSize;
	}
}