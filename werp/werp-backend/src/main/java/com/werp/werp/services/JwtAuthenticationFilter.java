package com.werp.werp.services;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String secretKey = "wErP_ae1212039120391204810241231239120391203s";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        // Obter o token do cabeçalho Authorization
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.replace("Bearer ", "");               
        } else if (token == null) {
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("jwt".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }
        
        if (token != null) {
        
	        try {
	            // Validar e decodificar o token
	            Claims claims = Jwts.parserBuilder()
	                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
	                    .build()
	                    .parseClaimsJws(token)
	                    .getBody();
	
	            String username = claims.getSubject();
	            List<SimpleGrantedAuthority> authorities = Collections.singletonList(
	                    new SimpleGrantedAuthority("ROLE_ADMIN") // Substitua conforme necessário
	            );
	
	            // Configurar o contexto de segurança
	            UsernamePasswordAuthenticationToken authentication =
	                    new UsernamePasswordAuthenticationToken(username, null, authorities);
	            SecurityContextHolder.getContext().setAuthentication(authentication);
	
	        } catch (Exception e) {
	            response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED);
	            response.getWriter().write("Token inválido ou expirado");
	            return;
	        }
        }
	
        // Continuar com a requisição
        filterChain.doFilter(request, response);
    }
}
