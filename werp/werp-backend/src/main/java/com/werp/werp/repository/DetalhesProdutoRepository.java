package com.werp.werp.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.werp.werp.model.DetalhesProduto;

public interface DetalhesProdutoRepository extends MongoRepository<DetalhesProduto, String> {

	Optional<DetalhesProduto> findByCodigoProdutoAndLoja(String codigoProduto, int loja);
	
	void deleteByCodigoProdutoAndLoja(String codigoProduto, int loja);
	
}
