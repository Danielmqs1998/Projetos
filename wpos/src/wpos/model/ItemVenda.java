package wpos.model;

import java.math.BigDecimal;

/**
 * Representa um item adicionado à venda atual no PDV.
 */
public class ItemVenda {

    private String codigo;
    private String descricao;
    private int quantidade;
    private BigDecimal precoUnitario;

    public ItemVenda(String codigo, String descricao, int quantidade, BigDecimal precoUnitario) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public BigDecimal getSubtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public String getCodigo()          { return codigo; }
    public String getDescricao()       { return descricao; }
    public int getQuantidade()         { return quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }

    public void setQuantidade(int q)   { this.quantidade = q; }
}
