package solvers;

import piece.Piece;

import java.util.List;

/**
 * Solves Elohim Puzzle with basic brute-force algorithm.
 */
public class SolverBasic implements Solver {

    @Override
    public boolean findSolution(int[][] currentBox, List<Piece> piecesRemaining) {

        if (isSolution(piecesRemaining)) {
            return true;
        } else {//no solution, process

            //go through all the remaining pieces.
            for (int i = 0; i < piecesRemaining.size(); i++) {

                //System.out.println("pieces remaining: " + piecesRemaining.toString());
                Piece currentPiece = piecesRemaining.get(i);

                //go through all rotations of this piece
                for (int j = 0; j < currentPiece.rotations; j++) {

                    //go through all rows of currentBox (except the last ones cause that's unnecessary)
                    for (int row = 0; row < currentBox.length - currentPiece.row + 1; row++) {

                        //go through all columns of currentBox (except the last ones cause that's unnecessary)
                        for (int col = 0; col < currentBox[0].length - currentPiece.col + 1; col++) {

                            //if we can fit the piece.
                            if (fitPiece(currentBox, currentPiece, row, col)) {
                                Piece temp = piecesRemaining.remove(i);

                                //update variables, recursively go deeper into the rabbit hole...
                                boolean answerFound = findSolution(currentBox, piecesRemaining);

                                if (answerFound)
                                    return true;

                                //go back one step. remove the piece from the box,
                                //and add it back to the pool of pieces
                                removePiece(currentBox, currentPiece, row, col);
                                piecesRemaining.add(i, temp);

                            }
                            //else do nothing

                        }

                    }

                    //rotate piece
                    currentPiece.rotate();

                }//end - for rotations

            }

        }//end else(not a solution)

        return false;
    }

    /**
     * fitPiece
     * <p>
     * attempts to jam in a piece at the set coordinates. returns true if fits, false if not.
     *
     * @param currentBox - the box that will be modified with the fit (if it fits) piece.
     * @param thePiece   - the piece to attempt to fit
     * @param row        of the box.
     * @param col        of the box.
     * @return boolean - success or failure of piece fitting
     */
    private static boolean fitPiece(int[][] currentBox, Piece thePiece, int row, int col) {
        try {
            //attempt to see if we can fit thePiece in the currentBox.
            for (int i = 0; i < thePiece.row; i++) {
                for (int j = 0; j < thePiece.col; j++) {

                    //if piece section exists, remove it from the currentBox
                    if (thePiece.space[i][j] != 0) {
                        //currentBox has that spot occupied
                        if (currentBox[row + i][col + j] != 0)
                            return false;
                    }

                }
            }
        } catch (ArrayIndexOutOfBoundsException ex) {//the fun lazy way.
            return false;
        }

        //put the piece in.
        for (int i = 0; i < thePiece.row; i++) {
            for (int j = 0; j < thePiece.col; j++) {

                //if piece section exists, remove it from the currentBox
                if (thePiece.space[i][j] != 0) {
                    //currentBox has that spot occupied
                    currentBox[row + i][col + j] = thePiece.id;
                }

            }
        }

        //System.out.println("piece Id added was: " + thePiece.id);
        return true;
    }

    /**
     * removePiece
     * <p>
     * removes the piece
     * it is implied that the piece exists when you call this method
     *
     * @param currentBox -
     * @param thePiece   -
     * @param row        - currentBox's row
     * @param col        - currentBox's column
     */
    private static void removePiece(int[][] currentBox, Piece thePiece, int row, int col) {

        //go through every square in the piece and only remove a piece section if
        //the piece section exists
        for (int i = 0; i < thePiece.row; i++) {
            for (int j = 0; j < thePiece.col; j++) {

                //if piece section exists, remove it from the currentBox
                if (thePiece.space[i][j] != 0) {
                    currentBox[row + i][col + j] = 0;
                }

            }
        }

        //System.out.println("piece Id removed was: " + thePiece.id);
    }

    /**
     * isSolution
     * <p>
     * checks to see if this is the solution
     *
     * @param piecesRemaining -
     * @return boolean - true on solution; false if not.
     */
    private static boolean isSolution(List<Piece> piecesRemaining) {
        return piecesRemaining.size() == 0;
    }

}
