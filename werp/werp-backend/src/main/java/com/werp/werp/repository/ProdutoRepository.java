package com.werp.werp.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.werp.werp.model.Produto;

public interface ProdutoRepository extends MongoRepository<Produto, String> {

	Optional<Produto> findByCodigoAndLoja(String codigo, int loja);
	
	void deleteByCodigoAndLoja(String codigo, int loja);
	
	
}
