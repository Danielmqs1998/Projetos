package com.werp.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import org.primefaces.PrimeFaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.werp.model.Cliente;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;

/**
 * Controller da tela de cadastro de clientes
 * 
 */
@Named(value = "cadastroClienteController")
@ViewScoped
@Getter
@Setter
public class CadastroClienteController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Cliente cliente;
	private final String URL = new PropertiesLoader().getBackend();
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private Client client;

	@PostConstruct
	private void inicializar() {
		
		client = ClientBuilder.newBuilder()
				.connectTimeout(30000, TimeUnit.MILLISECONDS)
				.readTimeout(30000, TimeUnit.MILLISECONDS)
				.build();
		
		String codigoCliente = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap()
                .get("codigo");
		
		if (codigoCliente == null) {
			cliente = new Cliente();
		} else {
			consultarCliente(codigoCliente);
		}
	}
	
	public void cadastrar() {
		try {
						
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			String tokenAtual = (String) session.getAttribute("token_atual");
			
			cliente.setDataCadastro(LocalDate.now());
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			String jsonCliente = mapper.writeValueAsString(cliente);
			
			Response response = client.target(URL + "/clientes/cadastro").request(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + tokenAtual)
					.post(Entity.json(jsonCliente));
			
			FacesMessage message;
			if (response.getStatus() == 200) {
				String mensagem = cliente.getCodigo() == 0 ? "Cadastro efetuado com sucesso!" : "Atualização efetuada com sucesso!";
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", mensagem);
				cliente.setCodigo(response.readEntity(Long.class));
			} else {
				String mensagem = cliente.getCodigo() == 0 ? "Falha ao cadastrar cliente!" : "Falha ao atualizar dados do cliente!";
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", mensagem);
			}
			
	        PrimeFaces.current().dialog().showMessageDynamic(message);

		} catch (Exception e) {
	        PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "Falha interna!"));
			e.printStackTrace();
		}
	}
	
	private void consultarCliente(String codigo) {
		try {
						
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			String tokenAtual = (String) session.getAttribute("token_atual");
			
			Response response = client.target(URL + "/clientes/consultar")
					.queryParam("codigo", codigo)
					.request(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + tokenAtual)
					.get();
			
			if (response.getStatus() == 200) {
				cliente = gson.fromJson(response.readEntity(String.class), Cliente.class);
			} else {
				PrimeFaces.current().executeScript("PF('dialogErroConsultaCliente').show();");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
