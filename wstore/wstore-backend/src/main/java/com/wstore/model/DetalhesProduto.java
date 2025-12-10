package com.wstore.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "detalhes_produto")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode (of = "codigoProduto")
@ToString
public class DetalhesProduto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private String codigoProduto;
	private String urlImagem1;
	private String urlImagem2;
	private String urlImagem3;
	private String textoDescritivo;
	private AvaliacoesProduto avaliacoes;
	
	@Getter
	@Setter
	private class AvaliacoesProduto {
		private String nomeUsuario;
		private String comentario;
		private String data;
		private int classificacao;
	}
	
}
