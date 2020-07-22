/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto.model;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.TAD_DiGraph;
import java.io.FileWriter;
import java.io.IOException;
import projecto.WebCrawler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe que realiza as ações do DAO em formato JSON
 *
 * @author Diogo Venâncio - 160221076
 * @author Miguel Lapão - 170221003
 */
public class WebCrawlerJSon implements WebCrawlerDAO {

    private static final String fileName = "webCrawler.json";

    public WebCrawlerJSon() {
    }

    @Override
    public void exportWeb(WebCrawler web) throws WebActionException {
        FileWriter writer = null;
        try {
            writer = new FileWriter("" + fileName);
            Gson gson = new GsonBuilder().serializeNulls().create();

            gson.toJson(web.getDiGraph(), writer);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(WebCrawlerJSon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public WebCrawler importWeb() throws WebActionException {
        try {
            BufferedReader br = new BufferedReader(new FileReader("" + fileName));
            Gson gson = new GsonBuilder().serializeNulls().create();

            Digraph<Page, Link> di = gson.fromJson(br, new TypeToken<TAD_DiGraph<Page, Link>>() {
            }.getType());

            WebCrawler web = new WebCrawler();

            web.setDiGraph(di);

            return web;
        } catch (IOException ex) {
            throw new WebActionException("Não existe ficheiro guardado!");
        }
    }

}
