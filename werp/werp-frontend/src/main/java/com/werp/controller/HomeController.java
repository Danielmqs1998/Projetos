package com.werp.controller;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.werp.model.Estatisticas;
import com.werp.model.TotalizadorVendas;
import com.werp.model.TotalizadorVendas.TotalizadorVendaProduto;
import com.werp.model.TotalizadorVendas.TotalizadorVendaVendedor;
import com.werp.util.PropertiesLoader;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;
import software.xdev.chartjs.model.charts.BarChart;
import software.xdev.chartjs.model.charts.LineChart;
import software.xdev.chartjs.model.charts.PieChart;
import software.xdev.chartjs.model.data.BarData;
import software.xdev.chartjs.model.data.LineData;
import software.xdev.chartjs.model.data.PieData;
import software.xdev.chartjs.model.dataset.BarDataset;
import software.xdev.chartjs.model.dataset.LineDataset;
import software.xdev.chartjs.model.dataset.PieDataset;
import software.xdev.chartjs.model.options.BarOptions;
import software.xdev.chartjs.model.options.LineOptions;
import software.xdev.chartjs.model.options.PieOptions;
import software.xdev.chartjs.model.options.Plugins;
import software.xdev.chartjs.model.options.Title;
import software.xdev.chartjs.model.options.elements.Fill;
import software.xdev.chartjs.model.options.scale.Scales;
import software.xdev.chartjs.model.options.scale.cartesian.linear.LinearScaleOptions;

