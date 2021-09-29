package solvers;

import piece.Piece;
import util.PieceArrayBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Solves Elohim Puzzle with basic brute-force algorithm.
 * with more solution pruning.
 * version 4.
 * <p>
 * solves a game called "Sigil of Elohim" and its tetris box puzzles.
 * the game was made by DevolverDigital. Available for PC/Mobile for free.
 * <p>
 * This code is easily modifiable so that you can:
 * 1) Have a box with "holes" (not tested).
 * 2) Have diverse Pieces (not tested).
 * <p>
 * This code was created for part A of the game.
 * <p>
 * Please note, "bruteforcing backtracking" may take a while to produce the solution.
 * Turning off System.printStuff makes things faster.
 * <p>
 * Note: Not tested for 4-block tetris pieces.
 * <p>
 * Note: It takes at most 20 minutes for 2-8 & 3-8 (for my computer at least)
 * <p>
 * Note: The order of the sorted pieces heavily affects the program.
 * Straight pieces followed by square pieces have a lower chance of leaving bubbles.
 * If they go first, the program is faster.
 * Maybe? Sorting by piece quantity is better?
 * <p>
 * Pruning ideas?
 * - bubbles that are not divisible by a certain number "like 4"
 * <p>
 * Same as ElohimSolver3 but takes bubbles of minSize and pushes pieces into them
 * <p>
 * This program will take a pool of pieces and jam them in every which way possible into the board. Unfortunately, this is very,
 * very slow. Some partial solutions are bad and shouldn't be investigated further. This program will "prune" bad partial
 * solutions and backtrack.
 * <p>
 * The pruning methods are: 1) In a sorted list of pieces, if the current piece to place down is the same as the previous piece
 * (was calculated), skip to the next piece to avoid recalculation.
 * <p>
 * 2) If the current piece does not fit even with all its rotations, backtrack.
 * <p>
 * 3-1) After placing a piece, check around the immediate area around the piece to see if there are any "bubbles" of spaces which
 * are of a size that are too small (a bubble of two blocks cannot fit any of the 4-block tetris pieces). If there is a "bubble"
 * that's smaller than a certain size, backtrack. For one space, the bubbleCheck() will only look only as far as a certain number
 * so it does look through the whole board. Also there is a 2d grid to store what has been bubbledChecked and the size it
 * encountered. This is checked before looking around the board for the bubble size.
 * <p>
 * 3-2) For every bubble formed from placing that one piece, see if there are enough correct pieces remaining to fill these
 * bubbles. If there aren't, backtrack.
 * <p>
 * This code is significantly faster than than ElohimSolver3 because it cuts down on the recursive solution combination search.
 * <p>
 * Feel free to improve this work.
 */
public class SolverBasicV4 implements Solver {

    //the smallest piece size (in terms of blocks). a "PieceT" has 4 blocks.
    private static int smallestPieceSize;

    @Override
    public boolean findSolution(int[][] currentBox, List<Piece> piecesRemaining) {
        smallestPieceSize = PieceArrayBuilder.getSmallestPiece(piecesRemaining);

        return findSolutionHelper(currentBox, piecesRemaining);
    }

