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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wstore.model.Produto;
import com.wstore.repository.ProdutoRepository;

@RestController
@RequestMapping(path = "/produtos")
public class ProdutoController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ProdutoRepository produtoRepository;

    /**
     * Consulta individual por codigo - usado pela pagina de detalhes.
     * Usa MongoTemplate com trim para evitar falhas por espacos no codigo.
     * Fallback para findByCodigo caso o MongoTemplate nao encontre.
     */
    @GetMapping("/consultar")
    public ResponseEntity<Produto> consultarProduto(@RequestParam String codigo) {
        String codigoNormalizado = codigo.trim();
        Query query = new Query(Criteria.where("codigo").is(codigoNormalizado));
        Produto produto = mongoTemplate.findOne(query, Produto.class);
        if (produto != null) {
            return ResponseEntity.ok(produto);
        }
        return produtoRepository.findByCodigo(codigoNormalizado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/listar")
    public Page<Produto> listarProdutos(
            @PageableDefault(size = 16, sort = "codigo") Pageable pageable,
            @RequestParam(required = false) BigDecimal precoMaximo,
            @RequestParam(required = false) BigDecimal precoMinimo,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String condicoes,
            @RequestParam(required = false) Integer categoria) {

        Criteria criteria = new Criteria();

        if (descricao != null && !descricao.isBlank()) {
            criteria.andOperator(
                new Criteria().orOperator(
                    Criteria.where("descricao").regex(descricao, "i"),
                    Criteria.where("codigoBarras").regex(descricao, "i")
                )
            );
        }

        boolean temMin = precoMinimo != null && precoMinimo.compareTo(BigDecimal.ZERO) > 0;
        boolean temMax = precoMaximo != null && precoMaximo.compareTo(BigDecimal.ZERO) > 0;
        if (temMin && temMax) {
            criteria = criteria.and("preco").gte(precoMinimo.doubleValue()).lte(precoMaximo.doubleValue());
        } else if (temMin) {
            criteria = criteria.and("preco").gte(precoMinimo.doubleValue());
        } else if (temMax) {
            criteria = criteria.and("preco").lte(precoMaximo.doubleValue());
        }

        if (estado != null && !estado.isBlank()) {
            criteria = criteria.and("estado").is(estado);
        }
        if (condicoes != null && !condicoes.isBlank()) {
            criteria = criteria.and("condicoes").all(Arrays.asList(condicoes.split(";")));
        }
        if (categoria != null && categoria > 0) {
            criteria = criteria.and("categoria").is(categoria);
        }

        Query query = new Query(criteria).with(pageable);
        List<Produto> lista = mongoTemplate.find(query, Produto.class);
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Produto.class);

        return new PageImpl<>(lista, pageable, total);
    }
}
