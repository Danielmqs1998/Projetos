package com.werp.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.primefaces.PrimeFaces;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.werp.model.MeioPagamento;
import com.werp.util.PropertiesLoader;

import jakarta.annotation.PostConstruct;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;

/**
 * Controller da tela de listagem de meios de pagamentos
 * 
 */
@Named(value = "meiosPagamentoController")
@ViewScoped
@Getter
@Setter
public class MeiosPagamentoController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<MeioPagamento> meiosPagamento;
	private final String URL = new PropertiesLoader().getBackend();
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	@ManagedProperty(value = "meiosPagamento")
	private MeioPagamento meioPagamentoRemover;

	@PostConstruct
	private void inicializar() {
		carregarMeiosPagamento(); 
	}

	private void carregarMeiosPagamento() {
			
		try {

			Client client = ClientBuilder.newBuilder()
					.connectTimeout(30000, TimeUnit.MILLISECONDS)
					.readTimeout(30000, TimeUnit.MILLISECONDS)
					.build();

			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			String tokenAtual = (String) session.getAttribute("token_atual");

			Response response = client.target(URL + "/meios_pagamento/listagem")
					.request(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + tokenAtual).get();

			if (response.getStatus() == 200) {
				meiosPagamento = Arrays.asList(response.readEntity(MeioPagamento[].class));
			} else {
				PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "Falha ao listar meios de pagamento!"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "Falha ao listar meios de pagamento!"));
		}
		

	}
	
	
}
