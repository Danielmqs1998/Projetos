package com.werp.werp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.werp.werp.model.TotalizadorVendas;
import com.werp.werp.repository.VendaRepository;

@RestController
@RequestMapping(path = "/vendas")
public class VendaController {

	@Autowired
	private VendaRepository vendaRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@GetMapping(path = "/graficos")
	public ResponseEntity<?> consultarGraficos() {
		
		ResponseEntity<?> response = ResponseEntity.noContent().build();
		
		try {

			Aggregation agregacaoMes = Aggregation.newAggregation(
					Aggregation.project().andExpression("{ $year: '$dataVenda' }").as("ano")
							.andExpression("{ $month: '$dataVenda' }").as("mes").andExpression("valorTotal")
							.as("valorTotal"),
					Aggregation.group("ano", "mes").count().as("totalRegistros").sum("valorTotal").as("totalValores"),
					Aggregation.project("totalRegistros", "totalValores").and("_id.ano").as("ano").and("_id.mes")
							.as("mes"),
					Aggregation.sort(Sort.by(Sort.Direction.ASC, "ano", "mes")));

			Aggregation agregacaoVendedor = Aggregation.newAggregation(
					Aggregation.project().andExpression("valorTotal").as("valorTotal").and("codigoVendedor")
							.as("codigoVendedor"),
					Aggregation.group("codigoVendedor").count().as("totalRegistros").sum("valorTotal")
							.as("totalValores"),
					Aggregation.project("totalRegistros", "totalValores").and("_id").as("codigoVendedor"),
					Aggregation.sort(Sort.by(Sort.Direction.ASC, "codigoVendedor")));


			AggregationResults<TotalizadorVendas.TotalizadorVendaMes> resultAgregacaoMes = mongoTemplate
					.aggregate(agregacaoMes, "vendas", TotalizadorVendas.TotalizadorVendaMes.class);

			AggregationResults<TotalizadorVendas.TotalizadorVendaVendedor> resultAgregacaoVend = mongoTemplate
					.aggregate(agregacaoVendedor, "vendas", TotalizadorVendas.TotalizadorVendaVendedor.class);

			TotalizadorVendas totalizadorVendas = new TotalizadorVendas();
			totalizadorVendas.setTotalizadorVendaMes(resultAgregacaoMes.getMappedResults());
			totalizadorVendas.setTotalizadorVendaVendedor(resultAgregacaoVend.getMappedResults());
			totalizadorVendas.setTotalizadorVendaProduto(agregarTotalVendasProduto());

			response = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(totalizadorVendas);

		} catch (Exception e) {
			e.printStackTrace();
			response = ResponseEntity.badRequest().body("Erro ao consultar dados das vendas." + e.getMessage());
		}
		return response;
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
	
	private AggregationResults<TotalizadorVendas.TotalizadorVendaMes> agregarTotalVendasMes() {
		
		Aggregation agregacaoMes = Aggregation.newAggregation(
				Aggregation.project().andExpression("{ $year: '$dataVenda' }").as("ano")
						.andExpression("{ $month: '$dataVenda' }").as("mes").andExpression("valorTotal")
						.as("valorTotal"),
				Aggregation.group("ano", "mes").count().as("totalRegistros").sum("valorTotal").as("totalValores"),
				Aggregation.project("totalRegistros", "totalValores").and("_id.ano").as("ano").and("_id.mes")
						.as("mes"),
				Aggregation.sort(Sort.by(Sort.Direction.ASC, "ano", "mes")));

		return mongoTemplate.aggregate(agregacaoMes, "vendas", TotalizadorVendas.TotalizadorVendaMes.class);
	}

}
