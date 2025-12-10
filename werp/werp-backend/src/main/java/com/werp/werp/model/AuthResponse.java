package com.werp.werp.model;

import java.io.Serializable;


public class AuthResponse implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean autenticado;
    private String token;
    
	public boolean isAutenticado() {
		return autenticado;
	}
	public void setAutenticado(boolean autenticado) {
		this.autenticado = autenticado;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	@Override
	public String toString() {
		return "AuthResponse [autenticado=" + autenticado + ", token=" + token + "]";
	}   
}
