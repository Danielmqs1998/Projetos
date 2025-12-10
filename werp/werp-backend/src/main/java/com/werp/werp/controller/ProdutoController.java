package com.werp.werp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.werp.werp.model.DetalhesProduto;
import com.werp.werp.model.Produto;
import com.werp.werp.repository.DetalhesProdutoRepository;
import com.werp.werp.repository.ProdutoRepository;

@RestController
@RequestMapping(path = "/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private DetalhesProdutoRepository detalhesProdutoRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@GetMapping("/total") 
	public Integer obterTotalProdutos(){
	    return (int) produtoRepository.count();
	}
	
	@PostMapping("/cadastrar")
	public ResponseEntity<?> cadastrarProduto(@RequestBody Produto produto){
		ResponseEntity<?> response;
		try {
			Produto prod = produtoRepository.findByCodigoAndLoja(produto.getCodigo(), produto.getLoja()).orElse(null);
			DetalhesProduto detalhes = detalhesProdutoRepository.findByCodigoProdutoAndLoja(produto.getCodigo(), produto.getLoja()).orElse(null);
			if (prod != null) {
				prod.atualizar(produto);
				detalhes.atualizar(produto.getDetalhes());
			} else {
				prod = produto;
				prod.setCodigo(String.valueOf(produtoRepository.count() + 1));
			}
			produtoRepository.save(prod);
			detalhesProdutoRepository.save(prod.getDetalhes());
			response = ResponseEntity.ok(prod.getCodigo());
		} catch (Exception e) {
			e.printStackTrace();
			response = ResponseEntity.badRequest().body("Erro ao efetuar cadastro: " + e.getMessage());
		}
		return response;
	}
	
	@GetMapping(path = "/consultar")
	public ResponseEntity<?> consultarProduto(@RequestParam String codigo, @RequestParam int loja) {
		ResponseEntity<?> response = ResponseEntity.noContent().build();
		try {
			Produto produto = produtoRepository.findByCodigoAndLoja(codigo, loja).orElse(null);
			if (produto != null) {
				DetalhesProduto detalhes = detalhesProdutoRepository.findByCodigoProdutoAndLoja(codigo, loja).orElse(null);
				produto.setDetalhes(detalhes == null ? new DetalhesProduto() : detalhes);
				response = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(produto);			
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = ResponseEntity.badRequest().body("Erro ao consultar dados do produto " + codigo + ". Erro: " + e.getMessage());
		}
		return response;
	}
	
	@GetMapping("/listagem") 
	public Page<Produto> listarProdutos(
			@RequestParam int primeiro, 
			@RequestParam int tamanhoPagina, 
			@RequestParam(required = false) Integer categoria,
			@RequestParam(required = false) String codigoDescricao){
		
		int pagina = primeiro / tamanhoPagina;
		Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
				
		Criteria criteria = new Criteria();

        if (categoria != null) {
            criteria = criteria.and("categoria").is(categoria);
        }

        if (codigoDescricao != null && !codigoDescricao.isBlank()) {
            criteria = new Criteria().andOperator(
                    criteria,
                    new Criteria().orOperator(
                            Criteria.where("descricao").regex(codigoDescricao, "i"),
                            Criteria.where("codigoBarras").regex(codigoDescricao, "i")
                    )
            );
        }

        Query query = new Query(criteria).with(pageable);

        List<Produto> lista = mongoTemplate.find(query, Produto.class);

        long total = mongoTemplate.count(
                Query.of(query).limit(-1).skip(-1),
                Produto.class
        );
        
	    return new PageImpl<>(lista, pageable, total);
	    
	}
	
	@DeleteMapping(path = "/remover")
	public ResponseEntity<?> removerProduto(@RequestParam String codigo, @RequestParam int loja) {
		ResponseEntity<?> response = ResponseEntity.noContent().build();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
				produtoRepository.deleteByCodigoAndLoja(codigo, loja);
				detalhesProdutoRepository.deleteByCodigoProdutoAndLoja(codigo, loja);
				response = ResponseEntity.ok().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = ResponseEntity.badRequest().body("Erro ao excluir produto. " + e.getMessage());
		}
		return response;
	}
	
}
