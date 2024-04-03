package adamnowakowski.sudoku.exception;

public class ExistingGameException extends RuntimeException {

    public ExistingGameException(String message) {
        super(message);
    }

}
