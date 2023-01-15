/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package pl.polsl.lab;

import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.polsl.lab.model.Board;
import pl.polsl.lab.model.CellException;
import pl.polsl.lab.model.GameManager;
import pl.polsl.lab.model.PositionOnBoard;


/**
 *
 * @author Tomasz Zawadzki
 * @version Final 1.1
 */
public class BoardTest {

    /**
     * Test to check if the input given by the user is within the game
     * boundaries and is valid.
     *
     * @param x First input given by the user
     * @param y Second input given by the user
     * @param boardSize This is the games board size. It is checked in another
     * test so this input should be valid.
     * @param expectedResult Expected test result
     */
    @ParameterizedTest
    @MethodSource("inputTestSet")
    public void testIsInputValid(int x, int y, int boardSize, boolean expectedResult) {
        //given
        Board testBoard = new Board(boardSize, boardSize);
        PositionOnBoard pos = new PositionOnBoard(x, y);

        //when
        boolean isInputValid = testBoard.isInputValid(pos);

        //then
        assertEquals(expectedResult, isInputValid);

    }

    /**
     * tests if the method adds a mine correctly, and if the nearby mine amounts
     * gets correctly updated.
     *
     * @param boardSize game board size
     * @param x x position for the mine
     * @param y y position for the mine
     */
    @ParameterizedTest
    @MethodSource("addMineTestSet")
    public void testAddMine(int boardSize, int x, int y) {
        //given
        Board testBoard = new Board(boardSize, boardSize);
        PositionOnBoard pos = new PositionOnBoard(x, y);
        int minX = Math.max(0, pos.getX() - 1);
        int maxX = Math.min(pos.getX() + 1, testBoard.getWidth() - 1);
        int minY = Math.max(0, pos.getY() - 1);
        int maxY = Math.min(pos.getY() + 1, testBoard.getHeight() - 1);
        //when
        testBoard.addMine(pos);
        //then
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                if (testBoard.getCells().get(i).get(j).getNearbyMines() == 0) {
                    fail("Nearby mines value not as expected.");
                }
            }
        }
        if (!testBoard.getCells().get(x).get(y).getIsMine()) {
            fail("Cell is not a mine.");
        }

    }

    /**
     * Checks if the method added the correct amount of mines to the board.
     *
     * @param boardSize game board size
     * @param mineAmount the amount of mines which should be added to the board.
     */
    @ParameterizedTest
    @MethodSource("randomizeMinesOnBoardTestSet")
    public void testRandomizeMinesOnBoard(int boardSize, int mineAmount) {
        //given
        Board testBoard = new Board(boardSize, boardSize);
        int countMines = 0;

        //when
        testBoard.randomizeMinesOnBoard(mineAmount);
        //then
        for (int i = 0; i < testBoard.getHeight(); i++) {
            for (int j = 0; j < testBoard.getWidth(); j++) {
                if (testBoard.getCells().get(i).get(j).getIsMine()) {
                    countMines++;
                }
            }
        }

        assertEquals(mineAmount, countMines++);

    }

    /**
     * Attemps to reveal a flagged cell which should throw an exception
     *
     * @param boardSize given board size
     * @param x x position of the cell
     * @param y y position of the cell
     */
    @ParameterizedTest
    @MethodSource("revealSingleExceptionTestSet")
    public void testRevealSingleCellException(int boardSize, int x, int y) {
        Board testBoard = new Board(boardSize, boardSize);
        PositionOnBoard pos = new PositionOnBoard(x, y);
        for (int i = 0; i < testBoard.getCells().size(); i++) {
            for (int j = 0; j < testBoard.getCells().get(i).size(); j++) {
                if (!testBoard.getCells().get(i).get(j).getIsMine()) {
                    testBoard.getCells().get(i).get(j).addNearbyMine();
                }
            }
        }
        testBoard.getCells().get(pos.getX()).get(pos.getY()).setIsFlag();
        try {
            testBoard.getCells().get(pos.getX()).get(pos.getY()).reveal();
            fail("No exception thrown when it should.");
        } catch (CellException e) {

        }
    }

    /**
     * Tests if the amount of revealed cells is correct depending on the
     * nearbyMines amount.
     *
     * @param boardSize given board size
     * @param nearbyMines nearby mines amount in a single cell
     * @param x x position of given cell
     * @param y y position of given cell
     */
    @ParameterizedTest
    @MethodSource("revealSingleCellTestSet")
    public void testRevealSingleCell(int boardSize, int nearbyMines, int x, int y) {
        //given
        Board testBoard = new Board(boardSize, boardSize);
        PositionOnBoard pos = new PositionOnBoard(x, y);
        int countRevealed = 0;
        if (nearbyMines > 0) {
            for (int i = 0; i < testBoard.getCells().size(); i++) {
                for (int j = 0; j < testBoard.getCells().get(i).size(); j++) {
                    if (!testBoard.getCells().get(i).get(j).getIsMine()) {
                        testBoard.getCells().get(i).get(j).addNearbyMine();
                    }
                }
            }
            //when
            try {
                testBoard.revealSingleCell(pos);
                for (int i = 0; i < testBoard.getCells().size(); i++) {
                    for (int j = 0; j < testBoard.getCells().get(i).size(); j++) {
                        if (testBoard.getCells().get(i).get(j).getIsRevealed()) {
                            countRevealed++;
                        }

                    }
                }
                //then
                int expectedResult = 1;
                assertEquals(expectedResult, countRevealed);
            } catch (CellException e) {

            }

        } else if (nearbyMines == 0) {
            for (int i = 0; i < testBoard.getCells().size(); i++) {
                for (int j = 0; j < testBoard.getCells().get(i).size(); j++) {
                    countRevealed++;
                }
            }
            int expectedResult = boardSize * boardSize;
            assertEquals(expectedResult, countRevealed);
        } else {
            fail("Invalid mineAmount input");
        }
    }

    /**
     * checks if the board is fully revealed
     *
     * @param boardSize given board size
     */
    @ParameterizedTest
    @MethodSource("showWholeBoardTestSet")
    public void testShowWholeBoard(int boardSize) {
        //given
        Board testBoard = new Board(boardSize, boardSize);
        //when
        testBoard.showWholeBoard();

        //then
        for (int i = 0; i < testBoard.getWidth(); i++) {
            for (int j = 0; j < testBoard.getHeight(); j++) {
                if (!testBoard.getCells().get(i).get(j).getIsRevealed()) {
                    fail("Cell is not revealed.");
                }
            }
        }
    }

    /**
     * checks if the method correctly calculates if the gameEnding conditions
     * are met.
     *
     * @param boardSize given board size
     * @param mineAmount given mineAmount
     * @param revealedCells given amount of cells which already have been
     * revealed in the game
     * @param expectedResult expected result of the test
     */
    @ParameterizedTest
    @MethodSource("isGameWonTestSet")
    public void testIsGameWon(int boardSize, int mineAmount, int revealedCells, boolean expectedResult) {
        //given

        GameManager testManager = new GameManager();
        testManager.initialize(boardSize, mineAmount);
        int toRevealCounter = revealedCells;
        for (int i = 0; i < testManager.getBoard().getCells().size(); i++) {
            for (int j = 0; j < testManager.getBoard().getCells().get(i).size(); j++) {
                if (!testManager.getBoard().getCells().get(i).get(j).getIsMine()) {
                    testManager.getBoard().getCells().get(i).get(j).addNearbyMine();
                }
            }
        }
        PositionOnBoard pos = new PositionOnBoard(0, 0);
        for (int i = 0; i < testManager.getBoard().getCells().size(); i++) {
            for (int j = 0; j < testManager.getBoard().getCells().get(i).size(); j++) {
                if (toRevealCounter == 0) {
                    break;
                }
                if (testManager.getBoard().getCells().get(i).get(j).getIsMine()) {

                } else {
                    pos.setX(j);
                    pos.setY(i);
                    try {
                        testManager.getBoard().revealSingleCell(pos);
                        toRevealCounter--;
                    } catch (CellException e) {
                        fail("Unexpected cell exception thrown.");
                    }
                }

            }
            if (toRevealCounter == 0) {
                break;
            }
        }

        //when 
        boolean isGameWon = testManager.getBoard().isGameWon();

        //then
        assertEquals(expectedResult, isGameWon);
    }

    /**
     * checks if the method correctly calculates the amount of cells left to
     * reveal.
     *
     * @param boardSize given board size
     * @param mineAmount given amount of mines
     * @param revealedCells given amount of already revealed cells
     * @param expectedResult expected result of test
     */
    @ParameterizedTest
    @MethodSource("leftToRevealTestSet")
    public void testLeftToReveal(int boardSize, int mineAmount, int revealedCells, int expectedResult) {
        //given
        GameManager testManager = new GameManager();
        testManager.initialize(boardSize, mineAmount);
        int revealedCounter = revealedCells;
        PositionOnBoard pos = new PositionOnBoard(0, 0);
        for (int i = 0; i < testManager.getBoard().getHeight(); i++) {
            if (revealedCounter == 0) {
                break;
            }
            for (int j = 0; j < testManager.getBoard().getWidth(); j++) {
                if (revealedCounter == 0) {
                    break;
                }
                if (!testManager.getBoard().getCells().get(i).get(j).getIsMine()) {
                    pos.setX(i);
                    pos.setY(j);
                    testManager.getBoard().getCells().get(i).get(j).addNearbyMine();
                    try {
                        testManager.getBoard().revealSingleCell(pos);
                        revealedCounter--;
                    } catch (CellException e) {
                        fail("Unexpected cell exception during reveal.");
                    }
                }
            }
        }

        //when
        int result = testManager.getBoard().leftToReveal();
        //then
        assertEquals(expectedResult, result);

    }

    private static Stream<Arguments> inputTestSet() {
        return Stream.of(
                arguments(0, 0, 10, true),
                arguments(2, 1, 5, true),
                arguments(5, 6, 7, true),
                arguments(-1, 0, 4, false),
                arguments(4, 3, 4, false)
        );
    }

    private static Stream<Arguments> isGameWonTestSet() {
        return Stream.of(
                arguments(5, 5, 20, true),
                arguments(10, 10, 90, true),
                arguments(4, 6, 10, true),
                arguments(6, 6, 10, false),
                arguments(8, 4, 50, false),
                arguments(4, 6, 2, false)
        );
    }

    private static Stream<Arguments> addMineTestSet() {
        return Stream.of(
                arguments(10, 9, 9),
                arguments(10, 0, 0),
                arguments(4, 2, 2),
                arguments(6, 3, 3),
                arguments(8, 2, 2)
        );
    }

    private static Stream<Arguments> showWholeBoardTestSet() {
        return Stream.of(
                arguments(10),
                arguments(7),
                arguments(4),
                arguments(6),
                arguments(8)
        );
    }

    private static Stream<Arguments> leftToRevealTestSet() {
        return Stream.of(
                arguments(10, 10, 5, 85),
                arguments(6, 4, 15, 17),
                arguments(8, 10, 54, 0),
                arguments(4, 2, 0, 14),
                arguments(3, 1, 2, 6)
        );
    }

    private static Stream<Arguments> randomizeMinesOnBoardTestSet() {
        return Stream.of(
                arguments(10, 49),
                arguments(8, 2),
                arguments(4, 8),
                arguments(6, 12),
                arguments(4, 5)
        );
    }

    private static Stream<Arguments> revealSingleExceptionTestSet() {
        return Stream.of(
                arguments(5, 4, 4),
                arguments(10, 2, 3),
                arguments(8, 5, 7),
                arguments(3, 0, 0),
                arguments(10, 9, 9)
        );
    }

    private static Stream<Arguments> revealSingleCellTestSet() {
        return Stream.of(
                arguments(5, 3, 2, 2),
                arguments(7, 3, 3, 3),
                arguments(8, 1, 7, 4),
                arguments(3, 0, 0, 0),
                arguments(10, 0, 9, 9)
        );
    }

}
