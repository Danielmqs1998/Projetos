package com.werp.werp.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Centraliza as propriedades JWT definidas em application.properties.
 * Nenhuma classe deve declarar a chave secreta diretamente no codigo.
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /** Chave HMAC-SHA256 - minimo 32 caracteres. Injetada via env JWT_SECRET. */
    private String secret;

    /** Validade do token em milissegundos. Padrao: 1 hora. */
    private long expirationMs = 3_600_000L;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    public void setExpirationMs(long expirationMs) {
        this.expirationMs = expirationMs;
    }
}
