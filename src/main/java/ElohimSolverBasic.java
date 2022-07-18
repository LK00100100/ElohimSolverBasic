import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import piece.*;
import solvers.Solver;
import solvers.SolverBasic;
import solvers.SolverBasicV3;
import solvers.SolverBasicV4;
import util.MatrixUtil;
import util.PieceArrayBuilder;

/**
 * ElohimSolver
 * <p>
 * solves a game called "Sigil of Elohim" and its tetris box puzzles.
 * the game was made by DevolverDigital. Available for PC/Mobile for free.
 * <p>
 * This code is easily modifiable so that you can:
 * 1) Have a box with "holes"
 * 2) Have diverse Pieces.
 * <p>
 * This code was created for part A of the game.
 * <p>
 * Please note, "bruteforcing backtracking" may take a while to produce the solution.
 * Turning off System.printStuff makes things faster.
 * <p>
 * email - LK00100100@gmail.com
 *
 * @author LK00100100
 * @version 1 - No pruning
 */
public class ElohimSolverBasic {

    public static void main(String[] args) {

        //read some standard input (aka the console).
        Scanner sc = new Scanner(System.in);

        ArrayList<Piece> pieceArray;            //stores the pieces. (hopefully, you input them in piece order)
        PieceArrayBuilder pf = new PieceArrayBuilder();

        //for the box
        int rows, cols;
        int[][] box;

        //get user input on piece quantity.
        //There is no error checking, cause let's face it... that's work.
        System.out.println("Programmer Note: There isn't any input error checking cause I am lazy");
        System.out.println("How many L Pieces: ");
        pf.addPieceL(sc.nextInt());
        System.out.println("How many J Pieces: ");
        pf.addPieceJ(sc.nextInt());
        System.out.println("How many O Pieces: ");
        pf.addPieceO(sc.nextInt());
        System.out.println("How many I Pieces: ");
        pf.addPieceI(sc.nextInt());
        System.out.println("How many T Pieces: ");
        pf.addPieceT(sc.nextInt());
        System.out.println("How many S Pieces: ");
        pf.addPieceS(sc.nextInt());
        System.out.println("How many Z Pieces: ");
        pf.addPieceZ(sc.nextInt());

        pieceArray = pf.getArrayPieces();
        pf.printArray();

        //get box size and make one.
        System.out.println("How many rows in the box: ");
        rows = sc.nextInt();
        System.out.println("How many columns in the box: ");
        cols = sc.nextInt();
        box = new int[rows][cols];

        //pick solver
        Solver solver = new SolverBasicV4();
        System.out.println("Which solver? basic, v3, v4  (recommend)");
        switch (sc.next()) {
            case "basic":
                solver = new SolverBasic();
                break;
            case "v3":
                solver = new SolverBasicV3();
                break;
            case "v4":
                solver = new SolverBasicV4();
                break;
            default:
                System.out.println("solver not supported. using solver v4");
        }

        System.out.println("using solver: " + solver);

        LocalDateTime startTime = LocalDateTime.now();

        boolean hasSolution = solver.findSolution(box, pieceArray);

        LocalDateTime endTime = LocalDateTime.now();

        //print timing
        Duration duration = Duration.between(endTime, startTime);
        System.out.println("took this much time: " + duration.toString());

        //print solution (or not)
        if (hasSolution)
            MatrixUtil.printIntMatrix(box);
        else
            System.out.println("No solution was found");

        sc.close();
    }

}
