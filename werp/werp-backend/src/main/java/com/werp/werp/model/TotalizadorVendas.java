package com.werp.werp.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Totalizador de vendas refatorado:
 *  - classes internas declaradas como static (eliminam referencia implicita
 *    a instancia de TotalizadorVendas, necessaria para deserializacao JSON
 *    e para uso pelo MongoTemplate sem instanciar o pai primeiro)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TotalizadorVendas implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<TotalizadorVendaMes> totalizadorVendaMes;
    private List<TotalizadorVendaVendedor> totalizadorVendaVendedor;
    private List<TotalizadorVendaProduto> totalizadorVendaProduto;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TotalizadorVendaMes {
        private Integer ano;
        private Integer mes;
        private double totalRegistros;
        private double totalValores;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TotalizadorVendaVendedor {
        private Integer codigoVendedor;
        private String nomeVendedor;
        private double totalRegistros;
        private double totalValores;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TotalizadorVendaProduto {
        private String codigoProduto;
        private double totalRegistros;
        private double totalValores;
    }
}
