package adamnowakowski.sudoku;

import static adamnowakowski.sudoku.SudokuGame.Status.FINISHED;
import static adamnowakowski.sudoku.SudokuGame.Status.ONGOING;
import static java.util.Arrays.stream;
import static lombok.AccessLevel.PRIVATE;

import java.util.Arrays;
import java.util.Random;

import adamnowakowski.sudoku.SudokuController.Cell;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = PRIVATE)
public class SudokuGame {

    public final int[][] board;
    public Status status;
    public int chances;

    private final static int SIZE = 9;
    private final static int CHANCES = 3;

    private final static Random random = new Random();

    public enum Status {
        ONGOING, FINISHED
    }

    public static SudokuGame create() {
        int[][] board = new int[SIZE][SIZE];

        for (int i = 0; i < 4; i++) {
            board[random.nextInt(0, 9)][random.nextInt(0, 9)] = random.nextInt(0, 9);
        }

        solveBoard(board); removeValues(board); return new SudokuGame(board, ONGOING, CHANCES);
    }

    public void update(Cell cell) {
        if (isInsertable(this.board, cell.value(), cell.row(), cell.column())) {
            this.board[cell.row()][cell.column()] = cell.value(); return;
        }

        this.chances -= 1;

        if (this.chances == 0 || stream(this.board).flatMapToInt(Arrays::stream).noneMatch(v -> v == 0))
            status = FINISHED;
    }

    private static void removeValues(int[][] board) {
        int count = 40; while (count > 0) {
            int row = random.nextInt(SIZE); int col = random.nextInt(SIZE); if (board[row][col] != 0) {
                board[row][col] = 0; count--;
            }
        }
    }

    private static boolean solveBoard(int[][] board) {
        for (int row = 0; row < SIZE; row++) {

            for (int column = 0; column < SIZE; column++) {

                if (board[row][column] == 0) {

                    for (int numberToTry = 1; numberToTry <= SIZE; numberToTry++) {

                        if (isInsertable(board, numberToTry, row, column)) {
                            board[row][column] = numberToTry;

                            if (solveBoard(board)) {
                                return true;
                            }

                            else {
                                board[row][column] = 0;
                            }
                        }
                    }

                    return false;
                }
            }
        }

        return true;
    }

    private static boolean isInsertable(int[][] board, int number, int row, int column) {
        return !existsInRow(number, row, board) && !existsInColumn(number, column, board) && !existsInBox(number, row, column, board);
    }

    private static boolean existsInRow(int number, int row, int[][] board) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == number) {
                return true;
            }
        }

        return false;
    }

    private static boolean existsInColumn(int number, int column, int[][] board) {
        for (int i = 0; i < SIZE; i++) {
            if (board[i][column] == number) {
                return true;
            }
        }

        return false;
    }

    private static boolean existsInBox(int number, int row, int column, int[][] board) {
        int currentRow = row - row % 3; int currentColumn = column - column % 3;

        for (int i = currentRow; i < currentRow + 3; i++) {
            for (int j = currentColumn; j < currentColumn + 3; j++) {
                if (number == board[i][j]) {
                    return true;
                }
            }
        }

        return false;
    }

}
