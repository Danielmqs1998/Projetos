package wpos.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wpos.model.ItemVenda;
import wpos.model.Produto;

/**
 * Controlador de negócio do PDV.
 * Mantém o estado da venda corrente e expõe operações para a View.
 * Sem dependência de JavaFX (testável isoladamente).
 */
public class PdvController {

    // ── Catálogo demo ─────────────────────────────────────────────────
    private static final Map<String, Produto> CATALOGO = new HashMap<>();
    static {
        CATALOGO.put("001", new Produto("001", "Café Expresso 500g",    new BigDecimal("18.90")));
        CATALOGO.put("002", new Produto("002", "Açúcar Cristal 1kg",    new BigDecimal("4.75")));
        CATALOGO.put("003", new Produto("003", "Leite Integral 1L",     new BigDecimal("6.49")));
        CATALOGO.put("004", new Produto("004", "Pão Forma 500g",        new BigDecimal("9.20")));
        CATALOGO.put("005", new Produto("005", "Manteiga 200g",         new BigDecimal("11.30")));
        CATALOGO.put("006", new Produto("006", "Suco de Laranja 1L",    new BigDecimal("8.00")));
        CATALOGO.put("007", new Produto("007", "Biscoito Recheado 140g",new BigDecimal("3.50")));
        CATALOGO.put("008", new Produto("008", "Arroz Branco 5kg",      new BigDecimal("28.99")));
        CATALOGO.put("009", new Produto("009", "Feijão Preto 1kg",      new BigDecimal("9.89")));
        CATALOGO.put("010", new Produto("010", "Macarrão Espaguete 500g",new BigDecimal("5.15")));
    }

    // ── Estado da venda ───────────────────────────────────────────────
    private final ObservableList<ItemVenda> itens = FXCollections.observableArrayList();
    private BigDecimal desconto = BigDecimal.ZERO;

    // ── Consulta ──────────────────────────────────────────────────────

    public Optional<Produto> buscarProduto(String codigo) {
        return Optional.ofNullable(CATALOGO.get(codigo.trim()));
    }

    // ── Operações da venda ────────────────────────────────────────────

    /**
     * Adiciona ou incrementa um item na venda.
     * @return o item adicionado/atualizado, ou empty se código não encontrado.
     */
    public Optional<ItemVenda> adicionarItem(String codigo, int quantidade) {
        Optional<Produto> prod = buscarProduto(codigo);
        if (prod.isEmpty()) return Optional.empty();

        Produto p = prod.get();
        Optional<ItemVenda> existente = itens.stream()
                .filter(i -> i.getCodigo().equals(p.getCodigo()))
                .findFirst();

        if (existente.isPresent()) {
            ItemVenda item = existente.get();
            item.setQuantidade(item.getQuantidade() + quantidade);
            // Notifica a ObservableList manualmente para atualizar a TableView
            int idx = itens.indexOf(item);
            itens.set(idx, item);
            return Optional.of(item);
        } else {
            ItemVenda novo = new ItemVenda(p.getCodigo(), p.getDescricao(), quantidade, p.getPreco());
            itens.add(novo);
            return Optional.of(novo);
        }
    }

    /** Remove um item da venda pelo código do produto. */
    public void removerItem(ItemVenda item) {
        itens.remove(item);
    }

    /** Cancela toda a venda corrente. */
    public void cancelarVenda() {
        itens.clear();
        desconto = BigDecimal.ZERO;
    }

    // ── Cálculos ─────────────────────────────────────────────────────

    public BigDecimal getSubtotal() {
        return itens.stream()
                .map(ItemVenda::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getDesconto() { return desconto; }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto == null ? BigDecimal.ZERO : desconto;
    }

    public BigDecimal getTotal() {
        return getSubtotal().subtract(desconto).max(BigDecimal.ZERO);
    }

    /**
     * Calcula o troco dado o valor pago pelo cliente.
     * Retorna negativo se o valor for insuficiente.
     */
    public BigDecimal calcularTroco(BigDecimal valorPago) {
        return valorPago.subtract(getTotal()).setScale(2, RoundingMode.HALF_UP);
    }

    public ObservableList<ItemVenda> getItens() { return itens; }

    public boolean isVendaVazia() { return itens.isEmpty(); }
}
