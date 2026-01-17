package com.werp.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.primefaces.PrimeFaces;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.werp.model.DetalhesProduto;
import com.werp.model.MeioPagamento;
import com.werp.model.Produto;
import com.werp.util.PropertiesLoader;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;

/**
 * Controller da tela de cadastro de produtos
 * 
 */
@Named(value = "cadastroProdutoController")
@ViewScoped
@Getter
@Setter
public class CadastroProdutoController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String URL = new PropertiesLoader().getBackend();
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private Client client;
	
	private Produto produto;
	
	private boolean carregouImagem;
	
	private String tipoEspecificacao;
	private String descricaoEspecificacao;
	
	private String descricaoItemEmbalagem;
	
	private String descricaoModelo;
	private String urlImagemModelo;
	
	private int meioPagamentoSelecionado;
	private List<MeioPagamento> meiosPagamento = new ArrayList<>();
	private List<MeioPagamento> meiosPagamentoSelecionados = new ArrayList<>();

	@PostConstruct
	private void inicializar() {
		
		client = ClientBuilder.newBuilder()
				.connectTimeout(30000, TimeUnit.MILLISECONDS)
				.readTimeout(30000, TimeUnit.MILLISECONDS)
				.build();

		String codigoProduto = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codigo");
		String loja = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("loja");

		if (codigoProduto == null || loja == null) {
			produto = new Produto();
			produto.setDetalhes(new DetalhesProduto());
		} else {
			consultarProduto(codigoProduto, Integer.parseInt(loja));
		}
		
		consultarMeiosPagamento();
		
	}

	public void cadastrar() {
		try {

			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			String tokenAtual = (String) session.getAttribute("token_atual");

			String jsonProduto = gson.toJson(produto);

			Response response = client.target(URL + "/produtos/cadastrar").request(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + tokenAtual).post(Entity.json(jsonProduto));

			FacesMessage message;
			if (response.getStatus() == 200) {
				String mensagem = produto.getCodigo() == null ? "Cadastro efetuado com sucesso!" : "Atualização efetuada com sucesso!";
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", mensagem);
				produto.setCodigo(response.readEntity(String.class));
			} else {
				String mensagem = produto.getCodigo() == null ? "Falha ao cadastrar produto!" : "Falha ao atualizar dados do produto!";
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", mensagem);
			}

			PrimeFaces.current().dialog().showMessageDynamic(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void consultarProduto(String codigo, int loja) { // 
		try {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			String tokenAtual = (String) session.getAttribute("token_atual");

			Response response = client.target(URL + "/produtos/consultar")
					.queryParam("codigo", codigo)
					.queryParam("loja", loja)
					.request(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + tokenAtual).get();

			if (response.getStatus() == 200) {
				produto = gson.fromJson(response.readEntity(String.class), Produto.class);
				carregarImagem();
			} else {
				PrimeFaces.current().executeScript("PF('dialogErroConsultaProduto').show();");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void consultarMeiosPagamento() { // 
		try {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			String tokenAtual = (String) session.getAttribute("token_atual");

			Response response = client.target(URL + "/meios_pagamento/listagem")
					.request(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + tokenAtual).get();

			if (response.getStatus() == 200) {
				meiosPagamento = Arrays.asList(gson.fromJson(response.readEntity(String.class), MeioPagamento[].class));
				if (meiosPagamento != null && produto.getDetalhes() != null && produto.getDetalhes().getMeiosPagamento() != null) {
					meiosPagamentoSelecionados = meiosPagamento.stream().filter(p -> produto.getDetalhes().getMeiosPagamento().contains(p.getCodigo())).toList();
				}
			} else {
				PrimeFaces.current().executeScript("PF('dialogErroConsultaProduto').show();");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void abrirImagem(String urlImagem) {
		
		if (urlImagem.isBlank()) {
			return;
		}
		
		try {
		    PrimeFaces.current().executeScript("window.open('" + urlImagem + "', '_blank');");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void carregarImagem() {
		carregouImagem = false;
		if (produto != null) {
			if (produto.getImagem().isEmpty()) {
				PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "Informe a URL da imagem do produto!"));
				return;
			}
			try {            
	            WebTarget target = client.target(produto.getImagem());
	            Response resp = target.request().get();
	            carregouImagem = resp.getStatus() == 200;
			} catch (Exception e) {
				PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "Falha ao carregar imagem do produto!"));
			}
		}
	}
	
	public void adicionarEspecificacao() {
		
		if (tipoEspecificacao.isBlank()) {
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "Informe o tipo da especificação!"));
			return;
		}
		
		if (descricaoEspecificacao.isBlank()) {
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "Informe a descrição da especificação!"));
			return;
		}
		
		produto.getDetalhes().getEspecificacoes().add(new DetalhesProduto.Especificacao(tipoEspecificacao, descricaoEspecificacao));
		tipoEspecificacao = "";
		descricaoEspecificacao = "";
	}
	
	public void adicionarModelo() {
		
		if (descricaoModelo.isBlank()) {
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "Informe a descrição do modelo!"));
			return;
		}
		
		if (urlImagemModelo.isBlank()) {
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "Informe a URL da imagem do modelo!"));
			return;
		}
		
		produto.getDetalhes().getModelos().add(new DetalhesProduto.Modelo(descricaoModelo, urlImagemModelo));
		descricaoModelo = "";
		urlImagemModelo = "";
	}
	
	public void adicionarItemEmbalagem() {
		
		if (descricaoItemEmbalagem.isEmpty()) {
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "Favor informar a descrição do item!"));
			return;	
		}
		
		if (produto.getDetalhes().getItensEmbalagem().contains(descricaoItemEmbalagem)) {
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "Item já adicionado!"));
			return;	
		}
		
		produto.getDetalhes().getItensEmbalagem().add(descricaoItemEmbalagem);
		descricaoEspecificacao = "";
	}
	
	public void adicionarMeioPagamento(int codigo) {
		MeioPagamento pagto = meiosPagamento.stream().filter(m -> m.getCodigo() == codigo).findFirst().get();
		if (!meiosPagamentoSelecionados.contains(pagto)) {
			meiosPagamentoSelecionados.add(pagto);
		} else {
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "Meio de pagamento já adicionado!"));
		}
	}

}
