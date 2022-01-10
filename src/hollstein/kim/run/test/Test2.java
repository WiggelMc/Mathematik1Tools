package hollstein.kim.run.test;

import hollstein.kim.run.Base;

import java.util.Scanner;

public class Test2 extends Base {
    public static void main(String[] args) {
        {
            Scanner scanner = new Scanner(System.in);
            int[][][] me = {
                    { {3}, {4},  {7},  v(1)},
                    { {1}, {9},  {1},  v(2)},
                    { {3}, {2},  {1},  v(3)},
                    { {6}, {8},  {14},  v(4)},
            };
            String[] mv = {
                    "a",
                    "b",
                    "c",
                    "d",
            };
            int basis = 17;
            loeseMatrix(me, mv, basis, scanner);
            scanner.close();
        }
    }
}
