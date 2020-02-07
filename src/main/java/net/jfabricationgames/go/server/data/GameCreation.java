package net.jfabricationgames.go.server.data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class GameCreation {
	
	@NotNull
	@Pattern(regexp = "black|white", message = "Choose either black or white")
	private String color;
	@Min(value = 0, message = "Comi can't be lower than 0")
	@Max(value = 10, message = "Comi can't be higher than 10")
	private double comi;
	@Min(value = 0, message = "The minimum handycap is 0")
	@Max(value = 9, message = "The maximum handycap is 9")
	private int handycap;
}