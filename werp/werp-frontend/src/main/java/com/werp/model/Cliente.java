package com.werp.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe modelo para dados de clientes.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private long codigo;
	@NotEmpty(message = "Favor informar o nome fantasia.")
	private String nomeFantasia;
	@NotEmpty(message = "Favor informar o CNPJ.")
	@Size(min = 14, message = "Favor informar os 14 dígitos.")
	private String cnpj;
	private String ramo;
	@NotEmpty(message = "Favor informar telefone para contato.")
	@Size(min = 10, message = "Favor informar os 10 dígitos.")
	private String contato;
	private String email;
	private Date dataCadastro;
	private boolean ativo;
	private Endereco endereco = new Endereco();
	private Contrato contrato = new Contrato();

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Endereco{
		@NotEmpty(message = "Favor informar o país.")
		private String pais;
		@NotEmpty(message = "Favor informar o estado.")
		private String estado;
		@NotEmpty(message = "Favor informar a cidade.")
		private String cidade;
		@NotEmpty(message = "Favor informar o bairro.")
		private String bairro;
		@NotEmpty(message = "Favor informar o endereço.")
		private String endereco;
		@NotEmpty(message = "Favor informar o CEP.")
		@Size(min = 8, message = "Favor informar os 8 dígitos.")
		private String cep;
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Contrato{
		private String categoria;
		@NotNull(message = "Favor informar a data de vencimento do contrato.")
		private Date dataValidade;
	}
}

