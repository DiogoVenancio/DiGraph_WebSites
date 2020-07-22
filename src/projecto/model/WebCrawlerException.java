/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto.model;

/**
 * Classe que lança exceções relacionadas com a classe WebCrawler
 *
 * @author Diogo Venâncio - 160221076
 * @author Miguel Lapão - 170221003
 */
public class WebCrawlerException extends RuntimeException {

    public WebCrawlerException() {
    }

    public WebCrawlerException(String message) {
        super(message);
    }
}
