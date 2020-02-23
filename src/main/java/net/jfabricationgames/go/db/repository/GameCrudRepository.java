package net.jfabricationgames.go.db.repository;

import org.springframework.data.repository.CrudRepository;

import net.jfabricationgames.go.game.Game;

public interface GameCrudRepository extends CrudRepository<Game, Integer> {
	
}