package com.werp.werp.controller;

import java.time.LocalDate;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.werp.werp.model.Estatisticas;
import com.werp.werp.model.TotalizadorVendas;
import com.werp.werp.repository.ClienteRepository;
import com.werp.werp.repository.ProdutoRepository;

@RestController
@RequestMapping(path = "/vendas")
public class VendaController {
	
	@Autowired
	private ClienteRepository clientesRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@GetMapping(path = "/graficos")
	public ResponseEntity<?> consultarGraficos() {
		
		ResponseEntity<?> response = ResponseEntity.noContent().build();
		
		try {

			TotalizadorVendas totalizadorVendas = new TotalizadorVendas();
			totalizadorVendas.setTotalizadorVendaMes(agregarTotalVendasMes());
			totalizadorVendas.setTotalizadorVendaVendedor(agregarTotalVendasVendedor());
			totalizadorVendas.setTotalizadorVendaProduto(agregarTotalVendasProduto());

			response = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(totalizadorVendas);

		} catch (Exception e) {
			e.printStackTrace();
			response = ResponseEntity.badRequest().body("Erro ao consultar dados das vendas." + e.getMessage());
		}
		return response;
	}
	
	
	@GetMapping(path = "/estatisticas")
	public ResponseEntity<?> consultarEstatisticas() {
		
		ResponseEntity<?> response = ResponseEntity.noContent().build();
		
		try {
			
			Estatisticas estatisticas = new Estatisticas();
			estatisticas.setTotalProdutos(produtoRepository.count());
			estatisticas.setTotalClientes(clientesRepository.count());
			estatisticas.setTotalVendasAno(consultarTotalVendasAno());
			
			response = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(estatisticas);

		} catch (Exception e) {
			e.printStackTrace();
			response = ResponseEntity.badRequest().body("Erro ao consultar dados de estatísticas." + e.getMessage());
		}
		return response;
	}
	
	private double consultarTotalVendasAno() {
		
		Aggregation agg = Aggregation.newAggregation(
		    Aggregation.match(
		        Criteria.where("dataVenda")
		            .gte(LocalDate.of(LocalDate.now().getYear(), 1, 1))
		            .lt(LocalDate.of(LocalDate.now().plusYears(1).getYear(), 1, 1))
		    ),
		    Aggregation.group().sum("valorTotal").as("totalVendas")
		);

		Document resultado = mongoTemplate.aggregate(agg, "vendas", Document.class).getUniqueMappedResult();
		
		return resultado != null ? resultado.getDouble("totalVendas").doubleValue() : 0d;
	}
		
	private List<TotalizadorVendas.TotalizadorVendaVendedor> agregarTotalVendasVendedor(){
		
		Aggregation agregacaoVendedor = Aggregation.newAggregation(
				Aggregation.project()
				    .andExpression("valorTotal").as("valorTotal")
					.and("codigoVendedor").as("codigoVendedor"),
				 Aggregation.group("codigoVendedor").count().as("totalRegistros").sum("valorTotal").as("totalValores"), // fazer group antes do lookup
				 Aggregation.lookup(
						        "vendedor",   // collection
						        "_id",        // campo do group (codigoVendedor)
						        "codigo",     // campo na collection vendedores
						        "vendedor"
						    ),
				 Aggregation.unwind("vendedor"),
				 Aggregation.project()
					.and("_id").as("codigoVendedor")
					.and("vendedor.nome").as("nomeVendedor")
					.and("totalRegistros").as("totalRegistros")
					.and("totalValores").as("totalValores"),
				 Aggregation.sort(Sort.by(Sort.Direction.ASC, "codigoVendedor")));


		return mongoTemplate.aggregate(agregacaoVendedor, "vendas", TotalizadorVendas.TotalizadorVendaVendedor.class).getMappedResults();
	}
	
	private List<TotalizadorVendas.TotalizadorVendaProduto> agregarTotalVendasProduto() {

		Aggregation agregacaoProduto = Aggregation.newAggregation(
				Aggregation.project().and("valorTotal").as("valorTotal").and("codigoProduto").as("codigoProduto"),
				Aggregation.group("codigoProduto").count().as("totalRegistros").sum("valorTotal")
						.as("totalValores"),
				Aggregation.project("totalRegistros", "totalValores").and("_id").as("codigoProduto"),
				Aggregation.sort(Sort.by(Sort.Direction.ASC, "codigoProduto")));

		return mongoTemplate.aggregate(agregacaoProduto, "vendas", TotalizadorVendas.TotalizadorVendaProduto.class).getMappedResults();
	}
	
	private List<TotalizadorVendas.TotalizadorVendaMes> agregarTotalVendasMes() {
		
		Aggregation agregacaoMes = Aggregation.newAggregation(
				Aggregation.project().andExpression("{ $year: '$dataVenda' }").as("ano")
						.andExpression("{ $month: '$dataVenda' }").as("mes").andExpression("valorTotal")
						.as("valorTotal"),
				Aggregation.group("ano", "mes").count().as("totalRegistros").sum("valorTotal").as("totalValores"),
				Aggregation.project("totalRegistros", "totalValores").and("_id.ano").as("ano").and("_id.mes")
						.as("mes"),
				Aggregation.sort(Sort.by(Sort.Direction.ASC, "ano", "mes")));

		return mongoTemplate.aggregate(agregacaoMes, "vendas", TotalizadorVendas.TotalizadorVendaMes.class).getMappedResults();
	}

}
