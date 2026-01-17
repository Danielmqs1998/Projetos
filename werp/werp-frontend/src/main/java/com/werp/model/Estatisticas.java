package com.werp.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Estatisticas implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double totalVendasAno;
	private double totalClientes;
	private double totalProdutos;

}
