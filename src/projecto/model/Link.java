/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto.model;

import java.io.Serializable;

/**
 * Classe responsável por armazenar a informação de cada link
 *
 * @author Diogo Venâncio - 160221076
 * @author Miguel Lapão - 170221003
 */
public class Link implements Serializable {

    private final String url;
    private final String description;

    /**
     * *
     * Construtor da classe Link
     *
     * @param url Url de um link
     * @param desc Descrição de um link
     */
    public Link(String url, String desc) {
        this.url = url;
        this.description = desc;
    }

    /**
     * *
     * Obter o url de um link
     *
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * *
     * Obter a descrição do link
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * *
     * Método toString da classe Link
     *
     * @return Url
     */
    @Override
    public String toString() {
        return "(" + url + ")";
    }

}
