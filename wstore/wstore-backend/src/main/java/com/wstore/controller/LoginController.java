package com.wstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wstore.model.AuthRequest;
import com.wstore.model.AuthResponse;
import com.wstore.services.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Refatorado:
 *  - sem chave JWT hardcoded (usa JwtService)
 *  - role real gravada no token
 *  - CORS centralizado no SecurityConfig
 */
@RestController
@RequestMapping(path = "/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {

        Authentication authentication = null;

        if (authRequest.getUsername() != null) {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } else {
            authentication = SecurityContextHolder.getContext().getAuthentication();
        }

        boolean autenticado = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);

        if (!autenticado) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nao autenticado");
        }

        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst().orElse("ROLE_USER");

        String token = jwtService.gerarToken(authentication.getName(), role);

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (jwtService.getExpiracaoMs() / 1000));
        response.addCookie(cookie);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAutenticado(true);
        return ResponseEntity.ok(authResponse);
    }
}