    public boolean findSolutionHelper(int[][] currentBox, List<Piece> piecesRemaining) {

        if (isSolution(piecesRemaining)) {
            return true;
        } else {//no solution, process recursively

            ArrayList<BubbleData> bubbleData = new ArrayList<>();
            Piece currentPiece2;    //a piece that fits in a bubble (out of many bubbles)

            //go through all the remaining pieces.
            for (int i = 0; i < piecesRemaining.size(); i++) {

                Piece currentPiece = piecesRemaining.get(i);

                //check if the previous piece is the same as current.
                //if so, just go to the next piece since we don't want to recalculate the same things.
                //note: assumed piecesRemaining is sorted.
                if (i > 0) {
                    if (currentPiece.equals(piecesRemaining.get(i - 1))) {
                        continue;
                    }
                }

                boolean fittedOnce = false;    //if at least one piece's rotation cannot be placed anywhere, backtrack.

                //go through all rotations of this piece
                for (int j = 0; j < currentPiece.rotations; j++) {

                    //go through all rows of currentBox (except the last ones where the pieces can't fit)
                    for (int row = 0; row < currentBox.length - currentPiece.row + 1; row++) {

                        //go through all columns of currentBox (except the last ones where the pieces can't fit)
                        for (int col = 0; col < currentBox[0].length - currentPiece.col + 1; col++) {

                            //if we can fit the piece.
                            if (fitPiece(currentBox, currentPiece, row, col)) {
                                Piece pieceRemoved = piecesRemaining.remove(i);

								/*
								System.out.println("currentBox");
								printIntMatrix(currentBox);
								System.out.println();
								System.out.println("currentPieceId: " + currentPiece.id);
								System.out.println();
								 */

                                //if placing the piece caused a small bad "bubbles" of space, then don't do the recursion.
                                //the remaining Bubbles WILL have a quantity of pieces from remainingPieces that'll fit in them.
                                if (!bubbleCheckInit(pieceRemoved, piecesRemaining, currentBox, row, col, smallestPieceSize, bubbleData)) {
                                    fittedOnce = true;

                                    //for every good bubble, jam a good piece in it (recursively)
                                    for (BubbleData bubbleDatum : bubbleData) {
										/*
										System.out.println("after BCI bubbleData's size's: " + bubbleData.size());
										printRemainingPieces(piecesRemaining);
										System.out.println();
										*/

                                        for (int x = 0; x < piecesRemaining.size(); x++) {

                                            currentPiece2 = piecesRemaining.get(x);
                                            //if it's the same as the last piece, continue
                                            if (x > 0) {
                                                if (currentPiece2.equals(piecesRemaining.get(x - 1))) {
                                                    continue;
                                                }
                                            }

                                            //if it's the same piece type as the current bubble's pieceType
                                            //jam the piece in.
                                            if (currentPiece2.pieceType.equals(bubbleDatum.PieceType)) {

                                                //rotate the piece so it fits.
                                                for (int r = 0; r < currentPiece2.rotations; r++) {
                                                    if (fitPiece(currentBox, currentPiece2, bubbleDatum.row, bubbleDatum.col))
                                                        break;
                                                    else
                                                        currentPiece2.rotate();
                                                }

                                                //remove the bubbled pieces
                                                bubbleDatum.position = x;
                                                bubbleDatum.thePieceFitted = piecesRemaining.remove(x);

												/*
												System.out.println("Fitting in this piece: ");
												printIntMatrix(bubbleData.get(currentBubble).thePieceFitted.space);
												System.out.println();
												*/
                                                break; //go to the next bubble

                                            }

                                        }


                                    }

                                    //update variables, recursively go deeper into the rabbit hole...
                                    boolean answerFound = findSolutionHelper(currentBox, piecesRemaining);

                                    if (answerFound)
                                        return true;

                                    //remove all of the pieces that filled the bubbles.
                                    for (int x = bubbleData.size() - 1; x >= 0; x--) {
                                        currentPiece2 = bubbleData.get(x).thePieceFitted;

                                        removePiece(currentBox, currentPiece2, bubbleData.get(x).row, bubbleData.get(x).col);
                                        piecesRemaining.add(bubbleData.get(x).position, currentPiece2);
                                        bubbleData.remove(x);
                                    }


                                }

                                //when we backtracked from some recursion,
                                //remove the piece from the box,
                                //and add it back to the pool of pieces
                                removePiece(currentBox, currentPiece, row, col);
                                piecesRemaining.add(i, pieceRemoved);

                            }
                            //else do nothing

                        }

                    }

                    //rotate piece
                    currentPiece.rotate();

                }//end - for rotations

                //if the current piece cannot fit at all (despite rotations) and without creating bad "bubbles" of small space.
                //then don't bother going through the remaining pieces.
                if (!fittedOnce) {
                    break;    //break out of - for(go through all the remaining pieces.);
                }

            }

        }//end else(not a solution)

        return false;
    }

