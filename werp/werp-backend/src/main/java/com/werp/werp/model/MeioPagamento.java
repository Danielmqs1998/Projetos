package com.werp.werp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "meios_pagamento")
public class MeioPagamento {

	@Id
	private String id;
	private int codigo;
	private String descricao;
	private double taxaFixa;
	private double taxaPercentual;
	private String tipo;
	private String instituicao;
	private int maximoParcelas;

}
