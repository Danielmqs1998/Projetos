package com.werp.werp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.werp.werp.model.Usuario;

public interface UserRepository extends MongoRepository<Usuario, Long> {

	Usuario findByUsername(String name);

}
