package adamnowakowski.sudoku.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private final static String ERROY_KEY = "error";

    @ExceptionHandler(value = {
            ExistingGameException.class,
            NoCurrentGameException.class,
            ExistingValueException.class,
            IllegalPositionException.class})
    public ResponseEntity<?> handleError(RuntimeException exception) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(Map.of(ERROY_KEY, exception.getMessage()));
    }

}
