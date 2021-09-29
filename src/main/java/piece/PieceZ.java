package piece;

/**
 * Piece Z
 * <p>
 * X
 * XX
 * X
 *
 * @author LK00100100
 */
public class PieceZ extends Piece {

    public PieceZ() {
        id = -1;
        pieceType = "PieceZ";

        row = 3;
        col = 2;

        space = new int[row][col];

        //set space to 0.
        clearSpace();

        //input the space with a non-zero number, which will be
        //replaced with an ID number for solution identification
        space[0][1] = id;
        space[1][0] = id;
        space[1][1] = id;
        space[2][0] = id;

        rotations = 2;
        currentRotation = 1;
    }

    @Override
    public void printPieceType() {
        System.out.print("PieceZ");
    }

}
