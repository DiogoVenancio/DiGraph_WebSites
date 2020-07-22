/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto.model;

import com.brunomnsilva.smartgraph.graph.Digraph;
import java.util.List;

/**
 * Interface que guarda o estado atual do Digrafo e do caminho
 *
 * @author Diogo Venâncio - 160221076
 * @author Miguel Lapão - 170221003
 */
public interface Memento {

    public Digraph<Page, Link> getDiGraphMemento();

    public List<Page> getPathIterativo();
}
