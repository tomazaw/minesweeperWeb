/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package pl.polsl.lab;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.polsl.lab.model.CellException;
import pl.polsl.lab.model.GameManager;
import pl.polsl.lab.model.PositionOnBoard;


/**
 *
 * @author Tomasz Zawadzki
 * @version Final 1.1
 */
public class GameManagerTest {

    private static GameManager testingGameManager;

    @BeforeAll
    public static void setupClass() {
        testingGameManager = new GameManager();
    }

    /**
     * method testing if the user input is valid and within game boundaries
     *
     * @param boardSize Given board size. This value should be valid as it is
     * tested in another method.
     * @param x given x position
     * @param y given y position
     * @param expectedResult expected test result
     */
    @ParameterizedTest
    @MethodSource("testSet")
    public void testValidateInput(int boardSize, int x, int y, boolean expectedResult) {
        //given
        PositionOnBoard pos = new PositionOnBoard(x, y);
        int amountOfMines = boardSize * boardSize / 2 - 1;
        testingGameManager.initialize(boardSize, amountOfMines);

        try {
            //when
            boolean ifInputValid = testingGameManager.validateInput(pos);
            //then
            assertEquals(ifInputValid, expectedResult);
        } catch (CellException e) {
            fail("Unexpected cell exception thrown.");
        }
    }

    /**
     * test if the method throws an exception if the user input is valid but the
     * cell is already revealed
     *
     * @param boardSize given board size
     * @param x given x position
     * @param y given y position
     */
    @ParameterizedTest
    @MethodSource("exceptionTestSet")
    public void testValidateInputException(int boardSize, int x, int y) {
        //given
        PositionOnBoard pos = new PositionOnBoard(x, y);
        int amountOfMines = 0;
        testingGameManager.initialize(boardSize, amountOfMines);
        try {
            testingGameManager.getBoard().getCells().get(x).get(y).reveal();
        } catch (CellException e) {
            fail("Cell exception thrown during first reveal.");
        }

        try {
            //when
            testingGameManager.validateInput(pos);
            //then
            fail("Exception not thrown when it should be.");
        } catch (CellException e) {

        }
    }

    /**
     * test if the method correctly cheks the given beginning input
     *
     * @param boardSize given boardSize
     * @param mineAmount given mineAmount
     * @param expectedResult expected test result
     */
    @ParameterizedTest
    @MethodSource("beginningTestSet")
    public void testValidateBeginningInput(int boardSize, int mineAmount, boolean expectedResult) {

        //when
        boolean result = testingGameManager.validateBeginningInput(boardSize, mineAmount);

        //then
        assertEquals(result, expectedResult);
    }

    private static Stream<Arguments> testSet() {
        return Stream.of(
                arguments(10, 2, 3, true),
                arguments(5, 1, 0, true),
                arguments(2, 0, 0, true),
                arguments(6, 5, 5, true),
                arguments(8, 6, 4, true),
                arguments(5, 12, 6, false),
                arguments(4, -1, 5, false),
                arguments(5, 5, 5, false),
                arguments(9, 0, -1, false),
                arguments(7, 7, -12, false)
        );
    }

    private static Stream<Arguments> exceptionTestSet() {
        return Stream.of(
                arguments(5, 3, 2),
                arguments(5, 1, 0),
                arguments(2, 0, 0),
                arguments(6, 5, 5),
                arguments(8, 6, 4)
        );
    }

    private static Stream<Arguments> beginningTestSet() {
        return Stream.of(
                arguments(8, 2, true),
                arguments(7, 6, true),
                arguments(10, 49, true),
                arguments(0, 0, false),
                arguments(-5, 4, false),
                arguments(2, 2, true)
        );
    }
}
