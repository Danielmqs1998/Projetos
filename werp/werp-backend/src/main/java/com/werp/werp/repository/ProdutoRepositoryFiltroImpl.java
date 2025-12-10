package com.werp.werp.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.werp.werp.model.Produto;

@Repository
public class ProdutoRepositoryFiltroImpl implements ProdutoRepositoryFiltro {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<Produto> buscarFiltrado(Integer categoria, String codigoDescricao, Pageable pageable) {

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
}

