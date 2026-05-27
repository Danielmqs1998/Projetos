package com.werp.werp.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade de usuario refatorada:
 *  - @Id adicionado (ausente no original)
 *  - @Indexed(unique=true) em username para garantir unicidade no MongoDB
 *  - Lombok @Data elimina getters/setters manuais
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "usuarios_erp")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    private String password;

    /** Ex: ROLE_ADMIN ou ROLE_USER */
    private String role;
}
