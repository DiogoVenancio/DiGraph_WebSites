/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import projecto.WebCrawler;

/**
 * Classe Singleton que representa o Log de quando é gerado um grafo
 *
 * @author Diogo Venâncio - 160221076
 * @author Miguel Lapão - 170221003
 */
public final class WebLogger {

    private static WebLogger instance = new WebLogger();

    File fw = new File("log.txt");

    public static WebLogger getInstance() {
        return instance;
    }

    /**
     * Regista as inserções feitas no Digrafo
     * @param p Page
     * @param p1 Page
     * @param link Link
     * @param web WebCrawler
     */
    public void saveToLog(Page p, Page p1, Link link, WebCrawler web) {

        try {
            if (fw.exists() == false) {
                fw.createNewFile();
            }

            PrintWriter out = new PrintWriter(new FileWriter(fw, true));

            out.append((new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(new java.util.Date())) + " | " + p1.getTitle() + " | "
                    + link.getUrl() + " | " + p.getTitle() + " | "
                    + web.getAdjacent(web.getPage(p1)).size() + "\n");
            out.close();
        } catch (IOException e) {
            System.out.println("Erro");
            e.printStackTrace();
        }
    }

    /**
     * Limpa o log anterior
     */
    public void clearLog() {
        fw.delete();
    }

}
