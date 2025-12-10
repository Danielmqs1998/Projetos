package com.werp.werp.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe modelo simplificada para vendas de produtos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "vendas")
public class Venda implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String codigoProduto;
	private double valorTotal;
	private double quantidade;
	private int finalizador;
	private int parcelas;
	private Date dataVenda;
	private int codigoVendedor;

}

