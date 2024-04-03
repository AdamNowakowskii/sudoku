package adamnowakowski.sudoku;

import static adamnowakowski.sudoku.SudokuGame.Status.FINISHED;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import adamnowakowski.sudoku.exception.ExistingGameException;
import adamnowakowski.sudoku.exception.ExistingValueException;
import adamnowakowski.sudoku.exception.IllegalPositionException;
import adamnowakowski.sudoku.exception.NoCurrentGameException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping(value = "/api/sudoku")
public class SudokuController {

    private final static String GAME_KEY = "GAME";

    @ResponseStatus(CREATED)
    @PostMapping(value = "/board")
    public SudokuGame startGame(HttpSession session) {
        if (null != session.getAttribute(GAME_KEY))
            throw new ExistingGameException("There is already a game in progress");

        SudokuGame game = SudokuGame.create();
        session.setAttribute(GAME_KEY, game);
        return game;
    }

    @ResponseStatus(OK)
    @PutMapping(value = "/board/cell")
    public SudokuGame insertValue(@RequestBody @Valid Cell cell, HttpSession session) {
        SudokuGame game = (SudokuGame) session.getAttribute(GAME_KEY);

        if (null == game)
            throw new NoCurrentGameException("No game in progress");

        if (!isPositionValid(game, cell.row, cell.column))
            throw new IllegalPositionException("Provided position doesn't exist");

        if (game.board[cell.row][cell.column] != 0)
            throw new ExistingValueException("The cell has already been modified");

        game.update(cell);

        if (game.status == FINISHED) session.removeAttribute(GAME_KEY);

        return game;
    }

    private static boolean isPositionValid(SudokuGame game, int row, int column) {
        int size = game.board.length;
        return row >= 0
                && row < size
                && column >= 0
                && column < size;
    }

    public record Cell(
            @Valid @RequestParam @NotNull @Min(1) @Max(9) int value,
            @Valid @RequestParam @NotNull int row,
            @Valid @RequestParam @NotNull int column) {}

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping(value = "/board")
    public void endGame(HttpSession session) {
        if (null == session.getAttribute(GAME_KEY))
            throw new NoCurrentGameException("No game in progress");

        session.removeAttribute(GAME_KEY);
    }

}
