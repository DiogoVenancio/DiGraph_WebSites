/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import projecto.WebCrawler;

/**
 * Classe que realiza as ações do DAO em tipo serialização
 *
 * @author Diogo Venâncio - 160221076
 * @author Miguel Lapão - 170221003
 */
public class WebCrawlerSerialization implements WebCrawlerDAO {

    private static final String fileName = "webCrawler.dat";
    private WebCrawler webC;

    public WebCrawlerSerialization() {
    }

    /**
     * Dá export de um WebCrawler
     * @param web WebCrawler
     * @throws WebActionException  Caso não seja possivel guardar
     */
    @Override
    public void exportWeb(WebCrawler web) throws WebActionException {

        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream("" + fileName);
            try ( ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                out.writeObject(web);
            }
            fileOut.close();
        } catch (IOException ex) {
            Logger.getLogger(WebCrawlerSerialization.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Dá import de um WebCrawler guardado em ficheiro
     * @return Um WebCrawler
     * @throws WebActionException Caso não exista ficheiro
     */
    @Override
    public WebCrawler importWeb() throws WebActionException {
        try {
            FileInputStream fileIn = new FileInputStream("" + fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            this.webC = (WebCrawler) in.readObject();
            in.close();
            fileIn.close();

            return this.webC;
        } catch (IOException | ClassNotFoundException ex) {
            throw new WebActionException("Não existe ficheiro guardado!");
        }
    }

}
