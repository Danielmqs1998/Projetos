package com.wstore.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Centraliza as propriedades JWT*/
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

	private String secret;
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

	public void setExpirationMs(long ms) {
		this.expirationMs = ms;
	}
}
