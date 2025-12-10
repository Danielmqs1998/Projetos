package com.werp.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe modelo para dados resumidos de produtos (listagem)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoResumo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String codigo;
	private String codigoBarras;
	private String descricao;
	private double quantidade;
	private int fornecedor;
	private int categoria;
	
}