package com.wstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wstore.model.Produto;
import com.wstore.repository.ProdutoRepository;

@RestController
@RequestMapping(path = "/produtos")
@CrossOrigin(
    origins = "http://localhost:4200", 
    allowCredentials = "true"
)
public class ProdutoController {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@GetMapping("/listar")
	public Page<Produto> listarProdutos(@PageableDefault(size=16, sort="codigo") Pageable pageable){
		return produtoRepository.findAll(pageable);
	}
	
}
