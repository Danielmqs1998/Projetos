package wpos.view;

import java.math.BigDecimal;
import java.util.Optional;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import wpos.controller.PdvController;
import wpos.model.ItemVenda;
import wpos.util.Formatador;

/**
 * Tela principal do PDV — operação de venda simples.
 *
 * Layout:
 *   ┌─────────────────── HEADER ───────────────────┐
 *   │  WPOS · Ponto de Venda                        │
 *   ├──── CAMPO DE CÓDIGO ─────────────────────────┤
 *   │  [Código ___________] [Qtd __] [Adicionar]    │
 *   ├──── TABELA DE ITENS ─────────────────────────┤
 *   │  Código | Descrição | Qtd | Unit | Subtotal   │
 *   │  ...                                          │
 *   ├──── TOTAIS ───────────────────────────────────┤
 *   │  Subtotal / Desconto / TOTAL                  │
 *   ├──── AÇÕES ────────────────────────────────────┤
 *   │  [Remover Item]  [Cancelar]  [Finalizar]       │
 *   └───────────────────────────────────────────────┘
 */
public class PdvView {

    private static final String VERDE_ESCURO = "#1b5e20";
    private static final String VERDE       = "#2e7d32";
    private static final String VERDE_CLARO = "#a5d6a7";
    private static final String CINZA_FUNDO = "#f5f5f5";
    private static final String TEXTO_CLARO = "#ffffff";

    private final PdvController ctrl = new PdvController();

    // Campos de entrada
    private TextField campoCodigo;
    private TextField campoQtd;

    // Tabela
    private TableView<ItemVenda> tabela;

    // Totais
    private Label lblSubtotal;
    private Label lblDesconto;
    private Label lblTotal;

    public Scene build(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + CINZA_FUNDO + ";");

        root.setTop(buildHeader());
        root.setCenter(buildCentro());
        root.setBottom(buildRodape());

