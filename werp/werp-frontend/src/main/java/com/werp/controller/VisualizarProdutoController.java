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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;

/**
 * Controller da tela de listagem de produtos
 * 
 */
@Named(value = "visualizarProdutoController")
@ViewScoped
@Getter
@Setter
public class VisualizarProdutoController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LazyDataModel<Produto> produtos;
	private final String URL = new PropertiesLoader().getBackend();
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private Produto produtoRemover;
	private int filtroCategoria;
	private String filtroCodigoDescricao;
	private int totalProdutos = 0;

	@PostConstruct
	private void inicializar() {
		produtos = new LazyDataModel<Produto>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<Produto> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                return carregarProdutos(first, pageSize);
			}
			
			@Override
			public int count(Map<String, FilterMeta> filterBy) {
				return produtos.getRowCount() > 0 ? totalProdutos : obterTotalProdutos();
			}
		};
	}

	private List<Produto> carregarProdutos(int primeiro, int tamanhoPagina) {
			
		List<Produto> produtos = new ArrayList<>();
		
		String filtros = "";
		
		if (filtroCategoria > 0) {
			filtros += "&categoria=" + filtroCategoria;
		}
		
		if (filtroCodigoDescricao != null && !filtroCodigoDescricao.isEmpty()) {
			filtros += "&codigoDescricao=" + filtroCodigoDescricao;
		}
		
		try {

			Client client = ClientBuilder.newBuilder()
					.connectTimeout(30000, TimeUnit.MILLISECONDS)
					.readTimeout(30000, TimeUnit.MILLISECONDS)
					.build();

			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			String tokenAtual = (String) session.getAttribute("token_atual");

			Response response = client.target(URL + "/produtos/listagem?primeiro=" + primeiro + "&tamanhoPagina=" + tamanhoPagina + filtros)
					.request(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + tokenAtual).get();

			if (response.getStatus() == 200) {
				String json = response.readEntity(String.class);
				JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
				JsonArray contentArray = jsonObject.getAsJsonArray("content");
				totalProdutos = jsonObject.get("numberOfElements").getAsInt();

				produtos = new Gson().fromJson(contentArray, new TypeToken<List<Produto>>(){}.getType());
			} else {
				PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "Falha ao listar produtos!"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "Falha ao listar produtos!"));
		}
		
		return produtos;
	}
	
	private Integer obterTotalProdutos() {
		
		int totalProdutos = 0;
		
		try {

			Client client = ClientBuilder.newBuilder().connectTimeout(30000, TimeUnit.MILLISECONDS).readTimeout(30000, TimeUnit.MILLISECONDS).build();

			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			String tokenAtual = (String) session.getAttribute("token_atual");

			Response response = client.target(URL + "/produtos/total").request(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + tokenAtual).get();

			if (response.getStatus() == 200) {
				totalProdutos = response.readEntity(Integer.class);
			} else {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "Falha ao obter total de produtos!");
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return totalProdutos;
	}

	public void redirecionar(String codigoProduto, int loja) {
		try {
			FacesContext.getCurrentInstance().getExternalContext()
			.redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() 
					+ "/paginas/cadastro_produtos.xhtml?codigo=" + codigoProduto + "&loja=" + loja);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removerProduto() {
		try {
						
			Client client = ClientBuilder.newBuilder()
					.connectTimeout(30000, TimeUnit.MILLISECONDS)
					.readTimeout(30000, TimeUnit.MILLISECONDS)
					.build();
			
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			String tokenAtual = (String) session.getAttribute("token_atual");
			
			Response response = client.target(URL + "/produtos/remover")
					.queryParam("codigo", produtoRemover.getCodigo())
					.queryParam("loja", produtoRemover.getLoja())
					.request(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + tokenAtual)
					.delete();
			
			if (response.getStatus() == 200) {
				PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Produto removido com sucesso."));
			} else {
				PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Falha ao remover produto."));
			}

		} catch (Exception e) {
			e.printStackTrace();
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Falha ao remover produto."));
		}
		
		this.produtoRemover = null;
	}
	
}
