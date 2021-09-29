import java.util.ArrayList;
import java.util.Scanner;

import piece.*;
import util.PieceArrayBuilder;

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
		int rows, cols;
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

		//findSolution(box, pieceArray);

		//print time duration of algorithm
		//printStopwatchTime(startTime);

		System.out.println("No solution was found");

		sc.close();

	}

}
