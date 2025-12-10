package com.werp.model;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeioPagamento {

	private String id;
	private int codigo;
	private String descricao;
	private double taxaFixa;
	private double taxaPercentual;
	private String instituicao;
	private TipoPagamento tipo;
	private int maximoParcelas;
	
	public enum TipoPagamento {
		DEBITO,
		CREDITO,
		PIX,
		DINHEIRO
	}
	
	@Transient
	private String urlIcone;

	public String getUrlIcone() {

	    if (urlIcone == null) {
	        switch (tipo) {
	        
	            case CREDITO:
	                urlIcone = "https://res.cloudinary.com/dot6s1ryg/image/upload/v1764125027/cartao_credito_h2ttuq.png";
	                break;

	            case PIX:
	                urlIcone = "https://res.cloudinary.com/dot6s1ryg/image/upload/v1764127035/pix_kzxdmb.png";
	                break;

	            default:
	                urlIcone = "";
	        }
	    }

	    return urlIcone;
	}

}