    /**
     * bubbleCheckInit
     * <p>
     * Ex: placing a T in a 2x3 box creates two "bubbles" of size 1.
     * If the smallest remaining piece is of size > 1, then return true, there is a bad bubble.
     * <p>
     * 0+0	0 = bubble
     * +++	+ = piece
     * <p>
     * Then it checks the remaining bubbles of minSize and determines if there are pieces from piecesRemaining that can fit in all bubbles.
     * <p>
     * Ex: If there's 2 PieceL bubbles but only 1 PieceL in remainingPieces, return true (bad bubbles)
     * If there's 2 PieceL bubbles but 3 PieceL in remainingPieces, return false
     *
     * @param lastPiece       - the Piece just placed
     * @param piecesRemaining - the list of Pieces remaining
     * @param currentBox      - the currentBox state
     * @param rowPlaced       - top row of Piece placed
     * @param colPlaced       - left col of Piece placed
     * @param minSize         - the minimum Size of the bubble has to be.
     * @return boolean - true if a bubble smaller than minSize exists or if we don't have enough of the right Pieces in remainingPieces for the bubbles.
     */
    private static boolean bubbleCheckInit(Piece lastPiece, List<Piece> piecesRemaining, int[][] currentBox, int rowPlaced, int colPlaced, int minSize, ArrayList<BubbleData> bubbleData) {

        //keeps track of the "bubble numbers" (the size the space can fit. limited to minSize)
        int[][] bubbleBox = new int[currentBox.length][currentBox[0].length];

        //initialize the bubbleBox
        //the lastPiece is centered.
        //-1 = occupied
        //0 = unchecked
        //>0 = checked, with biggest bubble size found (limited by minSize).
        for (int row = 0; row < bubbleBox.length; row++) {
            for (int col = 0; col < bubbleBox[0].length; col++) {
                //if the spot is occupied.
                if (currentBox[row][col] != 0) {
                    //fill in bubbleBox as occupied.
                    bubbleBox[row][col] = -1;
                } else {
                    //fill in bubbleBox as unoccupied (never done recursion).
                    bubbleBox[row][col] = 0;
                }

            }
        }

        //top left corner of the search space (used in for loop below)
        int row = rowPlaced - 1;
        int col = colPlaced - 1;

        int retVal;

        //go through every space ONLY around the piece.
        for (int i = 0; i < lastPiece.row + 2; i++) {
            for (int j = 0; j < lastPiece.col + 2; j++) {

                //valid range check
                if (row + i < bubbleBox.length && col + j < bubbleBox[0].length) {
                    if (row + i >= 0 && col + j >= 0) {
                        //if this space is unoccupied and hasn't been checked.
                        if (bubbleBox[row + i][col + j] == 0) {

                            if ((retVal = bubbleCheck(bubbleBox, row + i, col + j, 0, minSize)) < minSize) {

								/*
								System.out.println("Small Bubble Found: " + retVal);
								printIntMatrix(bubbleBox);
								System.out.println();
								*/
                                return true;
                            }
                            bubbleBox[row + i][col + j] = retVal;

                            //set every positive value to the retVal
                            for (int x = 0; x < bubbleBox.length; x++) {
                                for (int y = 0; y < bubbleBox[0].length; y++) {
                                    //if the spot is occupied.
                                    if (bubbleBox[x][y] > 0) {
                                        bubbleBox[x][y] = retVal;
                                    }

                                }
                            }

                        }
                    }
                }

            }
        }

        //part two of the bubble Check.
        //see if any of the remaining pieces types can fit in the min-size bubbles.

        //this will keep track of what part of the bubbleBox has been checked
        //cuts down on recursion.
        boolean[][] isChecked = new boolean[bubbleBox.length][bubbleBox[0].length];
        for (int x = 0; x < isChecked.length; x++) {
            for (int y = 0; y < isChecked[0].length; y++) {
                isChecked[x][y] = false;
            }
        }

        //this will fill out what the piece looks like.
        int[][] pieceSpace = new int[bubbleBox.length][bubbleBox[0].length];
        int[][] pieceSpaceTrimmed;    //pieceSpace without all of the useless walls.
        zeroIntMatrix(pieceSpace);

        //clone the array so you don't mess with the original
        ArrayList<Piece> piecesRemainingClone = new ArrayList<>(piecesRemaining);

        //go through all spaces in bubbleBox
        for (int x = 0; x < bubbleBox.length; x++) {
            for (int y = 0; y < bubbleBox[0].length; y++) {
                //if this box has bubble of minsize
                if (!isChecked[x][y] && bubbleBox[x][y] == minSize) {
                    //get the piece
                    bubbleToPiece(bubbleBox, isChecked, pieceSpace, x, y, minSize);

                    BubbleData someBubble = new BubbleData();
                    //trim the pieceSpace of all of the leading white space.
                    //and update BubbleData's upper left corner coordinates
                    pieceSpaceTrimmed = trimIntMatrix(pieceSpace, someBubble);

                    //does this piece exist in the remaining pieces?
                    //if it doesn't return true (bad bubble).
                    if ((retVal = doesPieceExist(piecesRemainingClone, pieceSpaceTrimmed, someBubble)) != -1) {

                        //fill out bubbleData
                        bubbleData.add(someBubble);

						/*
						zeroIntMatrix(pieceSpace);
						System.out.println("Piece exists:");
						printIntMatrix(pieceSpaceTrimmed);
						printRemainingPieces(piecesRemaining);
						*/

                        //remove this from the pool of available pieces
                        //so we don't have 3 PieceL bubbles but 1 PieceL remaining seen as "good"
                        piecesRemainingClone.remove(retVal);

                    } else {
						/*
						System.out.println("Piece does not exist:");
						printIntMatrix(bubbleBox);
						System.out.println("Piece:");
						printIntMatrix(pieceSpaceTrimmed);
						System.out.println();
						printRemainingPieces(piecesRemaining);
						*/

                        //empty out bubbleData since they're all bad now.
                        if (bubbleData.size() > 0)
                            bubbleData.subList(0, bubbleData.size()).clear();

                        return true;    //there is a bad bubble.
                    }

                } else {
                    isChecked[x][y] = true;
                }
            }
        }

        return false;
    }

