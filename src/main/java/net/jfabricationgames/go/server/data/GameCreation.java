package net.jfabricationgames.go.server.data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class GameCreation {
	
	@NotNull(message = "Choose an opponent player")
	@Size(min = 3, message = "Invalid username (usernames have at least 3 characters)")
	private String opponentPlayer;
	@NotNull(message = "Choose either black or white")
	@Pattern(regexp = "black|white", message = "Choose either black or white")
	private String color;
	@Min(value = 0, message = "Comi can't be lower than 0")
	@Max(value = 10, message = "Comi can't be higher than 10")
	private double comi = 5.5;
	@Min(value = 0, message = "The minimum handycap is 0")
	@Max(value = 9, message = "The maximum handycap is 9")
	private int handycap;
	@NotNull(message = "Choose a board size")
	@Min(value = 9, message = "Choose a board size")//because 0 is used when nothing else is selected
	private int boardSize;
}