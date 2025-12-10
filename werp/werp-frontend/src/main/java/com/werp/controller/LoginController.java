package com.werp.controller;

import java.io.Serializable;

import org.primefaces.PrimeFaces;

import com.werp.rest.AuthRequest;
import com.werp.rest.AuthResponse;
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
 * Controller da tela de login.
 * 
 */
@Named(value = "loginController")
@ViewScoped
@Getter
@Setter
public class LoginController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String usuario;
	private String senha;
	private final String URL = new PropertiesLoader().getBackend();

	// faz chamada ao back-end, caso o usuario estiver logado redireciona para a
	// pagina de usuario com acesso.
	@PostConstruct
	private void inicializar() {
		String token = verificarToken();
		if (token != null && !token.isEmpty()){
			redirecionar();
		}
	}

	public void login() {

		Client client = ClientBuilder.newClient();

		AuthRequest credenciais = new AuthRequest();
		credenciais.setToken(verificarToken());
		credenciais.setUsername(usuario);
		credenciais.setPassword(senha);
		
		try {
			Response response = client.target(URL + "/auth/login").request(MediaType.APPLICATION_JSON)
					.post(Entity.json(credenciais));

			AuthResponse resposta = null;
			if (response != null && response.getStatus() == 200) {
				resposta = response.readEntity(AuthResponse.class);
				if (resposta.isAutenticado()) {
					String token = response.getCookies().get("jwt").getValue();
					HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
							.getSession(true);
					session.setAttribute("token_atual", token);
					redirecionar();
				}
			} else {
				PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Usuário ou senha incorretos!"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Falha ao tentar efetuar login!"));
		} 
	}

	private void redirecionar() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(
					FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/paginas/index.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String verificarToken() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		String token = (String) session.getAttribute("token_atual");
		return token;
	}

}
