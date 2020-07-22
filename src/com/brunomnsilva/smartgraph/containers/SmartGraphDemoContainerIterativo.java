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

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import projecto.Main;
import projecto.ModalWindow;
import projecto.WebCrawler;
import projecto.model.Link;
import projecto.model.Observer;
import projecto.model.Page;
import projecto.model.WebCrawlerAction;
import projecto.model.WebCrawlerCareTaker;

/**
 * Classe javafx que mostra um Digrafo gerado iterativamente
 *
 * @author brunomnsilva
 *
 * Changes made by:
 * @author Diogo Venâncio - 160221076
 * @author Miguel Lapão - 170221003
 */
public class SmartGraphDemoContainerIterativo extends BorderPane implements Observer, Serializable {

    private transient Text textPages;
    private transient Text textLinks;
    private transient Text textPopular;
    private transient Text textRed;

    public SmartGraphDemoContainerIterativo(SmartGraphPanel graphView, WebCrawler web, WebCrawlerCareTaker taker) {
        start(graphView, web, taker);
    }

    /**
     * Inicializa o layout
     *
     * @param graphView DiGrafo
     * @param web WebCrawler
     * @param WebCrawlerCareTaker CareTaker
     */
    private void start(SmartGraphPanel graphView, WebCrawler web, WebCrawlerCareTaker taker) {
        setCenter(new ContentZoomPane(graphView));

        HBox bottom = new HBox(10);

        Button button = createButton("Voltar");
        Button buttonUndo = createButton("UNDO");
        Button buttonPath = createButton("PATH");
        Button buttonExportJSON = createButton("Exportar JSON");
        Button buttonExportSER = createButton("Exportar SERIA");
        Button buttonInfo = createButton("Info");

        HBox top = new HBox(10);

        textPages = createText("Pages: " + web.getNumPages());
        textLinks = createText("Links: " + web.getNumLinks());
        textRed = createText("Not Found: " + web.getRedPages());
        textPopular = createText("Page mais referenciada: " + web.getMostLinkedPage().getTitle()
                + " com " + web.getAdjacent(web.getPage(web.getMostLinkedPage())).size()
                + " ligações");

        buttonPath.setOnAction(e -> {
            System.out.print("PATH: ");

            for (Page pagina : web.getPath()) {
                if (pagina.getTitle().equals(web.getPath().get(web.getPath().size() - 1).getTitle())) {
                    System.out.print(pagina.getTitle());
                } else {
                    System.out.print(pagina.getTitle() + " -> ");
                }
            }

            WebCrawler w = new WebCrawler();

            for (Page p : web.getPath()) {
                w.addPage(p);
            }

            for (Vertex<Page> p : w.getDiGraph().vertices()) {
                for (Edge<Link, Page> edge : web.getDiGraph().edges()) {
                    if (edge.vertices()[0].element().getTitle().equals(p.element().getTitle())) {

                        for (Vertex<Page> p1 : w.getDiGraph().vertices()) {
                            if (p1.element().getTitle().equals(edge.vertices()[1].element().getTitle())) {
                                w.addLink(edge.element(), p.element(), edge.vertices()[1].element());
                            }
                        }
                    } else if (edge.vertices()[1].element().getTitle().equals(p.element().getTitle())) {

                        for (Vertex<Page> p1 : w.getDiGraph().vertices()) {
                            if (p1.element().getTitle().equals(edge.vertices()[0].element().getTitle())) {
                                w.addLink(edge.element(), edge.vertices()[0].element(), p.element());
                            }
                        }

                    }
                }
            }

            Digraph<Page, Link> g1 = w.getDiGraph();

            SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
            SmartGraphPanel<Page, Link> graphView1 = new SmartGraphPanel<>(g1, strategy);

            if (g1.numVertices() > 0) {
                for (Vertex<Page> p : g1.vertices()) {
                    if (p.element().getRole().equals(Page.PageRole.MAIN)) {
                        graphView1.getStylableVertex(p.element()).setStyle("-fx-fill: black; -fx-stroke: gold;");
                    } else {
                        graphView1.getStylableVertex(p.element()).setStyle("-fx-fill: " + p.element().getColor() + "; -fx-stroke: black;");
                    }
                }
            }

            Scene scene = new Scene(new SmartGraphDemoContainerPath(graphView1, w, 0), 900, 500);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("DiGraph - Caminho");
            stage.setMinHeight(500);
            stage.setMinWidth(800);
            stage.setScene(scene);
            stage.show();

            graphView1.init();

            graphView1.setEdgeDoubleClickAction(graphEdge -> {
                System.out.println("Edge contains element: " + graphEdge.getUnderlyingEdge().element());

                graphEdge.setStyle("-fx-stroke: black; -fx-stroke-width: 2;");

                try {
                    w.seeLink(graphEdge.getUnderlyingEdge().element().getUrl());
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }

            });

            graphView1.setAutomaticLayout(true);

        });

        buttonUndo.setOnAction(e -> {
            taker.restoreState(web);
            web.notifyObservers();

            graphView.update();
        });

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
            showAlert("Info", "DoubleClick numa page para adicionar ao caminho.\n"
                    + "Undo para reverter ação.");
        });

        CheckBox automatic = new CheckBox("Automatic layout");
        automatic.selectedProperty().bindBidirectional(graphView.automaticLayoutProperty());

        top.getChildren().addAll(textPages, textLinks, textRed, textPopular);
        bottom.getChildren().addAll(automatic, button, buttonUndo, buttonPath, buttonExportSER, buttonExportJSON, buttonInfo);

        setTop(top);
        setBottom(bottom);
    }

    @Override
    public void update(WebCrawler web) {

        textPages.setText("Pages: " + web.getNumPages());
        textLinks.setText("Links: " + web.getNumLinks());
        textRed.setText("Not Found: " + web.getRedPages());

        textPopular.setText("Page mais referenciada: " + web.getMostLinkedPage().getTitle()
                + " com " + web.getAdjacent(web.getPage(web.getMostLinkedPage())).size()
                + " ligações");
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
