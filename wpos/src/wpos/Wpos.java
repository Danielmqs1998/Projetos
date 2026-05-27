package wpos;

import javafx.application.Application;
import javafx.stage.Stage;
import wpos.view.PdvView;

/**
 * Ponto de entrada da aplicação WPOS — PDV JavaFX.
 */
public class Wpos extends Application {

    @Override
    public void start(Stage stage) {
        PdvView view = new PdvView();
        stage.setScene(view.build(stage));
        stage.setTitle("WPOS — Ponto de Venda");
        stage.setMinWidth(720);
        stage.setMinHeight(480);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
