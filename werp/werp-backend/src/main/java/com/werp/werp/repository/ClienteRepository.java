package com.werp.werp.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.werp.werp.model.Cliente;

public interface ClienteRepository extends MongoRepository<Cliente, Long> {

	Optional<Cliente> findByCodigo(long codigo);
	
	void deleteByCodigo(long codigo);
	
}
