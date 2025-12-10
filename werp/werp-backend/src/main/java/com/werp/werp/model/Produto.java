package com.werp.werp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "produtos")
public class Produto {

	@Id
	private String id;
	private String codigo;
	private int loja;
	private String codigoBarras;
	private String descricao;
	private double quantidade;
	private double preco;
	private double precoPromocional;
	private String imagem;
	private int fornecedor;
	private int categoria;
	
	@Transient
	private DetalhesProduto detalhes;
	
	public void atualizar(Produto produto) {
		this.codigo = produto.getCodigo();
		this.loja = produto.getLoja();
		this.codigoBarras = produto.getCodigoBarras();
		this.descricao = produto.getDescricao();
		this.quantidade = produto.getQuantidade();
		this.preco = produto.getPreco();
		this.precoPromocional = produto.getPrecoPromocional();
		this.imagem = produto.getImagem();
		this.fornecedor = produto.getFornecedor();
		this.categoria = produto.getCategoria();
		this.detalhes = produto.getDetalhes();
	}

}
