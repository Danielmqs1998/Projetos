package com.wstore.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "usuarios_loja")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "username")
@ToString
public class Usuario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String username;
	private String password;
	private String role;
	private Date dataCadastro;
	private Date dataUltimoAcesso;
	private DadosPessoais dadosPessoais;
	private DadosBancarios dadosBancarios;

	@Getter
	@Setter
	private class DadosPessoais {

		private String nomeCompleto;
		private String contato;
		private String email;
		private Date dataNascimento;

	}

	@Getter
	@Setter
	private class DadosBancarios {

		private String nomeInstituicao;
		private String numeroCartao;

	}

}
