package com.werp.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.stream.Stream;

public class PropertiesLoader {

	private static HashMap<String, String> properties = new HashMap<>();

	public PropertiesLoader() {
		carregarPropertiesServidor();
	}

	private enum ServerProperty {
		BACKEND_URL,
		BACKEND_PORTA;
	}

	private void carregarPropertiesServidor() {
		try (InputStream is = getClass().getResourceAsStream("/server.properties");
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				Stream<String> lines = br.lines();) {
			lines.forEach(l -> {
				String[] keyValue = l.split("=");
				properties.put(keyValue[0], keyValue[1]);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getServerProperty(ServerProperty serverProperty) {
		switch (serverProperty) {
			case BACKEND_URL: return properties.get("backend.url"); 
			case BACKEND_PORTA: return properties.get("backend.porta"); 
			default: return "";
		}
	}
	
	public String getBackend() {
		return getServerProperty(ServerProperty.BACKEND_URL) + ":" + getServerProperty(ServerProperty.BACKEND_PORTA);
	}
}
