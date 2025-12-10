package com.werp.model;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe modelo para dados de produtos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Produto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String codigo;
	@Positive(message = "Favor informar a loja.")
	private int loja;
	@NotEmpty(message = "Favor informar o código de barras.")
	private String codigoBarras;
	@NotEmpty(message = "Favor informar a descrição.")
	private String descricao;
	@NotNull(message = "Favor informar a quantidade.")
	private double quantidade;
	@NotNull(message = "Favor informar o preço.")
	private double preco;
	private double precoPromocional;
	@NotEmpty(message = "Favor informar a URL da imagem.")
	private String imagem;
	@Positive(message = "Favor informar o código do fornecedor.")
	private int fornecedor;
	private int categoria;
	private DetalhesProduto detalhes;
	
}