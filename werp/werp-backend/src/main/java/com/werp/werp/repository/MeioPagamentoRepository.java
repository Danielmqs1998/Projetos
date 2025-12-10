package com.werp.werp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.werp.werp.model.MeioPagamento;
import com.werp.werp.model.Produto;

public interface MeioPagamentoRepository extends MongoRepository<MeioPagamento, Integer> {

	Optional<Produto> findByCodigo(Integer codigo);
	
	List<MeioPagamento> findByCodigoIn(List<Integer> codigos);
	
}
