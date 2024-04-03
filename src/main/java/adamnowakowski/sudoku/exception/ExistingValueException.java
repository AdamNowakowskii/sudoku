package adamnowakowski.sudoku.exception;

public class ExistingValueException extends RuntimeException {

    public ExistingValueException(String message) {
        super(message);
    }
}
