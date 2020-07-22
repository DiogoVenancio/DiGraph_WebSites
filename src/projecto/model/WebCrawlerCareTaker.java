/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto.model;

import java.io.Serializable;
import java.util.Stack;
import projecto.WebCrawler;

/**
 * Classe que representa o CareTaker
 *
 * @author Diogo Venâncio - 160221076
 * @author Miguel Lapão - 170221003
 */
public class WebCrawlerCareTaker implements Serializable {

    Stack<Memento> objsMemento;

    public WebCrawlerCareTaker() {
        objsMemento = new Stack<>();
    }

    /**
     * Guarda o estado atual do WebCrawler
     * @param web WebCrawler
     */
    public void saveState(WebCrawler web) {

        Memento objMemento = web.createMemento();

        System.out.println("SAVE STATE");

        objsMemento.push(objMemento);
    }

    /**
     * Restora o estado do WebCrawler para o anterior
     * @param web WebCrawler
     */
    public void restoreState(WebCrawler web) {
        if (!objsMemento.isEmpty()) {
            Memento objMemento = objsMemento.pop();

            System.out.println("RESTORE STATE");

            web.setMemento(objMemento);
        }
    }

}
