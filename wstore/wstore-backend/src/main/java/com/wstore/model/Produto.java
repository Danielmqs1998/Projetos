package com.wstore.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * 
 * 
 */
@Document(collection = "produtos")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"codigo", "codigoBarras"})
@ToString
public class Produto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String codigo;
	private String codigoBarras;
	private String descricao;
	private double quantidade;
	private double preco;
	private double precoPromocional;
	private boolean usado;
	private String imagem;
	private int fornecedor;
	private int categoria;
	private int totalAvaliacoes;
	private int classificacao;
	
}
