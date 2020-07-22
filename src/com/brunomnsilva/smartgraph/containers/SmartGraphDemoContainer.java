/*
 * The MIT License
 *
 * Copyright 2019 Bruno Silva.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.brunomnsilva.smartgraph.containers;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import projecto.Main;
import projecto.ModalWindow;
import projecto.WebCrawler;
import projecto.model.WebCrawlerAction;

/**
 * Classe javafx que mostra um Digrafo gerado automaticamente
 *
 * @author brunomnsilva
 *
 * Changes made by:
 * @author Diogo Venâncio - 160221076
 * @author Miguel Lapão - 170221003
 */
public class SmartGraphDemoContainer extends BorderPane {

    public SmartGraphDemoContainer(SmartGraphPanel graphView, WebCrawler web) {
        start(graphView, web);
    }

    /**
     * Inicializa o layout
     *
     * @param graphView DiGrafo
     * @param web WebCrawler
     */
    private void start(SmartGraphPanel graphView, WebCrawler web) {

        setCenter(new ContentZoomPane(graphView));

        HBox bottom = new HBox(10);

        Button button = createButton("Voltar");
        Button buttonExportJSON = createButton("Exportar JSON");
        Button buttonExportSER = createButton("Exportar SERIA");
        Button buttonInfo = createButton("Info");

        HBox top = new HBox(10);

        Text textPages = createText("Pages: " + web.getNumPages());
        Text textLinks = createText("Links: " + web.getNumLinks());
        Text textRed = createText("Not Found: " + web.getRedPages());
        Text textPopular = createText("Page mais referenciada: " + web.getMostLinkedPage().getTitle()
                + " com " + web.getAdjacent(web.getPage(web.getMostLinkedPage())).size()
                + " ligações");

        button.setOnAction(e -> {
            Main main = new Main();
            try {
                main.start();
            } catch (IOException ex) {
                Logger.getLogger(ModalWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
            ((Stage) button.getScene().getWindow()).close();
        });

        buttonExportSER.setOnAction(e -> {

            try {
                WebCrawlerAction action = new WebCrawlerAction("serialization");
                action.exportWeb(web);

                showAlert("Exportar", "DiGrafo exportado com sucesso!");
            } catch (IllegalAccessError ex) {
                System.out.println(ex.getMessage());
            }

        });

        buttonExportJSON.setOnAction(e -> {

            try {
                WebCrawlerAction action = new WebCrawlerAction("json");
                action.exportWeb(web);

                showAlert("Exportar", "DiGrafo exportado com sucesso!");
            } catch (IllegalAccessError ex) {
                System.out.println(ex.getMessage());

            }
        });

        buttonInfo.setOnAction(e -> {
            showAlert("Info", "DoubleClick numa page para visualizar o caminho mais curto.\n"
                    + "DoubleClick num link para abrir o mesmo no browser");
        });

        CheckBox automatic = new CheckBox("Automatic layout");
        automatic.selectedProperty().bindBidirectional(graphView.automaticLayoutProperty());

        top.getChildren().addAll(textPages, textLinks, textRed, textPopular);
        bottom.getChildren().addAll(automatic, button, buttonExportSER, buttonExportJSON, buttonInfo);

        setTop(top);
        setBottom(bottom);
    }

    /**
     * Método para criar botões
     *
     * @param titulo Titulo do botão
     * @return Button
     */
    public static Button createButton(String titulo) {
        Button b = new Button(titulo);
        b.setMaxWidth(Double.MAX_VALUE);
        return b;
    }

    /**
     * Método para criar os texts
     *
     * @param titulo Texto
     * @return Text
     */
    public static Text createText(String titulo) {
        Text text = new Text(titulo);
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        return text;
    }

    /**
     * Mostrar alerta para o ecrã
     *
     * @param titulo Titulo da janela
     * @param message Mensagem a aparecer
     */
    public static void showAlert(String titulo, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
