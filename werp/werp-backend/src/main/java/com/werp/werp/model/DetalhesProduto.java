package com.werp.werp.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe modelo para os detalhes dos produtos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "detalhes_produto")
public class DetalhesProduto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
	private String codigoProduto;
	private int loja;
	private String urlImagemApresentacao1;
	private String urlImagemApresentacao2;
	private String urlImagemApresentacao3;
	private String urlImagemApresentacao4;
	private String urlImagemApresentacao5;
	private String textoApresentacao;
	private String marca;
	private String fabricante;
	private double peso;
	private double altura;
	private double largura;
	private double comprimento;
	private String material;
	private String garantia;
	private String validade;
	private int codigoFornecedor;
	
	private List<Modelo> modelos;

	private List<String> itensEmbalagem;
	
	private List<Especificacao> especificacoes;

	private List<Integer> meiosPagamento;
	
	@Getter
	@Setter
	@NoArgsConstructor
	public static class Especificacao{
		private String tipo;
		private String descricao;
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	public static class Modelo{
		private String descricao;
		private String urlImagem;
	}
	
	public void atualizar (DetalhesProduto detalhes) {
		this.codigoProduto = detalhes.getCodigoProduto();
		this.loja = detalhes.getLoja();
		this.urlImagemApresentacao1 = detalhes.getUrlImagemApresentacao1();
		this.urlImagemApresentacao2 = detalhes.getUrlImagemApresentacao2();
		this.urlImagemApresentacao3 = detalhes.getUrlImagemApresentacao3();
		this.urlImagemApresentacao4 = detalhes.getUrlImagemApresentacao4();
		this.urlImagemApresentacao5 = detalhes.getUrlImagemApresentacao5();
		this.textoApresentacao = detalhes.getTextoApresentacao();
		this.marca = detalhes.getMarca();
		this.fabricante = detalhes.getFabricante();
		this.peso = detalhes.getPeso();
		this.altura = detalhes.getAltura();
		this.largura = detalhes.getLargura();
		this.comprimento = detalhes.getComprimento();
		this.material = detalhes.getMaterial();
		this.garantia = detalhes.getGarantia();
		this.validade = detalhes.getValidade();
		this.codigoFornecedor = detalhes.getCodigoFornecedor();
		this.modelos = detalhes.getModelos();
		this.itensEmbalagem = detalhes.getItensEmbalagem();
		this.especificacoes = detalhes.getEspecificacoes();
		this.meiosPagamento = detalhes.getMeiosPagamento();
	}
	
}