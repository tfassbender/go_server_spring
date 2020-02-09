package net.jfabricationgames.go.db.repository;

import org.springframework.data.repository.CrudRepository;

import net.jfabricationgames.go.server.data.User;

/**
 * Implementation is created by Spring because of the CrudRepository extension
 */
public interface UserRepository extends CrudRepository<User, Integer> {
	
	public User findByUsername(String username);
}