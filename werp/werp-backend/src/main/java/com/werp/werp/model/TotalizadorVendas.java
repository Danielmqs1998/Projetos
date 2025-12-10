package com.werp.werp.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
	public class TotalizadorVendaMes {

		private Integer ano;
		private Integer mes;
		private double totalRegistros;
		private double totalValores;

	}

	@Getter
	@Setter
	public class TotalizadorVendaVendedor {

		private Integer codigoVendedor;
		private double totalRegistros;
		private double totalValores;

	}

	@Getter
	@Setter
	public class TotalizadorVendaProduto {
		
		private String codigoProduto;
		private double totalRegistros;
		private double totalValores;
		
	}

}
