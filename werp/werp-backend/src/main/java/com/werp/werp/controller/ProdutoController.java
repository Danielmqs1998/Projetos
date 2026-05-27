package com.werp.werp.controller;

import java.util.List;
import java.util.UUID;

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
import org.springframework.security.access.prepost.PreAuthorize;
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

/**
 * Controller de produtos refatorado:
 *  - codigo gerado com UUID (evita colisao por count)
 *  - autorizacao via @PreAuthorize (elimina verificacao manual de role)
 *  - CORS centralizado no SecurityConfig (anotacao removida)
 */
@RestController
@RequestMapping(path = "/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private DetalhesProdutoRepository detalhesProdutoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarProduto(@RequestBody Produto produto) {
        try {
            Produto prod = produtoRepository.findByCodigoAndLoja(produto.getCodigo(), produto.getLoja()).orElse(null);
            DetalhesProduto detalhes = detalhesProdutoRepository
                    .findByCodigoProdutoAndLoja(produto.getCodigo(), produto.getLoja()).orElse(null);

            if (prod != null) {
                prod.atualizar(produto);
                if (detalhes != null) {
                    detalhes.atualizar(produto.getDetalhes());
                } else {
                    detalhes = produto.getDetalhes();
                }
            } else {
                prod = produto;
                // UUID garante unicidade sem depender de count()
                prod.setCodigo(UUID.randomUUID().toString());
                detalhes = produto.getDetalhes();
            }

            produtoRepository.save(prod);
            if (detalhes != null) {
                detalhesProdutoRepository.save(detalhes);
            }
            return ResponseEntity.ok(prod.getCodigo());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao efetuar cadastro: " + e.getMessage());
        }
    }

    @GetMapping(path = "/consultar")
    public ResponseEntity<?> consultarProduto(@RequestParam String codigo, @RequestParam int loja) {
        try {
            Produto produto = produtoRepository.findByCodigoAndLoja(codigo, loja).orElse(null);
            if (produto == null) {
                return ResponseEntity.noContent().build();
            }
            DetalhesProduto detalhes = detalhesProdutoRepository
                    .findByCodigoProdutoAndLoja(codigo, loja).orElse(new DetalhesProduto());
            produto.setDetalhes(detalhes);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(produto);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("Erro ao consultar produto " + codigo + ": " + e.getMessage());
        }
    }

    @GetMapping("/listagem")
    public Page<Produto> listarProdutos(
            @RequestParam int primeiro,
            @RequestParam int tamanhoPagina,
            @RequestParam(required = false) Integer categoria,
            @RequestParam(required = false) String codigoDescricao) {

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
                            Criteria.where("codigoBarras").regex(codigoDescricao, "i")));
        }

        Query query = new Query(criteria).with(pageable);
        List<Produto> lista = mongoTemplate.find(query, Produto.class);
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Produto.class);

        return new PageImpl<>(lista, pageable, total);
    }

    /** Apenas ROLE_ADMIN pode remover produtos – verificacao declarativa via anotacao. */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/remover")
    public ResponseEntity<?> removerProduto(@RequestParam String codigo, @RequestParam int loja) {
        try {
            produtoRepository.deleteByCodigoAndLoja(codigo, loja);
            detalhesProdutoRepository.deleteByCodigoProdutoAndLoja(codigo, loja);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao excluir produto: " + e.getMessage());
        }
    }
}
