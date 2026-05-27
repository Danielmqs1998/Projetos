package wpos.model;

import java.math.BigDecimal;

/**
 * Catálogo fictício de produtos para demonstração do PDV.
 * Em produção, viria de uma API ou banco de dados local.
 */
public class Produto {

    private final String codigo;
    private final String descricao;
    private final BigDecimal preco;

    public Produto(String codigo, String descricao, BigDecimal preco) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.preco = preco;
    }

    public String getCodigo()    { return codigo; }
    public String getDescricao() { return descricao; }
    public BigDecimal getPreco() { return preco; }
}
