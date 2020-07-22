/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Main classe que pergunta ao utilizador qual irá ser a main page para executar
 * o programa
 *
 * @author Diogo Venâncio - 160221076
 * @author Miguel Lapão - 170221003
 */
public class MainPage extends Application {

    public static String url;

    /**
     * Inicializa a aplicação
     *
     * @param arg0 Argument
     * @throws Exception Exceção
     */
    @Override
    public void start(Stage arg0) throws Exception {

        Stage window = new Stage();
        GridPane grid1 = new GridPane();
        grid1.setAlignment(Pos.CENTER);
        grid1.setHgap(10);
        grid1.setVgap(12);

        Label label = new Label("Insira o link da página main");
        TextField valueT = new TextField();
        Button button = new Button("OK");

        label.setMaxWidth(Double.MAX_VALUE);
        valueT.setMaxWidth(Double.MAX_VALUE);
        button.setMaxWidth(Double.MAX_VALUE);
        
        button.setOnAction(e -> {
            url = valueT.getText();

            //http://www.brunomnsilva.com/sandbox/index.html
            if (!url.isEmpty() && isValid(url)) {
                System.out.println("Url Atribuido: " + url);

                Main main = new Main();

                try {
                    main.start();
                } catch (IOException ex) {
                    Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                }

                ((Stage) button.getScene().getWindow()).close();
            } else {
                System.out.println("Introduza um link válido");
            }
        });

        grid1.add(label, 0, 0);
        grid1.add(valueT, 0, 1);
        grid1.add(button, 0, 2);

        Scene scene = new Scene(grid1, 500, 250);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        window.setScene(scene);
        window.setTitle("Definir URL");
        window.showAndWait();
        window.setX(d.width / 2 - (window.getWidth() / 2));
        window.setY(d.height / 2 - (window.getHeight() / 2));

    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Metodo para validar o url main
     *
     * @param url Url da main page
     * @return true se for valido
     */
    public static boolean isValid(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
