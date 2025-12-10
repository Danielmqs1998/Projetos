package com.wstore.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wstore.model.AuthRequest;
import com.wstore.model.AuthResponse;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/auth")
@CrossOrigin(
    origins = "http://localhost:4200", 
    allowCredentials = "true"
)
public class LoginController {
	
    private final String secretKey = "wErP_ae1212039120391204810241231239120391203s";

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
		
		Authentication authentication = null;
	
		if(authRequest.getUsername() != null) {
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		} else if (SecurityContextHolder.getContext().getAuthentication() != null) {
			authentication = SecurityContextHolder.getContext().getAuthentication();
		}
		
		if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) { 
			AuthResponse authResponse = new AuthResponse();
			authResponse.setAutenticado(true); //adicionar validade do token
			
			if(authRequest.getUsername() != null) {
				String token = Jwts.builder()
		                .setSubject(authRequest.getUsername())
		                .setIssuedAt(new Date())
		                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Expira em 1h
		                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
		                .compact();
				
			    Cookie cookie = new Cookie("jwt", token);
		        cookie.setHttpOnly(true);   // Torna o cookie inacessível via JavaScript
		        cookie.setSecure(true);     // Garante que o cookie só será enviado por HTTPS
		        cookie.setPath("/");       // Define o caminho do cookie
		        cookie.setMaxAge(3600);    // Define a expiração do cookie (1 hora)
	
		        response.addCookie(cookie);
			}
			
			return ResponseEntity.ok(authResponse); 
		} else { 
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Não autenticado"); 
		}
	}
}
