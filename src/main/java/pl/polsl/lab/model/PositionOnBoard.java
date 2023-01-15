/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pl.polsl.lab.model;

/**
 * Class containing the information about the position.
 *
 * @author Tomasz Zawadzki
 * @version Final 1.0
 */
public class PositionOnBoard {

    /**
     * The x coordinate.
     */
    private int x;
    /**
     * The y coordinate.
     */
    private int y;

    /**
     * Constructor of the class. Initializes the position values with the given
     * ones.
     *
     * @param x given x coordinate
     * @param y given y coordinate
     */
    public PositionOnBoard(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * x coordinate getter
     *
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * x coordinate setter
     *
     * @param x given x coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * y coordinate getter
     *
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * y coordinate setter
     *
     * @param y given y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

}
