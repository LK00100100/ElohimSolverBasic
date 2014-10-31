import java.util.ArrayList;
import java.util.Scanner;

import Pieces.*;

/**
 * ElohimSolver
 * 
 * solves a game called "Sigil of Elohim" and its tetris box puzzles.
 * the game was made by DevolverDigital. Available for PC/Mobile for free.
 * 
 * This code is easily modifiable so that you can:
 * 1) Have a box with "holes"
 * 2) Have diverse Pieces.
 * 
 * This code was created for part A of the game.
 * 
 * Please note, "bruteforcing backtracking" may take a while to produce the solution.
 * Turning off System.printStuff makes things faster.
 * 
 * email - LK00100100@gmail.com
 * 
 * @author LK00100100
 * @version 1 - No pruning
 *
 */
public class ElohimSolverBasic {

	private static long startTime;				//stop-watch for the program

	public static void main(String[] args) {

		//read some standard input (aka the console).
		Scanner sc = new Scanner(System.in);

		ArrayList<Piece> pieceArray; 			//stores the pieces. (hopefully, you input them in piece order)
		PieceArrayBuilder pf = new PieceArrayBuilder();

		//for the box
		int rows;
		int cols;
		int[][] box;

		//get user input on piece quantity.
		//There is no error checking, cause let's face it... that's work.
		System.out.println("Programmer Note: There isn't any input error checking cause I am lazy");
		System.out.println("How many L Pieces: ");
		pf.addPieceL(sc.nextInt());
		System.out.println("How many L Reversed Pieces: ");
		pf.addPieceLReversed(sc.nextInt());
		System.out.println("How many Square Pieces: ");
		pf.addPieceSquare(sc.nextInt());
		System.out.println("How many Straight Pieces: ");
		pf.addPieceStraight(sc.nextInt());
		System.out.println("How many T Pieces: ");
		pf.addPieceT(sc.nextInt());
		System.out.println("How many Thunder Pieces: ");
		pf.addPieceThunder(sc.nextInt());
		System.out.println("How many Thunder Reversed Pieces: ");
		pf.addPieceThunderReversed(sc.nextInt());
		pieceArray = pf.getArrayPieces();
		pf.printArray();

		//get box size and make one.
		System.out.println("How many rows in the box: ");
		rows = sc.nextInt();
		System.out.println("How many columns in the box: ");
		cols = sc.nextInt();
		box = new int[rows][cols];

		//clears the box matrix (note, you can add holes after these loops)
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < cols; j++)
				box[i][j] = 0;

		startTime = System.currentTimeMillis();

		findSolution(box, pieceArray);

		//print time duration of algorithm
		printStopwatchTime(startTime);

		System.out.println("No solution was found");

		sc.close();

	}

	private static void findSolution(int[][] currentBox, ArrayList<Piece> piecesRemaining){

		if(isSolution(piecesRemaining)){
			solutionFound(currentBox);
			System.exit(0);	//end program immediately.
		}
		else{//no solution, process

			//go through all the remaining pieces.
			for(int i = 0; i < piecesRemaining.size(); i++){

				//System.out.println("pieces remaining: " + piecesRemaining.toString());
				Piece currentPiece = piecesRemaining.get(i);

				//go through all rotations of this piece
				for(int j = 0; j < currentPiece.rotations; j++){

					//go through all rows of currentBox (except the last ones cause that's unnecessary)
					for(int row = 0; row < currentBox.length - currentPiece.row + 1; row++){

						//go through all columns of currentBox (except the last ones cause that's unnecessary)
						for(int col = 0; col < currentBox[0].length - currentPiece.col + 1; col++){

							//if we can fit the piece.
							if(fitPiece(currentBox, currentPiece, row, col)){
								Piece temp = piecesRemaining.remove(i);

								//update variables, recursively go deeper into the rabbit hole...
								findSolution(currentBox, piecesRemaining);

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

	}

	/**
	 * fitPiece
	 * 
	 * attempts to jam in a piece at the set coordinates. returns true if fits, false if not.
	 * 
	 * @param currentBox - the box that will be modified with the fit (if it fits) piece.
	 * @param thePiece - the piece to attempt to fit
	 * @param row of the box.
	 * @param column of the box.
	 * @return boolean - success or failure of piece fitting
	 */
	private static boolean fitPiece(int[][] currentBox, Piece thePiece, int row, int col){

		try{
			//attempt to see if we can fit thePiece in the currentBox.
			for(int i = 0; i < thePiece.row; i++){
				for(int j = 0; j < thePiece.col; j++){

					//if piece section exists, remove it from the currentBox
					if(thePiece.space[i][j] != 0){
						//currentBox has that spot occupied
						if(currentBox[row + i][col + j] != 0)
							return false;
					}

				}
			}
		}
		catch(ArrayIndexOutOfBoundsException ex){//the fun lazy way.
			return false;
		}

		//put the piece in.
		for(int i = 0; i < thePiece.row; i++){
			for(int j = 0; j < thePiece.col; j++){

				//if piece section exists, remove it from the currentBox
				if(thePiece.space[i][j] != 0){
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
	 * 
	 * removes the piece
	 * it is implied that the piece exists when you call this method
	 * 
	 * @param currentBox
	 * @param thePiece
	 * @param row - currentBox's row
	 * @param col - currentBox's column
	 */
	private static void removePiece(int[][] currentBox, Piece thePiece, int row, int col) {

		//go through every square in the piece and only remove a piece section if
		//the piece section exists
		for(int i = 0; i < thePiece.row; i++){
			for(int j = 0; j < thePiece.col; j++){

				//if piece section exists, remove it from the currentBox
				if(thePiece.space[i][j] != 0){
					currentBox[row + i][col + j] = 0;
				}

			}
		}

		//System.out.println("piece Id removed was: " + thePiece.id);
	}

	/**
	 * isSolution
	 * 
	 * checks to see if this is the solution
	 * 
	 * @param piecesRemaining
	 * @return boolean - true on solution; false if not.
	 */
	private static boolean isSolution(ArrayList<Piece> piecesRemaining){

		if(piecesRemaining.size() == 0)
			return true;

		return false;

	}

	/**
	 * solutionFound
	 * 
	 * a solution was found. Print it out
	 * 
	 * @param currentBox
	 */
	private static void solutionFound(int[][] currentBox){
		printIntMatrix(currentBox);

		printStopwatchTime(startTime);

	}

	private static void printStopwatchTime(long timeStart){
		long endTime = System.currentTimeMillis() - timeStart;
		long second = (endTime / 1000) % 60;
		long minute = (endTime / (1000 * 60)) % 60;
		long hour = (endTime / (1000 * 60 * 60)) % 24;
		System.out.printf("Duration (HH/MM/SS): %2d:%2d:%2d ", hour, minute, second);
	}

	private static void printIntMatrix(int[][] theMatrix){
		for(int i = 0; i < theMatrix.length; i++){
			for(int j = 0; j < theMatrix[0].length; j++){
				System.out.printf("%2d ", theMatrix[i][j]);
			}
			System.out.println();
		}
	}
}
