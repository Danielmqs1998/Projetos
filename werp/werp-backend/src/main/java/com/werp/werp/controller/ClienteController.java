package com.werp.werp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.werp.werp.model.Cliente;
import com.werp.werp.repository.ClienteRepository;

@RestController
@RequestMapping(path = "/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@GetMapping("/total") 
	public Integer obterTotalProdutos(){
	    return (int) clienteRepository.count();
	}

	@PostMapping(path = "/cadastro")
	public ResponseEntity<?> cadastrarCliente(@RequestBody Cliente cliente) {
		ResponseEntity<?> response;
		try {
			Cliente cli = clienteRepository.findByCodigo(cliente.getCodigo()).orElse(null);
			if (cli != null) {
				cli.atualizar(cliente);
			} else {
				cli = cliente;
				cli.setCodigo(clienteRepository.count() + 1);
			}
			clienteRepository.save(cli);
			response = ResponseEntity.ok(cli.getCodigo());
		} catch (Exception e) {
			e.printStackTrace();
			response = ResponseEntity.badRequest().body("Erro ao efetuar cadastro: " + e.getMessage());
		}
		return response;
	}

	@GetMapping(path = "/consultar")
	public ResponseEntity<?> consultarCliente(@RequestParam long codigo) {
		ResponseEntity<?> response = ResponseEntity.noContent().build();
		try {
			Cliente cliente = clienteRepository.findByCodigo(codigo).orElse(null);
			if (cliente != null) {
				response = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(cliente);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = ResponseEntity.badRequest().body("Erro ao consultar dados do cliente " + codigo + ". Erro: " + e.getMessage());
		}
		return response;
	}

	@GetMapping(path = "/listagem")
	public Page<Cliente> listagemClientes(@RequestParam int primeiro, @RequestParam int tamanhoPagina) {
		int pagina = primeiro / tamanhoPagina;
		Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
	    return clienteRepository.findAll(pageable);
	}
	
	@DeleteMapping(path = "/remover")
	public ResponseEntity<?> removerCliente(@RequestParam long codigo) {
		ResponseEntity<?> response = ResponseEntity.noContent().build();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
				clienteRepository.deleteByCodigo(codigo);
				response = ResponseEntity.ok().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = ResponseEntity.badRequest().body("Erro ao excluir cliente. " + e.getMessage());
		}
		return response;
	}

}
