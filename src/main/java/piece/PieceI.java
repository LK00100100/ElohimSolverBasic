package piece;

/**
 * Piece I
 * <p>
 * XXXX
 *
 * @author LK00100100
 */
public class PieceI extends Piece {

    public PieceI() {
        id = -1;
        pieceType = "PieceI";

        row = 1;
        col = 4;

        space = new int[row][col];

        //set space to 0.
        clearSpace();

        //input the space with a non-zero number, which will be
        //replaced with an ID number for solution identification
        space[0][0] = id;
        space[0][1] = id;
        space[0][2] = id;
        space[0][3] = id;

        rotations = 2;
        currentRotation = 1;
    }

    @Override
    public void printPieceType() {
        System.out.print("I");
    }

}
