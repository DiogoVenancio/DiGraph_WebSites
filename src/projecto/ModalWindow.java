/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto;

import java.awt.Dimension;
import java.awt.Toolkit;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Classe javafx que representa um modal ao qual o utilizador pode inserir o
 * número máximo de pages ou links, consoante o algoritmo selecionado
 *
 * @author Diogo Venâncio - 160221076
 * @author Miguel Lapão - 170221003
 */
public class ModalWindow extends GridPane {

    private int valor;

    public ModalWindow() {
    }

    public void Modal() {

        Stage window = new Stage();
        GridPane grid1 = new GridPane();
        grid1.setAlignment(Pos.CENTER);
        grid1.setHgap(10);
        grid1.setVgap(12);

        Label label = new Label("Insira um número máximo de paginas/links");
        TextField valueT = new TextField();
        Button button = new Button("OK");

        button.setOnAction(e -> {
            valor = Integer.parseInt(valueT.getText());
            System.out.println("Valor Atribuido: " + valor);
            ((Stage) button.getScene().getWindow()).close();
        });

        label.setMaxWidth(Double.MAX_VALUE);
        valueT.setMaxWidth(Double.MAX_VALUE);
        button.setMaxWidth(Double.MAX_VALUE);

        grid1.add(label, 0, 0);
        grid1.add(valueT, 0, 1);
        grid1.add(button, 0, 2);

        Scene scene = new Scene(grid1, 500, 200);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        window.setScene(scene);
        window.setTitle("Definir Valor");
        window.showAndWait();
        window.setX(d.width / 2 - (window.getWidth() / 2));
        window.setY(d.height / 2 - (window.getHeight() / 2));
    }

    public int getValor() {
        return valor;
    }

}
