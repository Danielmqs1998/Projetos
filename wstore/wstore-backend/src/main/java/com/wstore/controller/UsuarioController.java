package com.wstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wstore.model.Usuario;
import com.wstore.repository.UserRepository;

@RestController
@RequestMapping(path = "/usuarios")
@CrossOrigin(
    origins = "http://localhost:4200", 
    allowCredentials = "true"
)
public class UsuarioController {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/dados_usuario")
	public ResponseEntity<?> consultarDadosUsuario(){
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			Usuario usuario = userRepository.findByUsername(username);
			return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); 
		}
	}
	
}
