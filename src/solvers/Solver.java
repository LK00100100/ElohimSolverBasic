package solvers;

import piece.Piece;

import java.util.List;

/**
 * Solves Elohim Puzzle
 */
public interface Solver {

    /**
     * @param currentBox      the box to check and this will be altered
     * @param piecesRemaining pieces remaining to cram into the box.
     * @return true if solution found. false otherwise.
     */
    boolean findSolution(int[][] currentBox, List<Piece> piecesRemaining);

}
