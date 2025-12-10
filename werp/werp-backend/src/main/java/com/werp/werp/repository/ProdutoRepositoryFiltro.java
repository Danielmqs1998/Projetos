package com.werp.werp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.werp.werp.model.Produto;

public interface ProdutoRepositoryFiltro {
	 
	Page<Produto> buscarFiltrado(Integer categoria, String codigoDescricao, Pageable pageable);
	
}
