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
import pl.polsl.lab.model.Cell;
import pl.polsl.lab.model.CellException;
import pl.polsl.lab.model.GameManager;


/**
 *
 * @author Tomasz Zawadzki
 * @version Final 1.1
 */
public class CellTest {

    /**
     * Tests if the cell reveal method behaves correctly. In case the cell is
     * flagged an exception should be thrown, otherwise the cell should get
     * revealed.
     *
     * @param boardSize given boardSize
     * @param x given x parameter of the cell
     * @param y given y parameter of the cell
     * @param ifIsFlagged information if the cell is flagged
     */
    @ParameterizedTest
    @MethodSource("revealTestSet")
    public void testReveal(int boardSize, int x, int y, boolean ifIsFlagged) {
        //given
        GameManager testingGameManager = new GameManager();
        int amountOfMines = 0;
        testingGameManager.initialize(boardSize, amountOfMines);
        if (ifIsFlagged) {
            testingGameManager.getBoard().getCells().get(x).get(y).setIsFlag();
            try {
                testingGameManager.getBoard().getCells().get(x).get(y).reveal();
                fail("No exception thrown when it should be. The cell was flagged!");
            } catch (CellException e) {

            }
        } else {
            try {
                //when
                testingGameManager.getBoard().getCells().get(x).get(y).reveal();
            } catch (CellException e) {
                //then
                fail("Unexpected exception thrown. The cell was not flagged.");
            }
        }
    }

    /**
     * tests if the returned value of the cell toString method is correct.
     *
     * @param isFlag if the cell is a flag
     * @param isMine if the cell is a mine
     * @param isRevealed if the cell is revealed
     * @param nearbyMines the amount of nearbyMines to the cell
     */
    @ParameterizedTest
    @MethodSource("toStringTestSet")
    public void testToString(boolean isFlag, boolean isMine, boolean isRevealed, int nearbyMines) {
        //given
        Cell testCell = new Cell();
        if (isMine) {
            testCell.setIsMine(isMine);
        } else {
            for (int i = 1; i <= nearbyMines; i++) {
                testCell.addNearbyMine();
            }
        }
        if (isFlag) {
            testCell.setIsFlag();
        } else if (isRevealed) {
            try {
                testCell.reveal();
            } catch (CellException e) {
                fail("Unexpected CellException during reveal.");
            }
        }
        String expectedResult;
        if (testCell.getIsFlag()) {
            expectedResult = "F";
        } else if (testCell.getIsRevealed()) {
            if (isMine) {
                expectedResult = "B";
            } else {
                expectedResult = "" + nearbyMines;
            }
        } else {
            expectedResult = "#";
        }
        //when
        String result = testCell.toString();

        //then  
        assertEquals(expectedResult, result);

    }

    private static Stream<Arguments> revealTestSet() {
        return Stream.of(
                arguments(5, 3, 4, false),
                arguments(6, 1, 4, true),
                arguments(8, 0, 0, false),
                arguments(10, 9, 9, false),
                arguments(4, 2, 2, true)
        );
    }

    private static Stream<Arguments> toStringTestSet() {
        return Stream.of(
                arguments(false, true, true, 0),
                arguments(true, true, false, 0),
                arguments(false, false, true, 3),
                arguments(true, false, false, 2),
                arguments(false, true, true, -1)
        );
    }
}
