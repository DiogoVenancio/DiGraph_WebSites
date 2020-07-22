/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto.model;

import projecto.WebCrawler;

/**
 * Interface para definir as operações que se pretende efetuar sobre o objecto
 *
 * @author Diogo Venâncio - 160221076
 * @author Miguel Lapão - 170221003
 */
public interface WebCrawlerDAO {

    public void exportWeb(WebCrawler web) throws WebActionException;

    public WebCrawler importWeb() throws WebActionException;
}
