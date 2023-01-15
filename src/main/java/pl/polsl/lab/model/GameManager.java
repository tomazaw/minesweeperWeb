/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl.polsl.lab.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains information about the game, which contains the game
 * board.
 *
 * @author Tomasz Zawadzki
 * @version Final 1.2
 */
public class GameManager {

    /**
     * A private instance of the Board class.
     */
    private Board gameBoard;

    /**
     * Default constructor of the GameManager class.
     */
    public GameManager() {

    }

    /**
     * Initializes an object of the Board class.
     *
     * @param boardSize Given board size
     * @param mineCount Given number of mines.
     */
    public void initialize(int boardSize, int mineCount) {
        gameBoard = new Board(boardSize, boardSize);
        gameBoard.randomizeMinesOnBoard(mineCount);
    }

    /**
     * gameBoard getter
     *
     * @return gameBoard
     */
    public Board getBoard() {
        return gameBoard;
    }

    /**
     * Validates whether the given input is within the game board boundaries.
     *
     * @param pos Position given by the user
     * @return true the input is valid, otherwise returns false
     * @throws CellException If the cell on the position of the give input is
     * revealed throws an exception.
     */
    public boolean validateInput(PositionOnBoard pos) throws CellException {

        if (gameBoard.isInputValid(pos)) {
            if (gameBoard.getCells().get(pos.getX()).get(pos.getY()).getIsRevealed()) {
                throw new CellException("This cell is already revealed!");
            }
            return true;
        }
        return false;
    }

    /**
     * A method to check whether the beginning input is valid.
     *
     * @param boardSize boardSize user input.
     * @param mineAmount mineAmount user input.
     * @return whether the input is valid.
     */
    public boolean validateBeginningInput(int boardSize, int mineAmount) {
        if ((boardSize * boardSize) / 2 < mineAmount || boardSize <= 1 || mineAmount < 1 || boardSize >= 50) {
            return false;
        }
        return true;

    }

    /**
     *
     * @return
     */
    public int calculateFinalScore() {
        int finalScore = gameBoard.getScore();
        finalScore *= gameBoard.getTotalMines();
        return finalScore;
    }

    /**
     * A method to serialize the list of results in order to use it in another
     * session of the program.
     *
     * @param scoreList given scoreList
     * @throws IOException An input output exception.
     */
    public void serializeScores(List<List<String>> scoreList) throws IOException {
        FileOutputStream fos = new FileOutputStream("scoreList");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(scoreList);
        oos.close();
        fos.close();
    }

    /**
     * A method to deserialize the list of previous results in order to later
     * display the table of results.
     *
     * @return 2D list of results.
     */
    public List<List<String>> deserialize() {
        List<List<String>> scoreList = new ArrayList<>();
        try {
            FileInputStream scoreStream = new FileInputStream("scoreList");
            ObjectInputStream ois = new ObjectInputStream(scoreStream);
            scoreList = (ArrayList) ois.readObject();
            ois.close();
            scoreStream.close();
        } catch (IOException ioe) {
        } catch (ClassNotFoundException c) {

        }
        return scoreList;
    }

    /**
     * Prepares the current and previous game data to be displayed in form of a
     * table and then serialized.
     *
     * @param nickname users nickname
     * @param gameScore current gameScore
     * @param givenList given list of previous game scores.
     * @return new list of game scores.
     */
    public List<List<String>> gatherGameData(String nickname, int gameScore, List<List<String>> givenList) {
        List<List<String>> newList = new ArrayList<>();
        if (givenList.size() != 0) {
            newList = givenList;
        }
        List<String> currentData = new ArrayList<>();
        currentData.add(nickname);
        currentData.add(Integer.toString(gameScore));
        currentData.add(java.time.LocalDate.now().toString());
        newList.add(currentData);
        return newList;
    }

}
