package adamnowakowski.sudoku.exception;

public class NoCurrentGameException extends RuntimeException {

    public NoCurrentGameException(String message) {
        super(message);
    }

}
