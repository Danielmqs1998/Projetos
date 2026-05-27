package com.werp.werp.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.werp.werp.model.Cliente;
import com.werp.werp.repository.ClienteRepository;

/**
 * Controller de clientes refatorado:
 *  - codigo gerado com UUID (evita colisao por count)
 *  - autorizacao via @PreAuthorize
 *  - CORS centralizado no SecurityConfig
 */
@RestController
@RequestMapping(path = "/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/total")
    public long obterTotalClientes() {
        return clienteRepository.count();
    }

    @PostMapping(path = "/cadastro")
    public ResponseEntity<?> cadastrarCliente(@RequestBody Cliente cliente) {
        try {
            Cliente cli = clienteRepository.findByCodigo(cliente.getCodigo()).orElse(null);
            if (cli != null) {
                cli.atualizar(cliente);
            } else {
                cli = cliente;
                // UUID como identificador de negocio evita colisoes por count()
                cli.setCodigo(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
            }
            clienteRepository.save(cli);
            return ResponseEntity.ok(cli.getCodigo());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao efetuar cadastro: " + e.getMessage());
        }
    }

    @GetMapping(path = "/consultar")
    public ResponseEntity<?> consultarCliente(@RequestParam long codigo) {
        try {
            return clienteRepository.findByCodigo(codigo)
                    .map(c -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body((Object) c))
                    .orElse(ResponseEntity.noContent().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("Erro ao consultar cliente " + codigo + ": " + e.getMessage());
        }
    }

    @GetMapping(path = "/listagem")
    public Page<Cliente> listagemClientes(@RequestParam int primeiro, @RequestParam int tamanhoPagina) {
        int pagina = primeiro / tamanhoPagina;
        Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
        return clienteRepository.findAll(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/remover")
    public ResponseEntity<?> removerCliente(@RequestParam long codigo) {
        try {
            clienteRepository.deleteByCodigo(codigo);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao excluir cliente: " + e.getMessage());
        }
    }
}
