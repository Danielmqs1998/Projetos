package com.werp.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;

/**
 * Controller da tela de listagem de clientes
 * 
 */
@Named(value = "visualizarClienteController")
@ViewScoped
@Getter
@Setter
public class VisualizarClienteController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LazyDataModel<Cliente> clientes;
	private final String URL = new PropertiesLoader().getBackend();
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private long clienteRemocao;
	private int totalClientes;

	@PostConstruct
	private void inicializar() {
		totalClientes = obterTotalClientes();
		clientes = new LazyDataModel<Cliente>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public List<Cliente> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                return carregarClientes(first, pageSize);
			}
			
			@Override
			public int count(Map<String, FilterMeta> filterBy) {
				return totalClientes;
			}
		};
	}

	private List<Cliente> carregarClientes(int primeiro, int tamanhoPagina) {
		
		List<Cliente> clientes = new ArrayList<>();

		try {

			Client client = ClientBuilder.newBuilder()
					.connectTimeout(30000, TimeUnit.MILLISECONDS)
					.readTimeout(30000, TimeUnit.MILLISECONDS)
					.build();

			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			String tokenAtual = (String) session.getAttribute("token_atual");

			Response response = client.target(URL + "/clientes/listagem?primeiro=" + primeiro + "&tamanhoPagina=" + tamanhoPagina)
					.request(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + tokenAtual).get();

			if (response.getStatus() == 200) {
				String json = response.readEntity(String.class);
				JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
				JsonArray contentArray = jsonObject.getAsJsonArray("content");
				clientes = new Gson().fromJson(contentArray, new TypeToken<List<Cliente>>(){}.getType());
			} else {
				PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "Falha ao listar clientes!"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "Falha ao listar clientes!"));
		}
		
		return clientes;
	}
	
	private Integer obterTotalClientes() {
		
		int totalClientes = 0;
		try {

			Client client = ClientBuilder.newBuilder().connectTimeout(30000, TimeUnit.MILLISECONDS).readTimeout(30000, TimeUnit.MILLISECONDS).build();

			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			String tokenAtual = (String) session.getAttribute("token_atual");

			Response response = client.target(URL + "/clientes/total").request(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + tokenAtual).get();

			if (response.getStatus() == 200) {
				totalClientes = response.readEntity(Integer.class);
			} else {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "Falha ao obter total de clientes!");
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return totalClientes;
	}

	public void redirecionar(long codigoCliente) {
		try {
			FacesContext.getCurrentInstance().getExternalContext()
			.redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
							+ "/paginas/cadastro_clientes.xhtml?codigo=" + codigoCliente);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removerCliente() {
		try {
						
			Client client = ClientBuilder.newBuilder()
					.connectTimeout(30000, TimeUnit.MILLISECONDS)
					.readTimeout(30000, TimeUnit.MILLISECONDS)
					.build();
			
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			String tokenAtual = (String) session.getAttribute("token_atual");
			
			Response response = client.target(URL + "/clientes/remover")
					.queryParam("codigo", clienteRemocao)
					.request(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + tokenAtual)
					.delete();
			
			if (response.getStatus() == 200) {
				PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Cliente removido com sucesso."));
			} else {
				PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Falha ao remover cliente."));
			}

		} catch (Exception e) {
			e.printStackTrace();
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Falha ao remover cliente."));
		}
		
		totalClientes--;
		setClienteRemocao(0);
	}
	
}
