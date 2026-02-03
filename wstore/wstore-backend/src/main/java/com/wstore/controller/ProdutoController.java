package com.wstore.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wstore.model.Produto;

@RestController
@RequestMapping(path = "/produtos")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class ProdutoController {

	@Autowired
	private MongoTemplate mongoTemplate;

	@GetMapping("/listar")
	public Page<Produto> listarProdutos(
			@PageableDefault(size=16, sort="codigo") Pageable pageable,
			@RequestParam(required = false) BigDecimal precoMaximo,
			@RequestParam(required = false) BigDecimal precoMinimo,
			@RequestParam(required = false) String descricao,
			@RequestParam(required = false) String estado,
			@RequestParam(required = false) String condicoes,
			@RequestParam(required = false) Integer categoria){
				
		Criteria criteria = new Criteria();

        if (descricao != null && !descricao.isBlank()) {
            criteria = new Criteria().andOperator(
                    criteria,
                    new Criteria().orOperator(
                            Criteria.where("descricao").regex(descricao, "i")
                    )
            );
        }
        
        if (precoMaximo != null && precoMaximo.compareTo(BigDecimal.ZERO) > 0) {
            criteria = criteria.and("preco").lte(precoMaximo.doubleValue());
        }
        
        if (precoMinimo != null && precoMinimo.compareTo(BigDecimal.ZERO) > 0) {
            criteria = criteria.and("preco").gte(precoMinimo.doubleValue());
        }
        
        if (estado != null) {
            criteria = criteria.and("estado").is(estado);
        }
        
        if (condicoes != null && !condicoes.isEmpty()) {
            criteria = criteria.and("condicoes").all(Arrays.asList(condicoes.split(";")));
        }

        if (categoria != null && categoria > 0) {
            criteria = criteria.and("categoria").is(categoria);
        }
        
        Query query = new Query(criteria).with(pageable);

        List<Produto> lista = mongoTemplate.find(query, Produto.class);

        long total = mongoTemplate.count(
                Query.of(query).limit(-1).skip(-1),
                Produto.class
        );
        
	    return new PageImpl<>(lista, pageable, total);
	}

}
