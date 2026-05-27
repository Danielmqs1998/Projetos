package com.werp.werp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.werp.werp.model.MeioPagamento;
import com.werp.werp.repository.MeioPagamentoRepository;

/**
 * Controller de meios de pagamento refatorado:
 *  - CORS centralizado no SecurityConfig (anotacao removida)
 *  - findAll() nunca retorna null; verificacao de lista vazia corrigida
 */
@RestController
@RequestMapping(path = "/meios_pagamento")
public class MeiosPagamentoController {

    @Autowired
    private MeioPagamentoRepository meioPagamentoRepository;

    @GetMapping("/listagem")
    public ResponseEntity<?> listarMeiosPagamento() {
        try {
            List<MeioPagamento> meiosPagamento = meioPagamentoRepository.findAll();
            return ResponseEntity.ok(meiosPagamento);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao consultar meios de pagamento: " + e.getMessage());
        }
    }
}
