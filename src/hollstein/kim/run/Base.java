package hollstein.kim.run;

import hollstein.kim.matrix.Matrix;
import hollstein.kim.polynom.Polynom;
import hollstein.kim.polynom.PolynomDivision;

import java.util.Scanner;

public class Base {
    public static int[] v(int x) {
        return Matrix.genVar(x);
    }
    public static void teilePolynom(Polynom p1, Polynom p2, int basis) {
        PolynomDivision d = p1.teilePolynom(p2,basis);
        System.out.println(d.getLog());
    }

    public static void loeseMatrix(int[][][] matrix, String[] variables, int basis, Scanner scanner) {
        Matrix m = new Matrix(matrix,variables,basis, true);
        m.solveMatrix(scanner);
    }

    public static void faktorisierePolynome(Polynom[] polys, int basis) {
        Polynom.faktorisierePolynome(polys, basis);
    }
}
