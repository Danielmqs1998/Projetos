package com.wstore.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wstore.model.Produto;

public interface ProdutoRepository extends MongoRepository<Produto, String> {

}
