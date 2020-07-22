/* 
 * The MIT License
 *
 * Copyright 2019 brunomnsilva@gmail.com.
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
package projecto;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graph.Vertex;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainerIterativo;
import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainerPath;
import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import projecto.model.Link;
import projecto.model.Page;
import projecto.model.WebCrawlerAction;
import projecto.model.WebCrawlerCareTaker;
import projecto.model.WebLogger;

/**
 * Classe javafx que representa o menu inicial da aplicação
 *
 * @author brunomnsilva
 *
 * Changes made by:
 * @author Diogo Venâncio - 160221076
 * @author Miguel Lapão - 170221003
 */
public class Main extends GridPane {

    public Main() {
    }

    /**
     * Inicializa o Menu Inicial
     *
     * @throws IOException Exceção
     */
    public void start() throws IOException {

        Stage ignored = new Stage();

        ignored.setTitle("Main Page");
        ignored.centerOnScreen();

        Text text = new Text("Web Generator");
        text.setFont(Font.font(null, FontWeight.BOLD, 20));

        Text textIterativo = new Text("Modo iterativo");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));

        Button itButton = createButton("Start");

        Text textAutomatico = new Text("Modo Automático");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));

        Button bfsButton = createButton("BFS Generator");
        Button bfsButtonMax = createButton("BFS - Max Pages");
        Button dfsButton = createButton("DFS - Max Links");
        Button dfsButtonMax = createButton("DFS - Max Relationships");

        Text textAction = new Text("Import/Export");
        textAction.setFont(Font.font(null, FontWeight.BOLD, 15));

        Button SeriaButton = createButton("Import Seria");
        Button JsonButton = createButton("Import JSON");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(12);

        grid.add(text, 0, 0);
        grid.add(textIterativo, 0, 1);
        grid.add(itButton, 0, 2);
        grid.add(textAutomatico, 0, 5);
        grid.add(bfsButton, 0, 6);
        grid.add(bfsButtonMax, 0, 7);
        grid.add(dfsButton, 0, 8);
        grid.add(dfsButtonMax, 0, 9);
        grid.add(textAction, 0, 11);
        grid.add(SeriaButton, 0, 13);
        grid.add(JsonButton, 0, 14);

        itButton.setOnAction(e -> {
            WebLogger.getInstance().clearLog();
            WebCrawler web = new WebCrawler();
            try {
                System.out.println(getUrl());
                web.grafoGenerator(getUrl());
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            generateDiGraphIterativo(web);

            ((Stage) bfsButton.getScene().getWindow()).close();

        });

        SeriaButton.setOnAction(e -> {

            if (checkFile("serialization") == null) {
                showAlert();
            } else {
                generateDiGraph(checkFile("serialization"));

                ((Stage) bfsButton.getScene().getWindow()).close();
            }

        });

        JsonButton.setOnAction(e -> {

            if (checkFile("json") == null) {
                showAlert();
            } else {
                generateDiGraph(checkFile("json"));

                ((Stage) bfsButton.getScene().getWindow()).close();
            }
        });

        bfsButton.setOnAction(e -> {

            WebCrawler web = new WebCrawler();
            try {
                web.grafoGenerator_BFS(getUrl());
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            generateDiGraph(web);

            ((Stage) bfsButton.getScene().getWindow()).close();
        });

        bfsButtonMax.setOnAction(e -> {

            WebCrawler web = new WebCrawler();

            ModalWindow valor = new ModalWindow();
            valor.Modal();

            try {
                web.grafoGenerator_MaxPages_BFS(getUrl(), valor.getValor());
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            generateDiGraph(web);
            ((Stage) bfsButtonMax.getScene().getWindow()).close();
        });

        dfsButton.setOnAction(e -> {

            WebCrawler web = new WebCrawler();

            ModalWindow valor = new ModalWindow();
            valor.Modal();

            try {
                web.grafoGenerator_MaxLink_DFS(getUrl(), valor.getValor());
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            generateDiGraph(web);

            ((Stage) dfsButton.getScene().getWindow()).close();
        });

        dfsButtonMax.setOnAction(e -> {

            WebCrawler web = new WebCrawler();

            ModalWindow valor = new ModalWindow();
            valor.Modal();

            try {
                web.grafoGenerator_MaxRelan_DFS(getUrl(), valor.getValor());
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            generateDiGraph(web);

            ((Stage) dfsButton.getScene().getWindow()).close();
        });

        Scene scene = new Scene(grid, 500, 500);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        ignored.setScene(scene);
        ignored.show();
        ignored.setX(d.width / 2 - (ignored.getWidth() / 2));
        ignored.setY(d.height / 2 - (ignored.getHeight() / 2));
    }

    /**
     * Método para verifica o ficheiro
     *
     * @param type Tipo de ficheiro
     * @return WebCrawler
     */
    private WebCrawler checkFile(String type) {
        WebCrawlerAction action = new WebCrawlerAction(type);

        return action.importWeb();
    }

    /**
     * Método para gerar o Digrafo num modo automático
     *
     * @param web WebCrawler
     */
    private void generateDiGraph(WebCrawler web) {

        Digraph<Page, Link> g = web.getDiGraph();

        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        SmartGraphPanel<Page, Link> graphView = new SmartGraphPanel<>(g, strategy);

        System.out.println("\n*** STATS ***");
        System.out.println(web.printStats());

        changeColorVertex(g, graphView);

        Scene scene = new Scene(new SmartGraphDemoContainer(graphView, web), 1024, 768);

        initStage(scene, "DiGraph");

        graphView.init();

        graphView.setVertexDoubleClickAction(graphVertex -> {
            System.out.println("Page: " + graphVertex.getUnderlyingVertex().element());

            if (!graphVertex.getUnderlyingVertex().element().getTitle().contains("404") && !graphVertex.getUnderlyingVertex().element().equals(web.getMainPage().element())) {
                MinimumCostPath(web, web.getMainPage().element(), graphVertex.getUnderlyingVertex().element());
            }
        });

        graphView.setEdgeDoubleClickAction(graphEdge -> {
            System.out.println("Link: " + graphEdge.getUnderlyingEdge().element());

            graphEdge.setStyle("-fx-stroke: black; -fx-stroke-width: 2;");

            try {
                web.seeLink(graphEdge.getUnderlyingEdge().element().getUrl());
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        graphView.setAutomaticLayout(true);

    }

    /**
     * Método para gerar o Digrafo no modo iterativo
     *
     * @param web WebCrawler
     */
    private void generateDiGraphIterativo(WebCrawler web) {

        Digraph<Page, Link> g = web.getDiGraph();

        WebCrawlerCareTaker careTaker = new WebCrawlerCareTaker();

        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        SmartGraphPanel<Page, Link> graphView = new SmartGraphPanel<>(g, strategy);

        changeColorVertex(g, graphView);

        careTaker.saveState(web);

        SmartGraphDemoContainerIterativo smart = new SmartGraphDemoContainerIterativo(graphView, web, careTaker);

        web.add(smart);

        Scene scene = new Scene(smart, 1024, 768);

        initStage(scene, "DiGraph");

        graphView.init();

        graphView.setVertexDoubleClickAction(graphVertex -> {
            System.out.println("Page: " + graphVertex.getUnderlyingVertex().element());
            Page p1 = graphVertex.getUnderlyingVertex().element();

            if (!p1.getTitle().contains("404")) {

                if (!graphVertex.getUnderlyingVertex().element().equals(web.getMainPage().element())) {
                    graphView.getStylableVertex(p1).setStyle("-fx-fill: orange; -fx-stroke: gold;");
                }

                try {
                    Iterator it = web.getPath().iterator();
                    boolean existe = false;

                    while (it.hasNext()) {
                        if (it.next().equals(graphVertex.getUnderlyingVertex().element())) {
                            existe = true;
                        }
                    }

                    if (!existe) {
                        web.grafoGenerator(web.getUrlPage(graphVertex.getUnderlyingVertex().element()));
                        web.notifyObservers();

                        careTaker.saveState(web);

                        graphView.update();
                    }

                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        graphView.setEdgeDoubleClickAction(graphEdge -> {
            System.out.println("Link: " + graphEdge.getUnderlyingEdge().element());

            graphEdge.setStyle("-fx-stroke: black; -fx-stroke-width: 2;");

            try {
                web.seeLink(graphEdge.getUnderlyingEdge().element().getUrl());
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        graphView.setAutomaticLayout(true);
    }

    /**
     * Método que irá mostrar o caminho mais curto até determinada page
     *
     * @param web WebCrawler
     * @param orig Page origem
     * @param dest Page destino
     */
    private static void MinimumCostPath(WebCrawler web,
            Page orig, Page dest) {

        List<Page> pathPages = new ArrayList<>();
        List<Link> pathLinks = new ArrayList<>();

        System.out.println(String.format("Melhor path entre %s e %s", orig, dest));
        int res = web.minimumCostPath(dest, orig, pathPages, pathLinks);
        System.out.println(String.format("Custo total = %d", res));
        System.out.println("Pages: " + pathPages);
        System.out.println("Links: " + pathLinks);
        System.out.println("");

        WebCrawler w = new WebCrawler();

        for (Page p : pathPages) {
            w.addPage(p);
        }

        for (Link l : pathLinks) {
            for (Edge<Link, Page> edge : web.getDiGraph().edges()) {
                if (edge.element().equals(l)) {
                    if (pathPages.contains(edge.vertices()[0].element()) && pathPages.contains(edge.vertices()[1].element())) {
                        if (web.LinkExistBetweenPages(edge.vertices()[0].element(), edge.vertices()[1].element(), l.getUrl())) {
                            w.addLink(l, edge.vertices()[0].element(), edge.vertices()[1].element());
                        } else if (web.LinkExistBetweenPages(edge.vertices()[1].element(), edge.vertices()[0].element(), l.getUrl())) {
                            w.addLink(l, edge.vertices()[1].element(), edge.vertices()[0].element());
                        }
                    }
                }
            }
        }

        Digraph<Page, Link> g1 = w.getDiGraph();

        SmartPlacementStrategy strategy1 = new SmartCircularSortedPlacementStrategy();
        SmartGraphPanel<Page, Link> graphView1 = new SmartGraphPanel<>(g1, strategy1);

        changeColorVertex(g1, graphView1);

        Scene scene1 = new Scene(new SmartGraphDemoContainerPath(graphView1, w, res), 900, 500);

        initStage(scene1, "DiGraph - Caminho mais curto");

        graphView1.init();

        graphView1.setEdgeDoubleClickAction(graphEdge -> {
            System.out.println("Link: " + graphEdge.getUnderlyingEdge().element());

            graphEdge.setStyle("-fx-stroke: black; -fx-stroke-width: 2;");

            try {
                w.seeLink(graphEdge.getUnderlyingEdge().element().getUrl());
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        graphView1.setAutomaticLayout(true);
    }

    /**
     * Obtem o url atual da pagina inicial
     *
     * @return Url
     */
    public String getUrl() {
        return MainPage.url;
    }

    /**
     * Método para criar os texts
     *
     * @param titulo Texto
     * @return Text
     */
    public static Button createButton(String titulo) {
        Button b = new Button(titulo);
        b.setMaxWidth(Double.MAX_VALUE);
        return b;
    }

    /**
     * Mostrar alerta para o ecrã
     */
    public static void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Import");
        alert.setHeaderText(null);
        alert.setContentText("Não existe Digrafo guardado!");
        alert.showAndWait();
    }

    /**
     * Inicializa o Stage
     *
     * @param scene Scene
     * @param titulo Titulo da Scene
     */
    public static void initStage(Scene scene, String titulo) {
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle(titulo);
        stage.setMinHeight(500);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Método que altera a color dos vertices no digrafo
     *
     * @param g Digrafo
     * @param graphView View do Digrafo
     */
    public static void changeColorVertex(Digraph<Page, Link> g, SmartGraphPanel<Page, Link> graphView) {
        if (g.numVertices() > 0) {
            for (Vertex<Page> p : g.vertices()) {
                if (p.element().getRole().equals(Page.PageRole.MAIN)) {
                    graphView.getStylableVertex(p.element()).setStyle("-fx-fill: black; -fx-stroke: gold;");
                } else {
                    graphView.getStylableVertex(p.element()).setStyle("-fx-fill: " + p.element().getColor() + "; -fx-stroke: black;");
                }
            }
        }
    }

}
