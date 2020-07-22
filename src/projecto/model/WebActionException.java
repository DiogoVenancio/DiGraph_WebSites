/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto.model;

/**
 * Classe de exceções relativamente á classe WebAction
 *
 * @author Diogo Venâncio - 160221076
 * @author Miguel Lapão - 170221003
 */
public class WebActionException extends RuntimeException {

    public WebActionException() {
    }

    public WebActionException(String message) {
        super(message);
    }

}
