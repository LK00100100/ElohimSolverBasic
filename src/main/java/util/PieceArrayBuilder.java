package util;

import java.util.ArrayList;
import java.util.List;

import piece.*;

/**
 * util.PieceArrayBuilder
 * <p>
 * generates pieces (that you define)
 * and
 * stores it in an array of Pieces
 * <p>
 * please input Pieces in an orderly fashion.
 * All pieces of a certain type should be inputed at once
 * The backtracking algorithm requires that the pieces are in sorted (by piece) order.
 * <p>
 * I don't feel like typing up a sorting algorithm to ensure you are good to go.
 * Life is short. yolo
 *
 * @author LK00100100
 */
public class PieceArrayBuilder {

    private ArrayList<Piece> arrayPieces;
    private int id;                            //this will provide a unique id to every piece so solutions can be readable.
    private Piece temp;                        //holds a piece temporarily.
    private int smallestPieceSize;                //the smallest number of blocks a piece takes up in the array.

    public PieceArrayBuilder() {

        arrayPieces = new ArrayList<Piece>();
        id = 1;
        smallestPieceSize = 999999999;

    }

    public void addPieceL(int quantity) {
        //get a new piece, set id, and then add to the arrayPieces.
        for (int i = 0; i < quantity; i++) {
            temp = new PieceL();
            temp.setId(id++);
            arrayPieces.add(temp);
        }

        compareSmallestPieceSize(temp);

    }

    public void addPieceLReversed(int quantity) {
        //get a new piece, set id, and then add to the arrayPieces.
        for (int i = 0; i < quantity; i++) {
            temp = new PieceLReversed();
            temp.setId(id++);
            arrayPieces.add(temp);
        }

        compareSmallestPieceSize(temp);
    }

    public void addPieceSquare(int quantity) {
        //get a new piece, set id, and then add to the arrayPieces.
        for (int i = 0; i < quantity; i++) {
            temp = new PieceSquare();
            temp.setId(id++);
            arrayPieces.add(temp);
        }

        compareSmallestPieceSize(temp);
    }

    public void addPieceStraight(int quantity) {
        //get a new piece, set id, and then add to the arrayPieces.
        for (int i = 0; i < quantity; i++) {
            temp = new PieceStraight();
            temp.setId(id++);
            arrayPieces.add(temp);
        }

        compareSmallestPieceSize(temp);
    }

    public void addPieceT(int quantity) {
        //get a new piece, set id, and then add to the arrayPieces.
        for (int i = 0; i < quantity; i++) {
            temp = new PieceT();
            temp.setId(id++);
            arrayPieces.add(temp);
        }

        compareSmallestPieceSize(temp);
    }

    public void addPieceThunder(int quantity) {
        //get a new piece, set id, and then add to the arrayPieces.
        for (int i = 0; i < quantity; i++) {
            temp = new PieceThunder();
            temp.setId(id++);
            arrayPieces.add(temp);
        }

        compareSmallestPieceSize(temp);
    }

    public void addPieceThunderReversed(int quantity) {
        //get a new piece, set id, and then add to the arrayPieces.
        for (int i = 0; i < quantity; i++) {
            temp = new PieceThunderReversed();
            temp.setId(id++);
            arrayPieces.add(temp);
        }

        compareSmallestPieceSize(temp);
    }

    public ArrayList<Piece> getArrayPieces() {
        return arrayPieces;
    }

    public int getSmallestPiece() {
        return smallestPieceSize;
    }

    public void compareSmallestPieceSize(Piece thePiece) {

        if (thePiece == null)
            return;

        //set the smallest piece (if you can)
        if (thePiece.getBlockSize() < smallestPieceSize)
            smallestPieceSize = thePiece.getBlockSize();
    }


    public void printArray() {

        System.out.print("arrayPieces: ");
        for (int i = 0; i < arrayPieces.size(); i++) {
            arrayPieces.get(i).printPieceType();
            System.out.print(" ");
        }

        System.out.println();
    }

    /**
     * get the smallest block size (int) in the list of pieces
     *
     * @param pieces a list of pieces. on empty or null, throw exception
     * @return the smallest block size
     */
    public static int getSmallestPiece(List<Piece> pieces) {

        if (pieces == null || pieces.size() == 0)
            throw new IllegalArgumentException("list of pieces is empty.");

        int smallestBlockSize = Integer.MAX_VALUE;

        for (Piece piece : pieces) {
            if (piece.getBlockSize() < smallestBlockSize)
                smallestBlockSize = piece.getBlockSize();
        }

        return smallestBlockSize;

    }
}
