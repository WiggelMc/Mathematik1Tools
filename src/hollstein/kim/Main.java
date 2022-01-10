package hollstein.kim;

import hollstein.kim.polynom.Polynom;
import hollstein.kim.run.Base;

import java.util.Scanner;

public class Main extends Base {


    public static void main(String[] args) {

        //#################################################################

        {
            Polynom[] polys = {/*
                new Polynom(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}),*/
                    new Polynom(new int[]{1, 0, 1, 1, 1}),
                    new Polynom(new int[]{1, 1, 1, 0, 1}),
            };
            int basis = 2;
            faktorisierePolynome(polys, basis);
        }

        //#################################################################

        {
            /*-----------new Polynom(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}),*/
            Polynom p1 = new Polynom(new int[]{1, 0, 1, 1, 1});
            Polynom p2 = new Polynom(new int[]{1, 0, 1, 1, 1});
            int basis = 2;
            teilePolynom(p1, p2, basis);
        }

        //#################################################################

        {
            Scanner scanner = new Scanner(System.in);
            int[][][] me = {
                    { {1}, {0},  {1},  {1},  v(1)},
                    { {0}, {0},  {1},  {1},  v(2)},
                    { {1}, {1},  {0},  {1},  v(3)},
                    { {0}, {1},  {1},  {1},  v(4)},
            };
            String[] mv = {
                    "a",
                    "b",
                    "c",
                    "d",
            };
            int basis = 2;
            loeseMatrix(me, mv, basis, scanner);
            scanner.close();
        }

        //#################################################################
    }
}
