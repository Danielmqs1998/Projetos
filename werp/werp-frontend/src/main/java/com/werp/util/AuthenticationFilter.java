package com.werp.util;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String loginURI = httpRequest.getContextPath() + "/login.xhtml";

        boolean loggedIn = httpRequest.getSession().getAttribute("token_atual") != null;
        
        // Verificar se é uma requisição para login ou recursos estáticos (CSS, JS, etc.)
        boolean loginRequest = httpRequest.getRequestURI().equals(loginURI);
        boolean resourceRequest = httpRequest.getRequestURI().startsWith(httpRequest.getContextPath() + "/resources/");
        
        // Se o usuário está logado ou está acessando recursos ou login, continuar o filtro
        if (loggedIn || loginRequest || resourceRequest) {
            chain.doFilter(request, response);
        } else {
            if (!loginRequest && !loggedIn) {
                httpResponse.sendRedirect(loginURI);
            } else {
                chain.doFilter(request, response);
            }
        }
    }
}