@Named(value = "homeController")
@ViewScoped
@Getter
@Setter
public class HomeController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String URL = new PropertiesLoader().getBackend();
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private Client client;
	private String graficoVendasVendedor;
	private String graficoVendasProduto;
	private String graficoVendasMes;
	private Estatisticas estatisticas;

	@PostConstruct
	public void inicializar() {
		
		client = ClientBuilder.newBuilder()
				.connectTimeout(30000, TimeUnit.MILLISECONDS)
				.readTimeout(30000, TimeUnit.MILLISECONDS)
				.build();

		carregarVendas();
		carregarEstatisticas();
	}

	public void logout() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		String tokenAtual = (String) session.getAttribute("token_atual");
		Response response = client.target(URL + "/auth/logout").request()
				.header("Authorization", "Bearer " + tokenAtual).post(null);
		if (response.getStatus() == 200) {
			try {
				session.setAttribute("token_atual", null);
				FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void carregarVendas() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		String tokenAtual = (String) session.getAttribute("token_atual");
		Response response = client.target(URL + "/vendas/graficos").request()
				.header("Authorization", "Bearer " + tokenAtual).get();
		if (response.getStatus() == 200) {
			try {
				TotalizadorVendas vendas = gson.fromJson(response.readEntity(String.class), TotalizadorVendas.class);
				criarGraficoVendasMes(vendas.getTotalizadorVendaMes());
				criarGraficoVendasProduto(vendas.getTotalizadorVendaProduto());
				criarGraficoVendasVendedor(vendas.getTotalizadorVendaVendedor());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void carregarEstatisticas() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		String tokenAtual = (String) session.getAttribute("token_atual");
		Response response = client.target(URL + "/vendas/estatisticas").request()
				.header("Authorization", "Bearer " + tokenAtual).get();
		if (response.getStatus() == 200) {
			try {
				estatisticas = gson.fromJson(response.readEntity(String.class), Estatisticas.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void criarGraficoVendasVendedor(List<TotalizadorVendas.TotalizadorVendaVendedor> totalizadorVendedor) {
		
		Double[] totaisVendas = totalizadorVendedor.stream().map(TotalizadorVendaVendedor::getTotalValores).collect(Collectors.toList()).toArray(new Double[]{});
		
		String[] vendedores = totalizadorVendedor.stream().map(t -> t.getNomeVendedor()).collect(Collectors.toList()).toArray(new String[]{});
		
		String[] cores = IntStream.range(0, totaisVendas.length)
		        .mapToObj(i -> String.format(
		                "rgba(%d, %d, %d, 0.6)",
		                (int) (Math.random() * 255),
		                (int) (Math.random() * 255),
		                (int) (Math.random() * 255)
		        ))
		        .toArray(String[]::new);
		
		graficoVendasVendedor = new PieChart()
        .setData(new PieData()
                .addDataset(new PieDataset()
                    .setData(totaisVendas)
                    .setLabel("Total (R$)")
                    .setBackgroundColor(cores)
                    .setBorderColor("rgba(255, 255, 255, 1)")
                    .setBorderWidth(1))
	                .setLabels(vendedores))
	        		.setOptions(new PieOptions()
	        				.setResponsive(true)
	        				.setMaintainAspectRatio(false)
	        				.setPlugins(new Plugins()
	        						.setTitle(new Title()
	        								.setDisplay(true)			                    
	        								.setText("Vendas por vendedor"))))
	        		.toJson();
    }

	private void criarGraficoVendasProduto(List<TotalizadorVendas.TotalizadorVendaProduto> totalizadorProduto) {
		
		Double[] totaisVendas = totalizadorProduto.stream().map(TotalizadorVendaProduto::getTotalValores).collect(Collectors.toList()).toArray(new Double[]{});
		
		String[] produtos = totalizadorProduto.stream().map(TotalizadorVendaProduto::getCodigoProduto).collect(Collectors.toList()).toArray(new String[]{});
		
        graficoVendasProduto = new BarChart()
        .setData(new BarData()
                .addDataset(new BarDataset()
                    .setData(totaisVendas)
                    .setLabel("Total (R$)")
                    .setBackgroundColor("rgba(2, 136, 209, 0.5)")
                    .setBorderColor("rgba(255, 255, 255, 1)")
                    .setBorderWidth(1))
	                .setLabels(produtos))
	        		.setOptions(new BarOptions()
	        				.setResponsive(true)
	        				.setMaintainAspectRatio(false)
	        				.setPlugins(new Plugins()
	        						.setTitle(new Title()
	        								.setDisplay(true)
	        								.setText("Vendas por produto"))))
	        		.toJson();
    }
	
	private void criarGraficoVendasMes(List<TotalizadorVendas.TotalizadorVendaMes> totalizadorMes) {

		Double[] totaisVendas = totalizadorMes.stream().map(
				TotalizadorVendas.TotalizadorVendaMes::getTotalValores).collect(Collectors.toList()).toArray(new Double[]{});
		
		String[] meses = totalizadorMes.stream().map(t -> tratarMes(t.getMes())).collect(Collectors.toList()).toArray(new String[]{});
		
		graficoVendasMes = new LineChart()
				.setData(new LineData().addDataset(new LineDataset()
						.setData(totaisVendas)
	                    .setLabel("Total (R$)")
						.setBorderColor("rgba(2, 136, 209, 1)")
						.setLineTension(0.1f).setFill(new Fill<Boolean>(false)))
						.setLabels(meses))
						.setOptions(new LineOptions()
								.setScales(new Scales().addScale("y", new LinearScaleOptions().setBeginAtZero(true)))
								.setResponsive(true)
								.setMaintainAspectRatio(false)
								.setPlugins(new Plugins()
										.setTitle(new Title()
												.setDisplay(true)
												.setText("Vendas por mês"))))
						.toJson();
	}

	private String tratarMes(int mes) {
		switch (mes) {
		case 1:
			return "Janeiro";
		case 2:
			return "Fevereiro";
		case 3:
			return "Março";
		case 4:
			return "Abril";
		case 5:
			return "Maio";
		case 6:
			return "Junho";
		case 7:
			return "Julho";
		case 8:
			return "Agosto";
		case 9:
			return "Setembro";
		case 10:
			return "Outubro";
		case 11:
			return "Novembro";
		case 12:
			return "Dezembro";
		default:
			return "";
		}
	}

}
