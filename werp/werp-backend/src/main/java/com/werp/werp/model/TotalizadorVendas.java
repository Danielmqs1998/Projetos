package com.werp.werp.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TotalizadorVendas implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<TotalizadorVendaMes> totalizadorVendaMes;
	private List<TotalizadorVendaVendedor> totalizadorVendaVendedor;
	private List<TotalizadorVendaProduto> totalizadorVendaProduto;

	@Getter
	@Setter
	@AllArgsConstructor
	public class TotalizadorVendaMes {

		private Integer ano;
		private Integer mes;
		private double totalRegistros;
		private double totalValores;

	}

	@Getter
	@Setter
	@AllArgsConstructor
	public class TotalizadorVendaVendedor {

		private Integer codigoVendedor;
		private String nomeVendedor;
		private double totalRegistros;
		private double totalValores;

	}

	@Getter
	@Setter
	@AllArgsConstructor
	public class TotalizadorVendaProduto {
		
		private String codigoProduto;
		private double totalRegistros;
		private double totalValores;
		
	}

}
