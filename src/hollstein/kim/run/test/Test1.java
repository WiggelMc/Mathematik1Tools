package hollstein.kim.run.test;

import hollstein.kim.polynom.Polynom;
import hollstein.kim.run.Base;

import java.util.Scanner;

public class Test1 extends Base {
    public static void main(String[] args) {
        Polynom[] polys = {/*
            new Polynom(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}),*/
                new Polynom(new int[]{1, 0, 1, 1, 1}),
                new Polynom(new int[]{1, 1, 1, 0, 1}),
        };
        int basis = 2;
        faktorisierePolynome(polys, basis);
    }
}
