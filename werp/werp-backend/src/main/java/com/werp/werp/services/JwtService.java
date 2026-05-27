package com.werp.werp.services;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.werp.werp.configuration.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * Centraliza geracao e validacao de tokens JWT.
 * Controllers e filtros nao manipulam a chave diretamente.
 */
@Service
public class JwtService {

    private final JwtProperties props;

    public JwtService(JwtProperties props) {
        this.props = props;
    }

    public String gerarToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + props.getExpirationMs()))
                .signWith(Keys.hmacShaKeyFor(props.getSecret().getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validarEExtrairClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(props.getSecret().getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public long getExpiracaoMs() {
        return props.getExpirationMs();
    }

    /** Extrai as authorities do claim "role" gravado no token. */
    public List<String> extrairRoles(Claims claims) {
        String role = claims.get("role", String.class);
        return role != null ? List.of(role) : List.of();
    }
}
