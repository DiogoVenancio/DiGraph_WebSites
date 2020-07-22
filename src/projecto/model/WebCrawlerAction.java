/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto.model;

import projecto.WebCrawler;

/**
 * Classe responsável pela criação dos tipos de ficheiros
 *
 * @author Diogo Venâncio - 160221076
 * @author Miguel Lapão - 170221003
 */
public class WebCrawlerAction {
    
    private WebCrawlerDAO dao;

    public WebCrawlerAction(String type) {
        this.dao = createWebCrawler(type);
    }
    
    private WebCrawlerDAO createWebCrawler(String type){
        switch(type){
            case "serialization":
                return new WebCrawlerSerialization();
            case "json":
                return new WebCrawlerJSon();
            default:
                throw new WebActionException("Esse tipo de ficheiro não existe");
        }
    }
    
    public void exportWeb(WebCrawler web){
        dao.exportWeb(web);
    }
    
    public WebCrawler importWeb(){
        return dao.importWeb();
    } 
}
