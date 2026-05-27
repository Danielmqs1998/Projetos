package com.wstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wstore.model.Usuario;
import com.wstore.repository.UserRepository;

/**
 * Refatorado:
 *  - CORS removido (centralizado no SecurityConfig)
 *  - usa objeto Authentication diretamente (sem checar null manualmente)
 *  - retorna 404 se usuario nao encontrado (semantica correta)
 */
@RestController
@RequestMapping(path = "/usuarios")
public class UsuarioController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dados_usuario")
    public ResponseEntity<?> consultarDadosUsuario() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("Nao autenticado");
        }
        Usuario usuario = userRepository.findByUsername(auth.getName());
        return usuario != null
                ? ResponseEntity.ok(usuario)
                : ResponseEntity.notFound().build();
    }
}
