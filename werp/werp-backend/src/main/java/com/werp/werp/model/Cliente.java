package com.werp.werp.model;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "clientes")
public class Cliente implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private long codigo;
	private String nomeFantasia;
	private String cnpj;
	private String ramo;
	private String contato;
	private String email;
	private LocalDate dataCadastro;
	private boolean ativo;
	private Endereco endereco = new Endereco();
	private Contrato contrato = new Contrato();

	@Getter
	@Setter
	public class Endereco {
		private String pais;
		private String estado;
		private String cidade;
		private String bairro;
		private String endereco;
		private String cep;
	}

	@Getter
	@Setter
	public class Contrato {
		private String categoria;
		private LocalDate dataValidade;
	}
	
	public void atualizar(Cliente cliente) {
		this.nomeFantasia = cliente.getNomeFantasia();
		this.cnpj = cliente.getCnpj();
		this.ramo = cliente.getRamo();
		this.contato = cliente.getContato();
		this.email = cliente.getEmail();
		this.dataCadastro = cliente.getDataCadastro();
		this.ativo = cliente.isAtivo();
		this.endereco = cliente.getEndereco();
		this.contrato = cliente.getContrato();
	}
}
