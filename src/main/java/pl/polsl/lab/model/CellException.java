/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl.polsl.lab.model;

/**
 * Class that throws an exception when a cell is either revealed or is flagged
 * and the user tries to reveal it.
 *
 * @author Tomasz Zawadzki
 * @version Final 1.0
 */
public class CellException extends Exception {

    /**
     * Exception constructor
     *
     * @param message A given message in a form of a String.
     */
    public CellException(String message) {
        super(message);
    }

}
