package com.wstore.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Refatorado:
 *  - @Id adicionado (estava ausente)
 *  - @Indexed(unique=true) em username para garantir unicidade no MongoDB
 *  - classes internas DadosPessoais e DadosBancarios agora sao static
 *    (evita referencia implicita a instancia pai e problema de serializacao)
 */
@Document(collection = "usuarios_loja")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "username")
@ToString
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    private String password;
    private String role;
    private Date dataCadastro;
    private Date dataUltimoAcesso;
    private DadosPessoais dadosPessoais;
    private DadosBancarios dadosBancarios;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DadosPessoais {
        private String nomeCompleto;
        private String contato;
        private String email;
        private Date dataNascimento;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DadosBancarios {
        private String nomeInstituicao;
        /** NUNCA retornar o numero completo do cartao em respostas de API. */
        private String numeroCartao;
    }
}
