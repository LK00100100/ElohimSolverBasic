package solvers;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import piece.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SolverBasicV4Test {

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
        boolean hadSolution = new SolverBasicV4().findSolution(box, pieces);

        assertTrue(hadSolution);
    }

    //26 seconds
    @Test
    void shouldFindHardSolution() {
        System.out.println("lk00100100: this took my computer 26 seconds");

        //setup puzzle
        int[][] box = new int[6][8];

        List<Piece> pieces = new ArrayList<>();

        for (int i = 0; i < 4; i++)
            pieces.add(new PieceT());

        for (int i = 0; i < 2; i++)
            pieces.add(new PieceO());

        pieces.add(new PieceZ());

        for (int i = 0; i < 2; i++)
            pieces.add(new PieceJ());

        for (int i = 0; i < 2; i++)
            pieces.add(new PieceL());

        pieces.add(new PieceI());

        //solve
        Solver solver = new SolverBasicV4();
        boolean hadSolution = solver.findSolution(box, pieces);

        assertTrue(hadSolution);
    }

    @Test
    void shouldNotFindSolution() {
        //setup puzzle
        int[][] box = new int[2][2];

        List<Piece> pieces = new ArrayList<>();
        pieces.add(new PieceT());

        //solve
        Solver solver = new SolverBasicV4();
        boolean hadSolution = solver.findSolution(box, pieces);

        assertFalse(hadSolution);
    }
}