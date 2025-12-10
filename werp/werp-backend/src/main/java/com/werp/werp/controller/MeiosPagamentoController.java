package com.werp.werp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.werp.werp.model.MeioPagamento;
import com.werp.werp.repository.MeioPagamentoRepository;

@RestController
@RequestMapping(path = "/meios_pagamento")
@CrossOrigin(origins = "*")
public class MeiosPagamentoController {
	
	@Autowired
	private MeioPagamentoRepository meioPagamentoRepository;
	
	
	@GetMapping("/listagem") 
	public ResponseEntity<?> listarMeiosPagamento(){
		ResponseEntity<?> response;
		try {
			List<MeioPagamento> meiosPagamento = meioPagamentoRepository.findAll();
			if (meiosPagamento == null) {
				response = ResponseEntity.internalServerError().build();
			}
			
			response = ResponseEntity.ok(meiosPagamento);
		} catch (Exception e) {
			e.printStackTrace();
			response = ResponseEntity.badRequest().body("Erro ao consultar meios de pagamento: " + e.getMessage());
		}
		return response;
	}
	
}
