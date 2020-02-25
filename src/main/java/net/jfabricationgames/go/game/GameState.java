package net.jfabricationgames.go.game;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameState {
	
	private Integer id;
	private PlayerColor[][] state;
}