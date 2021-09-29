package util;

public class MatrixUtil {

    public static void printIntMatrix(int[][] theMatrix) {
        for (int i = 0; i < theMatrix.length; i++) {
            for (int j = 0; j < theMatrix[0].length; j++) {
                System.out.printf("%2d ", theMatrix[i][j]);
            }
            System.out.println();
        }
    }
}
