package com.wstore.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wstore.model.Usuario;

public interface UserRepository extends MongoRepository<Usuario, String> {

	Usuario findByUsername(String name);

}
