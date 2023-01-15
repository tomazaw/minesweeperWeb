/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl.polsl.lab.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class contains the whole information about the game board. It cointains
 * information abour each single cell on the board. It is the database of the
 * cells.
 *
 * @author Tomasz Zawadzki
 * @version Final 1.1
 */
public class Board {

    /**
     * Contains information about the width of the board.
     */
    final private int width;

    /**
     * Contains information about the height of the board.
     */
    final private int height;

    /**
     * Contains information about the amount of mines on the game board.
     */
    private int totalMines;

    /**
     * Contains information how many cells are already revealed.
     */
    private int revealCount;

    /**
     * Countains information about the area of the game board, which tells how
     * many cells are there.
     */
    private int boardSize;

    /**
     * The main data container of the programme. Contains all the singular cells
     * in the form of an ArrayList.
     */
    private List<List<Cell>> cells;

    private int score = 0;

    /**
     * Constructor of board, which initializes its beginning data.
     *
     * @param width Width of the board.
     * @param height Height of the board.
     */
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.totalMines = 0;
        this.revealCount = 0;
        boardSize = this.height * this.width;
        this.cells = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            List<Cell> temporaryList = new ArrayList<>();
            for (int j = 0; j < height; j++) {
                temporaryList.add(new Cell());
            }
            this.cells.add(temporaryList);
        }
    }

    /**
     * cells getter
     *
     * @return cells ArrayList
     */
    public List<List<Cell>> getCells() {
        return cells;
    }

    /**
     * width getter
     *
     * @return width of the game board
     */
    public int getWidth() {
        return width;
    }

    /**
     * height getter
     *
     * @return height of the board
     */
    public int getHeight() {
        return height;
    }

    /**
     * Cheks if the user given input is within the board boundaries.
     *
     * @param pos Position given by the user
     * @return true if the input is valid, otherwise returns false
     */
    public boolean isInputValid(PositionOnBoard pos) {
        if (pos.getX() >= 0 && pos.getX() < this.width && pos.getY() >= 0 && pos.getY() < this.height) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the cell is a mine.
     *
     * @param pos Position given by the user
     * @return true if the cell is a mine, otherwise returns false
     */
    public boolean isMine(PositionOnBoard pos) {
        return cells.get(pos.getX()).get(pos.getY()).getIsMine();
    }

    /**
     * Cheks if the cell is flagged.
     *
     * @param pos Position given by the user
     * @return true if the cell is flagged, otherwise returns false
     */
    public boolean isFlag(PositionOnBoard pos) {
        return cells.get(pos.getX()).get(pos.getY()).getIsFlag();
    }

    /**
     * Adds mine to a given position on board. The declaration of minimum and
     * maximum values ensures that the addNearbyMines method does not exceed the
     * game boundaries.
     *
     * @param pos given position on board
     * @return if the cell is already a mine returns false, otherwise if a mine
     * is succesfully added returns true
     */
    public boolean addMine(PositionOnBoard pos) {
        if (cells.get(pos.getX()).get(pos.getY()).getIsMine()) {
            return false;
        }
        int minX = Math.max(0, pos.getX() - 1);
        int maxX = Math.min(pos.getX() + 1, width - 1);
        int minY = Math.max(0, pos.getY() - 1);
        int maxY = Math.min(pos.getY() + 1, height - 1);
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                cells.get(i).get(j).addNearbyMine();
            }
        }
        cells.get(pos.getX()).get(pos.getY()).setIsMine(true);
        totalMines++;
        return true;
    }

    /**
     * Randomizes the placement of mines on the game board.
     *
     * @param amountOfMines The amount of mines to be placed on the game board.
     */
    public void randomizeMinesOnBoard(int amountOfMines) {
        Random random = new Random();
        for (int i = 0; i < amountOfMines; i++) {
            if (!addMine(new PositionOnBoard(random.nextInt(width), random.nextInt(height)))) {
                i--;
            }

        }
    }

    /**
     * Method used for revealing cells. If the number of nearby mines stored in
     * a single cell exceeds 0, reveals a singular cell. Otherwise it calls the
     * revealAround() method.
     *
     * @param pos Position of the cell to be revealed.
     * @throws CellException In case the cell is a flag, an exception is thrown.
     */
    public void revealSingleCell(PositionOnBoard pos) throws CellException {
        if (cells.get(pos.getX()).get(pos.getY()).getNearbyMines() != 0) {
            cells.get(pos.getX()).get(pos.getY()).reveal();
            revealCount++;
            score++;
        } else {
            revealAround(pos);
        }
    }

    /**
     * Reveals all the cells on the board. This method is used at the end of the
     * game to show the whole game board.
     */
    public void showWholeBoard() {

        /* cells.forEach(cellWidth -> {
            cellWidth.forEach(cellHeight -> {
                try {
                    if (cellHeight.getIsFlag()) {
                        cellHeight.setIsFlag();
                    }
                    cellHeight.reveal();
                } catch (CellException e) {

                }
            });
        });*/
        for (List<Cell> cell : cells) {
            cell.stream().filter(c -> c.getIsFlag() == true).forEach(c -> c.getIsFlag());
            cell.stream().forEach(c -> {
                try {
                    c.reveal();
                } catch (CellException e) {
                }
            });
        }
    }

    /**
     * Checks if the user won the game.
     *
     * @return true if the game is won, otherwise returns false
     */
    public boolean isGameWon() {
        if (this.revealCount + this.totalMines == this.width * this.height) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Calculates the number of cells which are not revealed on the board.
     *
     * @return The number of not revealed cells on the board
     */
    public int leftToReveal() {
        return (this.boardSize - (this.totalMines + this.revealCount));
    }

    /**
     * Calculates the area of the game board.
     *
     * @return the area of the game board
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * In case the number of nearby mines is equal to 0 this method is called
     * and it reveals all of the cells which are in the 3*3. This function is
     * recursively as long as no other cells with the nearby mines count of 0 in
     * the 3*3 area are left to reveal.
     *
     * @param pos
     */
    private void revealAround(PositionOnBoard pos) {
        int minX = Math.max(0, pos.getX() - 1);
        int maxX = Math.min(pos.getX() + 1, width - 1);
        int minY = Math.max(0, pos.getY() - 1);
        int maxY = Math.min(pos.getY() + 1, height - 1);
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                if (!cells.get(i).get(j).getIsRevealed()) {
                    try {
                        cells.get(i).get(j).reveal();

                        if (cells.get(i).get(j).getNearbyMines() == 0) {
                            PositionOnBoard position = new PositionOnBoard(i, j);
                            revealAround(position);
                        }
                        revealCount++;
                        score++;
                    } catch (CellException e) {

                    }
                }
            }
        }

    }

    /**
     * Score getter
     *
     * @return score
     */
    public int getScore() {
        return score;
    }

    /**
     * totalMines getter
     *
     * @return totalMines
     */
    public int getTotalMines() {
        return totalMines;
    }

}