    /**
     * checks to see if thePiece is in the list of remainingPieces
     *
     * @param remainingPieces list of pieces
     * @param pieceSpace      the piece to check
     * @param bubbleData      -
     * @return the position in remainingPieces where the piece is.
     */
    private static int doesPieceExist(ArrayList<Piece> remainingPieces, int[][] pieceSpace, BubbleData bubbleData) {

        for (int i = 0; i < remainingPieces.size(); i++) {
            Piece currentPiece = remainingPieces.get(i);

            //if this is the same as the last piece, continue
            if (i > 0) {
                if (currentPiece.equals(remainingPieces.get(i - 1))) {
                    continue;
                }
            }

            //compare the two.
            //a piece can have only a max of currentPiece rotations
            for (int r = 0; r < currentPiece.rotations; r++) {
                if (currentPiece.isSpaceEquals(pieceSpace)) {
                    bubbleData.PieceType = currentPiece.pieceType;
                    return i;
                } else {
                    pieceSpace = Piece.rotate(pieceSpace);
                }

            }

        }

        return -1;
    }

    /**
     * this finds bubbles and alters the bubbleBox
     *
     * @param bubbleBox   - keeps track of the size of the bubbles.
     * @param row         - row to check for bubbleBox
     * @param col         - col to check for bubbleBox
     * @param currentSize - current bubble size
     * @param minSize     - the bubble has to be at least this size. this is used to stop recursion.
     * @return currentSize
     */
    private static int bubbleCheck(int[][] bubbleBox, int row, int col, int currentSize, int minSize) {

        //check the bubbleBox for a value, so we don't have to use too much recursion.
        int retVal;

        //check valid range for bubbleBox
        if (row < 0 || col < 0)
            return currentSize;
        if (row >= bubbleBox.length || col >= bubbleBox[0].length)
            return currentSize;

        //if this bubbleBox space is occupied by a piece or void, just go back.
        if (bubbleBox[row][col] == -1)
            return currentSize;
            //if this space has a bubble size number, return the bigger number;
        else if (bubbleBox[row][col] > 0) {
            if (bubbleBox[row][col] > currentSize)
                return bubbleBox[row][col];
            else
                return currentSize;
        }

        //else no bubble number, fill it in.
        bubbleBox[row][col] = ++currentSize;

        //if we've reached the minSize + 1 limit, just return. no need to keep going deeper.
        // minSize + 1 says there could more than the minimum.
        if (currentSize == minSize + 1)
            return currentSize;

        //check left (first)
        //if the square we checked has a larger bubble number, then the current square
        //has the larger bubble number.
        retVal = bubbleCheck(bubbleBox, row, col - 1, currentSize, minSize);
        if (retVal > currentSize) {
            currentSize = retVal;
            bubbleBox[row][col] = currentSize;

            if (currentSize == minSize + 1)
                return currentSize;
        }

        //check up (second)
        retVal = bubbleCheck(bubbleBox, row - 1, col, currentSize, minSize);
        if (retVal > currentSize) {
            currentSize = retVal;
            bubbleBox[row][col] = currentSize;

            if (currentSize == minSize + 1)
                return currentSize;
        }

        //check right
        retVal = bubbleCheck(bubbleBox, row, col + 1, currentSize, minSize);
        if (retVal > currentSize) {
            currentSize = retVal;
            bubbleBox[row][col] = currentSize;

            if (currentSize == minSize + 1)
                return currentSize;
        }

        //check down
        retVal = bubbleCheck(bubbleBox, row + 1, col, currentSize, minSize);
        if (retVal > currentSize) {
            currentSize = retVal;
            bubbleBox[row][col] = currentSize;

            if (currentSize == minSize + 1)
                return currentSize;
        }

        return currentSize;
    }


