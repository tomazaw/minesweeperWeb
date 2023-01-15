/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl.polsl.lab.model;

/**
 * A class representing a single cell on the game board. It contains the
 * information whether a cell is flagged, is a mine, if the cell is revealed and
 * how many mines are there nearby.
 *
 * @author Tomasz Zawadzki
 * @version Final 1.0
 */
public class Cell {

    /**
     * Contains information whether a cell is a mine.
     */
    private boolean isMine;

    /**
     * Contains information whether a cell is flagged. A flagged cell cannot be
     * revealed until the user decides to unflag it.
     */
    private boolean isFlag;

    /**
     * Contains information whether a cell is revealed. Revealed means wheter a
     * cell is visible on the game board.
     */
    private boolean isRevealed;

    /**
     * Contains information how many mines are there in the nearest 3*3 area.
     */
    private int nearbyMines;

    /**
     * Basic constructor of class Cell which initializes its basic values.
     */
    public Cell() {
        isRevealed = false;
        isFlag = false;
        nearbyMines = 0;
        isMine = false;
    }

    /**
     * This method reveals a single cell.
     *
     * @throws CellException In case the cell is flagged which means it cannot
     * be revealed an exception is thrown.
     */
    public void reveal() throws CellException {
        if (isFlag) {
            throw new CellException("This cell is flagged. Remove the flag first");
        } else {
            isRevealed = true;
        }

    }

    @Override
    /**
     * Default Java toString() method override used to display cell info in the
     * console as visible signs.
     */
    public String toString() {
        if (isRevealed) {
            if (isMine) {
                return "B";
            } else {
                return "" + nearbyMines;
            }
        } else if (isFlag) {
            return "F";
        } else {
            return "#";
        }
    }

    /**
     * isMine getter
     *
     * @return isMine
     */
    public boolean getIsMine() {
        return isMine;
    }

    /**
     * isMine setter
     *
     * @param isMine Boolean value which contains the information wheter the
     * cell is going to be a mine.
     */
    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    /**
     * isFlag getter
     *
     * @return isFlag
     */
    public boolean getIsFlag() {
        return isFlag;
    }

    /**
     * Sets the value of isFlag to the opposite.
     */
    public void setIsFlag() {
        this.isFlag = !this.isFlag;
    }

    /**
     * isRevealed getter
     *
     * @return isRevealed
     */
    public boolean getIsRevealed() {
        return isRevealed;
    }

    /**
     * nearbyMines getter
     *
     * @return nerbyMines
     */
    public int getNearbyMines() {
        return nearbyMines;
    }

    /**
     * Adds information of a mine nearby stored in the nearbyMines parameter.
     */
    public void addNearbyMine() {
        nearbyMines += 1;
    }

}
