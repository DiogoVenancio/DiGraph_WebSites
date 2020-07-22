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
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import projecto.WebCrawler;

/**
 * Classe javafx que mostra um caminho do Digrafo
 *
 * @author brunomnsilva
 *
 * Changes made by:
 * @author Diogo Venâncio - 160221076
 * @author Miguel Lapão - 170221003
 */
public class SmartGraphDemoContainerPath extends BorderPane {

    public SmartGraphDemoContainerPath(SmartGraphPanel graphView, WebCrawler web, int res) {
        start(graphView, web, res);
    }

    /**
     * Inicializa o layout
     *
     * @param graphView DiGrafo
     * @param web WebCrawler
     * @param int Custo total do caminho
     */
    private void start(SmartGraphPanel graphView, WebCrawler web, int res) {
        setCenter(new ContentZoomPane(graphView));

        HBox bottom = new HBox(10);
        Button button = createButton("Voltar");

        HBox top = new HBox(10);

        Text textPages = createText("Pages: " + web.getNumPages());
        Text textLinks = createText("Links: " + web.getNumLinks());

        Text textCusto = createText("");

        if (res != 0) {
            textCusto.setText("Custo Total: " + res);
            top.getChildren().addAll(textPages, textLinks, textCusto);
        } else {
            top.getChildren().addAll(textPages, textLinks);
        }

        button.setOnAction(e -> {
            ((Stage) button.getScene().getWindow()).close();
        });

        CheckBox automatic = new CheckBox("Automatic layout");
        automatic.selectedProperty().bindBidirectional(graphView.automaticLayoutProperty());

        bottom.getChildren().addAll(automatic, button);

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

}