    /**
     * translates a bubble to a Piece's space (int[][]) recursively
     * modifies isSearched & pieceMatrix
     *
     * @param bubbleBox   - what bubbles are where (we only care about the bubbles of minSize)
     * @param isChecked   - what parts have been searched. cuts down on recursion.
     * @param pieceMatrix - the piece we are trying to pull from this one bubble.
     * @param row         - current Row
     * @param col         - current column
     * @param minSize     - the target bubble size we care about.
     */
    private static void bubbleToPiece(int[][] bubbleBox, boolean[][] isChecked, int[][] pieceMatrix, int row, int col, int minSize) {

        //check for valid range
        if (row < 0 || col < 0)
            return;
        if (row >= bubbleBox.length || col >= bubbleBox[0].length)
            return;

        //we checked this box.
        isChecked[row][col] = true;

        //if this is not part of the bubble, go back
        if (bubbleBox[row][col] != minSize) {
            return;
        }
        //else is part of the bubble.

        pieceMatrix[row][col] = minSize;

        //check up

        if (row - 1 >= 0 && !isChecked[row - 1][col])
            bubbleToPiece(bubbleBox, isChecked, pieceMatrix, row - 1, col, minSize);
        //check down
        if (row + 1 < bubbleBox.length && !isChecked[row + 1][col])
            bubbleToPiece(bubbleBox, isChecked, pieceMatrix, row + 1, col, minSize);
        //check left
        if (col - 1 >= 0 && !isChecked[row][col - 1])
            bubbleToPiece(bubbleBox, isChecked, pieceMatrix, row, col - 1, minSize);
        //check right
        if (col + 1 < bubbleBox[0].length && !isChecked[row][col + 1])
            bubbleToPiece(bubbleBox, isChecked, pieceMatrix, row, col + 1, minSize);
    }

