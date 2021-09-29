package piece;

/**
 * Piece O
 * <p>
 * XX
 * XX
 *
 * @author LK00100100
 */
public class PieceO extends Piece {

    public PieceO() {
        id = -1;
        pieceType = "PieceO";

        row = 2;
        col = 2;

        space = new int[row][col];

        //set space to 0.
        clearSpace();

        //input the space with a non-zero number, which will be
        //replaced with an ID number for solution identification
        space[0][0] = id;
        space[0][1] = id;
        space[1][0] = id;
        space[1][1] = id;

        rotations = 1;
        currentRotation = 1;
    }

    @Override
    public void printPieceType() {
        System.out.print("O");
    }

}
