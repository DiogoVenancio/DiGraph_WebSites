/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto.model;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.TAD_DiGraph;
import com.brunomnsilva.smartgraph.graph.Vertex;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import projecto.WebCrawler;

/**
 * Classe que representa o Memento da classe WebCrawler
 *
 * @author Diogo Venâncio - 160221076
 * @author Miguel Lapão - 170221003
 */
public class MementoWebCrawler implements Memento, Serializable {

    public Digraph<Page, Link> mementoweb;
    private List<Page> pathIterativo;

    public MementoWebCrawler(WebCrawler web, List<Page> path) {

        this.mementoweb = new TAD_DiGraph<>();

        for (Vertex<Page> p : web.getDiGraph().vertices()) {
            this.mementoweb.insertVertex(p.element());
        }

        for (Edge<Link, Page> edge : web.getDiGraph().edges()) {
            this.mementoweb.insertEdge(edge.vertices()[0], edge.vertices()[1], edge.element());
        }

        this.pathIterativo = new ArrayList<>(path);
    }

    /**
     * Obter o estado Digrafo atual
     * @return DiGrafo
     */
    @Override
    public Digraph<Page, Link> getDiGraphMemento() {
        return mementoweb;
    }

    /**
     * Obter o estado do caminho atual
     * @return Lista de pages
     */
    @Override
    public List<Page> getPathIterativo() {
        return pathIterativo;
    }

}