    /**
     * trims the walls of the matrix that are filled with 0's.
     * fills out topRow and leftCol of the someBubble
     * <p>
     * 000
     * 010 becomes [1]
     * 000
     *
     * @param theMatrix  -
     * @param someBubble - bubbleData's row and col to fill out
     * @return another Matrix which is trimmed.
     */
    private static int[][] trimIntMatrix(int[][] theMatrix, BubbleData someBubble) {

        int topRow = 0;
        int bottomRow = 0;
        int leftCol = 0;
        int rightCol = 0;

        boolean hasNothing = true;

        //find the top of the matrix with stuff in it
        for (int i = 0; i < theMatrix.length; i++) {
            for (int j = 0; j < theMatrix[0].length; j++) {
                if (theMatrix[i][j] > 0) {
                    hasNothing = false;
                    topRow = i;
                }
            }

            if (!hasNothing)
                break;

        }
        if (hasNothing)
            return null;

        //find the bottom of the matrix with stuff in it
        hasNothing = true;
        for (int i = theMatrix.length - 1; i >= 0; i--) {
            for (int j = 0; j < theMatrix[0].length; j++) {
                if (theMatrix[i][j] > 0) {
                    hasNothing = false;
                    bottomRow = i;
                }
            }

            if (!hasNothing)
                break;

        }
        if (hasNothing)
            return null;

        //find the left of the matrix with stuff in it
        hasNothing = true;
        for (int j = 0; j < theMatrix[0].length; j++) {
            for (int[] matrix : theMatrix) {
                if (matrix[j] > 0) {
                    hasNothing = false;
                    leftCol = j;
                }
            }

            if (!hasNothing)
                break;

        }
        if (hasNothing)
            return null;

        //find the right of the matrix with stuff in it
        hasNothing = true;
        for (int j = theMatrix[0].length - 1; j >= 0; j--) {
            for (int[] matrix : theMatrix) {
                if (matrix[j] > 0) {
                    hasNothing = false;
                    rightCol = j;
                }
            }

            if (!hasNothing)
                break;

        }
        if (hasNothing)
            return null;

        //fill out the bubbleData
        someBubble.row = topRow;
        someBubble.col = leftCol;

        //copy theMatrix into the smaller trimmed one.
        int[][] trimmed = new int[(bottomRow - topRow) + 1][(rightCol - leftCol) + 1];

        for (int i = 0; i < trimmed.length; i++) {
            for (int j = 0; j < trimmed[0].length; j++) {
                trimmed[i][j] = theMatrix[topRow + i][leftCol + j];
            }
        }

        return trimmed;
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

    //fills an int matrix will zeroes
    private static void zeroIntMatrix(int[][] a) {

        for (int x = 0; x < a.length; x++) {
            for (int y = 0; y < a[0].length; y++) {
                a[x][y] = 0;
            }
        }
    }

    @SuppressWarnings("unused")
    //mainly for testing purposes.
    private static void printRemainingPieces(ArrayList<Piece> thePieces) {

        for (int i = 0; i < thePieces.size(); i++) {
            System.out.print(thePieces.get(i).pieceType + " ");
        }

        System.out.println();
    }

    public static class BubbleData {

        //the upper left corner of the bubble
        public int row;
        public int col;

        public String PieceType;    //what piece type is required to fit here

        public Piece thePieceFitted;//the piece you fit in this bubble
        public int position;        //array position that you took this out of Piece Remaining

    }

}