        Scene scene = new Scene(root, 820, 580);
        atualizarTotais();
        return scene;
    }

    // ── Header ───────────────────────────────────────────────────────

    private HBox buildHeader() {
        Label titulo = new Label("WPOS  ·  Ponto de Venda");
        titulo.setStyle("-fx-text-fill: " + TEXTO_CLARO + "; -fx-font-size: 20px; -fx-font-weight: bold;");

        HBox header = new HBox(titulo);
        header.setPadding(new Insets(14, 24, 14, 24));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: " + VERDE_ESCURO + ";");
        return header;
    }

    // ── Centro (entrada + tabela) ─────────────────────────────────────

    private VBox buildCentro() {
        VBox centro = new VBox(12, buildEntrada(), buildTabela());
        centro.setPadding(new Insets(16, 20, 8, 20));
        VBox.setVgrow(buildTabela(), Priority.ALWAYS);
        return centro;
    }

    private HBox buildEntrada() {
        Label lblCodigo = rotulo("Código do produto:");
        campoCodigo = new TextField();
        campoCodigo.setPromptText("Ex: 001");
        campoCodigo.setPrefWidth(130);

        Label lblQtd = rotulo("Qtd:");
        campoQtd = new TextField("1");
        campoQtd.setPrefWidth(55);

        Button btnAdd = botaoPrimario("+ Adicionar", VERDE);

        btnAdd.setOnAction(e -> adicionarItem());
        campoCodigo.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.ENTER) adicionarItem(); });
        campoQtd.setOnKeyPressed(e    -> { if (e.getCode() == KeyCode.ENTER) adicionarItem(); });

        HBox linha = new HBox(10, lblCodigo, campoCodigo, lblQtd, campoQtd, btnAdd);
        linha.setAlignment(Pos.CENTER_LEFT);
        linha.setPadding(new Insets(6, 0, 6, 0));
        return linha;
    }

    @SuppressWarnings("unchecked")
    private TableView<ItemVenda> buildTabela() {
        tabela = new TableView<>(ctrl.getItens());
        tabela.setPlaceholder(new Label("Nenhum item na venda — informe o código do produto acima."));
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabela.setPrefHeight(280);

        TableColumn<ItemVenda, String> colCod = coluna("Código", 80);
        colCod.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));

        TableColumn<ItemVenda, String> colDesc = coluna("Descrição", 260);
        colDesc.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescricao()));

        TableColumn<ItemVenda, String> colQtd = coluna("Qtd", 60);
        colQtd.setCellValueFactory(c -> new SimpleStringProperty(
                String.valueOf(c.getValue().getQuantidade())));
        colQtd.setStyle("-fx-alignment: CENTER;");

        TableColumn<ItemVenda, String> colUnit = coluna("Unitário", 100);
        colUnit.setCellValueFactory(c -> new SimpleStringProperty(
                Formatador.moeda(c.getValue().getPrecoUnitario())));
        colUnit.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<ItemVenda, String> colSub = coluna("Subtotal", 110);
        colSub.setCellValueFactory(c -> new SimpleStringProperty(
                Formatador.moeda(c.getValue().getSubtotal())));
        colSub.setStyle("-fx-alignment: CENTER-RIGHT; -fx-font-weight: bold;");

        tabela.getColumns().addAll(colCod, colDesc, colQtd, colUnit, colSub);

        // Recalcular totais sempre que a lista mudar
        ctrl.getItens().addListener((ListChangeListener<ItemVenda>) change -> atualizarTotais());

        VBox.setVgrow(tabela, Priority.ALWAYS);
        return tabela;
    }

    // ── Rodapé (totais + ações) ───────────────────────────────────────

    private HBox buildRodape() {
        HBox rodape = new HBox(30, buildTotais(), buildAcoes());
        rodape.setAlignment(Pos.CENTER);
        rodape.setPadding(new Insets(10, 20, 16, 20));
        rodape.setStyle("-fx-background-color: #eeeeee; -fx-border-color: #cccccc; -fx-border-width: 1 0 0 0;");
        return rodape;
    }

    private GridPane buildTotais() {
        lblSubtotal = rotulo("R$ 0,00");
        lblDesconto = rotulo("R$ 0,00");
        lblDesconto.setStyle("-fx-text-fill: #c62828;");
        lblTotal    = new Label("R$ 0,00");
        lblTotal.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + VERDE_ESCURO + ";");

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(4);
        grid.add(rotulo("Subtotal:"),  0, 0); grid.add(lblSubtotal, 1, 0);
        grid.add(rotulo("Desconto:"),  0, 1); grid.add(lblDesconto, 1, 1);
        grid.add(rotulo("TOTAL:"),     0, 2); grid.add(lblTotal,    1, 2);
        return grid;
    }

    private VBox buildAcoes() {
        Button btnRemover   = botaoPrimario("Remover item",  "#e53935");
        Button btnDesconto  = botaoPrimario("Desconto",      "#f57c00");
        Button btnCancelar  = botaoPrimario("Cancelar venda","#757575");
        Button btnFinalizar = botaoPrimario("✔  Finalizar",  VERDE);

        btnRemover.setOnAction(e -> removerItemSelecionado());
        btnDesconto.setOnAction(e -> aplicarDesconto());
        btnCancelar.setOnAction(e -> cancelarVenda());
        btnFinalizar.setOnAction(e -> finalizarVenda());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox linha = new HBox(8, btnRemover, btnDesconto, spacer, btnCancelar, btnFinalizar);
        linha.setAlignment(Pos.CENTER_LEFT);

        VBox box = new VBox(linha);
        box.setAlignment(Pos.CENTER);
        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    // ── Ações ─────────────────────────────────────────────────────────

    private void adicionarItem() {
        String codigo = campoCodigo.getText().trim();
        if (codigo.isEmpty()) { campoCodigo.requestFocus(); return; }

        int qtd;
        try {
            qtd = Integer.parseInt(campoQtd.getText().trim());
            if (qtd <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            alerta("Quantidade inválida", "Informe um número inteiro positivo no campo Qtd.", AlertType.WARNING);
            campoQtd.requestFocus();
            return;
        }

        Optional<ItemVenda> resultado = ctrl.adicionarItem(codigo, qtd);
        if (resultado.isEmpty()) {
            alerta("Produto não encontrado",
                   "Nenhum produto com código \"" + codigo + "\" foi localizado.",
                   AlertType.WARNING);
        }

        campoCodigo.clear();
        campoQtd.setText("1");
        campoCodigo.requestFocus();
    }

    private void removerItemSelecionado() {
        ItemVenda selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            alerta("Nenhum item selecionado", "Clique em um item da lista para selecioná-lo.", AlertType.INFORMATION);
            return;
        }
        ctrl.removerItem(selecionado);
        atualizarTotais();
    }

    private void aplicarDesconto() {
        TextInputDialog dlg = new TextInputDialog(ctrl.getDesconto().toPlainString());
        dlg.setTitle("Desconto");
        dlg.setHeaderText("Informe o valor de desconto (R$):");
        dlg.setContentText("Desconto:");

        dlg.showAndWait().ifPresent(valor -> {
            try {
                BigDecimal desc = new BigDecimal(valor.replace(",", "."));
                if (desc.compareTo(ctrl.getSubtotal()) > 0) {
                    alerta("Desconto inválido", "O desconto não pode ser maior que o subtotal.", AlertType.WARNING);
                    return;
                }
                ctrl.setDesconto(desc);
                atualizarTotais();
            } catch (NumberFormatException ex) {
                alerta("Valor inválido", "Informe um número válido (ex: 5.00).", AlertType.WARNING);
            }
        });
    }

    private void cancelarVenda() {
        if (ctrl.isVendaVazia()) return;
        Alert conf = new Alert(AlertType.CONFIRMATION,
                "Deseja cancelar todos os itens da venda?", ButtonType.YES, ButtonType.NO);
        conf.setTitle("Cancelar venda");
        conf.setHeaderText(null);
        conf.showAndWait().filter(r -> r == ButtonType.YES).ifPresent(r -> {
            ctrl.cancelarVenda();
            atualizarTotais();
        });
    }

    private void finalizarVenda() {
        if (ctrl.isVendaVazia()) {
            alerta("Venda vazia", "Adicione pelo menos um item antes de finalizar.", AlertType.WARNING);
            return;
        }

        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Finalizar venda");
        dlg.setHeaderText("Total a pagar: " + Formatador.moeda(ctrl.getTotal()));
        dlg.setContentText("Valor recebido (R$):");

        dlg.showAndWait().ifPresent(valorStr -> {
            try {
                BigDecimal pago = new BigDecimal(valorStr.replace(",", "."));
                BigDecimal troco = ctrl.calcularTroco(pago);

                if (troco.compareTo(BigDecimal.ZERO) < 0) {
                    alerta("Valor insuficiente",
                           "O valor pago (" + Formatador.moeda(pago) + ") é menor que o total.",
                           AlertType.WARNING);
                    return;
                }

                Alert ok = new Alert(AlertType.INFORMATION);
                ok.setTitle("Venda finalizada");
                ok.setHeaderText("✔  Venda concluída com sucesso!");
                ok.setContentText(
                    "Total:           " + Formatador.moeda(ctrl.getTotal()) + "\n" +
                    "Valor pago:   " + Formatador.moeda(pago) + "\n" +
                    "Troco:           " + Formatador.moeda(troco));
                ok.showAndWait();

                ctrl.cancelarVenda();
                atualizarTotais();
                campoCodigo.requestFocus();

            } catch (NumberFormatException ex) {
                alerta("Valor inválido", "Informe um número válido (ex: 50.00).", AlertType.WARNING);
            }
        });
    }

    // ── Helpers ───────────────────────────────────────────────────────

    private void atualizarTotais() {
        lblSubtotal.setText(Formatador.moeda(ctrl.getSubtotal()));
        lblDesconto.setText(Formatador.moeda(ctrl.getDesconto()));
        lblTotal.setText(Formatador.moeda(ctrl.getTotal()));
    }

    private static Label rotulo(String texto) {
        Label l = new Label(texto);
        l.setStyle("-fx-font-size: 13px;");
        return l;
    }

    private static Button botaoPrimario(String texto, String cor) {
        Button b = new Button(texto);
        b.setStyle(
            "-fx-background-color: " + cor + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 7 16 7 16;" +
            "-fx-background-radius: 5;");
        b.setOnMouseEntered(e -> b.setStyle(
            "-fx-background-color: derive(" + cor + ", -15%);" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 7 16 7 16;" +
            "-fx-background-radius: 5;"));
        b.setOnMouseExited(e -> b.setStyle(
            "-fx-background-color: " + cor + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 7 16 7 16;" +
            "-fx-background-radius: 5;"));
        return b;
    }

    private static <T> TableColumn<T, String> coluna(String titulo, double largura) {
        TableColumn<T, String> col = new TableColumn<>(titulo);
        col.setPrefWidth(largura);
        return col;
    }

    private static void alerta(String titulo, String mensagem, AlertType tipo) {
        Alert a = new Alert(tipo, mensagem, ButtonType.OK);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.showAndWait();
    }
}
