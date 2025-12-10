package com.werp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe modelo simplificada para vendas de produtos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Venda implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String codigoProduto;
	private BigDecimal valorTotal;
	private BigDecimal quantidade;
	private int finalizador;
	private int parcelas;
	private Date dataVenda;
	private int codigoVendedor;

}

