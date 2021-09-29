package solvers;

import org.junit.jupiter.api.Test;
import piece.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SolverBasicTest {

    @Test
    void shouldFindSolution() {
        //setup puzzle
        int[][] box = new int[5][4];

        List<Piece> pieces = new ArrayList<>();
        pieces.add(new PieceL());
        pieces.add(new PieceL());
        pieces.add(new PieceJ());
        pieces.add(new PieceT());
        pieces.add(new PieceT());

        //solve
        boolean hadSolution = new SolverBasic().findSolution(box, pieces);

        assertTrue(hadSolution);
    }

    @Test
    void shouldNotFindSolution() {
        //setup puzzle
        int[][] box = new int[2][2];

        List<Piece> pieces = new ArrayList<>();
        pieces.add(new PieceT());

        //solve
        boolean hadSolution = new SolverBasic().findSolution(box, pieces);

        assertFalse(hadSolution);
    }
}