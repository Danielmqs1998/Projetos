package com.werp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
public class DetalhesProduto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
	
	private List<Especificacao> especificacoes = new ArrayList<>();

	private List<Integer> meiosPagamento = new ArrayList<>();
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Especificacao{
		private String tipo;
		private String descricao;
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Modelo{
		private String descricao;
		private String urlImagem;
	}

	
}