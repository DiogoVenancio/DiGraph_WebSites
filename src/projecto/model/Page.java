/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto.model;

import java.awt.Color;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Classe responsável por armazenar a informação de cada página
 *
 * @author Diogo Venâncio - 160221076
 * @author Miguel Lapão - 170221003
 */
public class Page implements Serializable {

    private static int next_id = 0;
    private final int id;
    private String title;
    private Color color;
    private final PageRole role;

    /**
     *
     * Enum para distinguir a main page das restantes
     */
    public enum PageRole {
        MAIN, SUB
    }

    /**
     *
     * Método para gerar uma cor aleatoriamente
     *
     * @return Color
     */
    private Color randomColor() {

        Random rand = new Random();

        List<Color> cores = new ArrayList<>();

        cores.add(Color.blue);
        cores.add(Color.white);
        cores.add(Color.gray);
        cores.add(Color.yellow);
        cores.add(Color.orange);
        cores.add(Color.cyan);

        Color cor = cores.get(rand.nextInt(cores.size()));

        return cor;
    }

    /**
     *
     * Construtor da classe Page
     *
     * @param title Titulo da page
     * @param role Role da page
     */
    public Page(String title, PageRole role) {
        this.id = ++Page.next_id;
        this.title = title;

        if (role.equals(PageRole.MAIN)) {
            this.color = Color.BLACK;
        } else if (title.contains("404")) {
            this.color = Color.RED;
        } else {
            this.color = randomColor();
        }
        this.role = role;

    }

    /**
     *
     * Obter id
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * Obter o titulo de uma page
     *
     * @return titulo
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * Obter a cor de uma page
     *
     * @return cor
     */
    public String getColor() {
        return (getColorName(color)).toLowerCase();
    }

    /**
     *
     * Obter a role da page
     *
     * @return role
     */
    public PageRole getRole() {
        return role;
    }

    /**
     *
     * Alterar o titulo de uma page
     *
     * @param title Titulo de uma page
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * Alterar a cor de uma page
     *
     * @param color Cor de uma page
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     *
     * Ao receber uma cor, obtem o nome da mesma
     *
     * @param c Uma cor
     * @return O nome da cor
     */
    private static String getColorName(Color c) {
        for (Field f : Color.class.getFields()) {
            try {
                if (f.getType() == Color.class && f.get(null).equals(c)) {
                    return f.getName();
                }
            } catch (java.lang.IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
        }
        return "unknown";
    }

    /**
     *
     * Print da page
     *
     * @return Print para a consola
     */
    @Override
    public String toString() {
        String r = "";

        if (isRole(PageRole.MAIN)) {
            r = "Página Príncipal - " + title;
        } else {
            r = title;
        }
        return r;
    }

    /**
     *
     * Verificaa role de uma page
     *
     * @param role Role de uma page
     * @return true se for verdade, caso contrario, false
     */
    public boolean isRole(PageRole role) {
        return this.role == role;
    }

    /**
     *
     * Método equals da Page
     *
     * @param obj Recebe um object
     * @return true se existir, caso contrario, false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Page other = (Page) obj;
        return (this.id == other.id);
    }

    /**
     *
     * Método hashCode da Page
     *
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.id);
        return hash;
    }
}
