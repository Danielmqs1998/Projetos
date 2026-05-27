package com.werp.werp.model;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade Venda refatorada:
 *  - dataVenda migrada de java.util.Date para LocalDate (consistencia com Cliente
 *    e com as agregacoes de mes/ano que ja assumiam LocalDate no VendaController)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "vendas")
public class Venda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String codigoProduto;
    private double valorTotal;
    private double quantidade;
    private int finalizador;
    private int parcelas;
    private LocalDate dataVenda;
    private int codigoVendedor;
}
