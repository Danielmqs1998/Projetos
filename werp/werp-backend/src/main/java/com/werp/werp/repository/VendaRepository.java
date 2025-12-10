package com.werp.werp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.werp.werp.model.Venda;

public interface VendaRepository extends MongoRepository<Venda, String> {

	
}
