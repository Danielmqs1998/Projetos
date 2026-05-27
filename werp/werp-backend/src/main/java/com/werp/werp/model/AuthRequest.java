package com.werp.werp.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/** AuthRequest refatorado: Lombok elimina getters/setters/toString manuais. */
@Data
@NoArgsConstructor
public class AuthRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
}
