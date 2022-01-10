package hollstein.kim.polynom;

import java.util.ArrayList;

public class Polynom {
    private final int[] koeffizienten;

    public Polynom(int[] koeffizienten) {
        this.koeffizienten = koeffizienten;
    }

    public int[] getKoeffizienten() {
        return koeffizienten;
        //TODO
    }

    public static int[] removeTrailingZeros(int[] arr) {
        for (int i = arr.length-1; i >= 0; i--) {
            if (arr[i] != 0) {
                int[] newarr = new int[i+1];
                System.arraycopy(arr, 0, newarr, 0, i+1);
                return newarr;
            }
        }
        return new int[0];
    }

    public static int[] setLength(int[] arr, int length) throws IndexOutOfBoundsException {
        if (arr.length > length) {
            throw new IndexOutOfBoundsException();
        }
        int[] newarr = new int[length];
        System.arraycopy(arr, 0, newarr, 0, arr.length);
        return newarr;
    }

    public static int[] shiftArray(int[] arr, int shift) throws IllegalArgumentException {
        if (shift < 0) {
            throw new IllegalArgumentException();
        }
        int[] newarr = new int[arr.length+shift];
        System.arraycopy(arr, 0, newarr, shift, arr.length);
        return newarr;
    }

    public static int[] simplifyBasis(int[] arr, int basis) throws IllegalArgumentException {
        int[] newarr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            newarr[i] = Math.floorMod(arr[i],basis);
        }
        return newarr;
    }

    public static int modDevide(int d1, int d2, int basis) {
        int dm1 = Math.floorMod(d1,basis);
        int dm2 = Math.floorMod(d2,basis);

        for (int i = 0; i < basis; i++) {
            if (dm1 == Math.floorMod(dm2 * i, basis)) {
                return i;
            }
        }
        throw new IllegalArgumentException();
    }

    public static int[] multiplyPolynom(int[] p, int fk, int fp, int basis) {
        int[] newp = shiftArray(p,fp);
        for (int i = 0; i < newp.length; i++) {
            newp[i] *= fk;
        }

        return removeTrailingZeros(simplifyBasis(newp,basis));
    }

    public static int[] subtractPolynom(int[] p1, int[] p2, int basis) {
        int length = Math.max(p1.length, p2.length);
        int[] p1l = setLength(p1, length);
        int[] p2l = setLength(p2, length);
        int[] newp = new int[length];

        for (int i = 0; i < length; i++) {
            newp[i] = p1l[i] - p2l[i];
        }

        return removeTrailingZeros(simplifyBasis(newp,basis));
    }

    public String getPolynomString() {
        String polynomString = "";
        for (int i = 0; i < koeffizienten.length; i++) {
            if (koeffizienten[i] == 1) {
                if (i == 0) {
                    polynomString = "1 " + polynomString;
                } else if (i == 1) {
                    polynomString = "x " + polynomString;
                } else {
                    polynomString = String.format("x%d ",i) + polynomString;
                }
            } else if (koeffizienten[i] != 0) {
                if (i == 0) {
                    polynomString = String.format("%d ",koeffizienten[i]) + polynomString;
                } else if (i == 1) {
                    polynomString = String.format("%dx ",koeffizienten[i]) + polynomString;
                } else {
                    polynomString = String.format("%dx%d ",koeffizienten[i],i) + polynomString;
                }
            }
        }
        if (polynomString.equals("")) {
            polynomString = "0";
        }
        return polynomString.trim();
    }

    public PolynomDivision teilePolynom(Polynom polynom, int basis) {
        int[] p1 = removeTrailingZeros(simplifyBasis(this.getKoeffizienten(),basis));
        int[] p2 = removeTrailingZeros(simplifyBasis(polynom.getKoeffizienten(),basis));

        int[] result = new int[p1.length]; //Result
        String log1 = String.format("####################\n\n(%s) / (%s) [in Z%d]\n\n",new Polynom(p1).getPolynomString(),new Polynom(p2).getPolynomString(),basis);
        String log3 = String.format("   %s\n",new Polynom(p1).getPolynomString());
        while (p1.length >= p2.length) {
            int gr1 = p1.length-1; //Grad
            int gr2 = p2.length-1; //
            int lk1 = p1[gr1]; //Leitkoeffizient
            int lk2 = p2[gr2]; //

            int fp = gr1 - gr2; //Potenz
            int fk = modDevide(lk1,lk2,basis); //Koeffizient
            result[fp] = fk;

            int[] p2t = multiplyPolynom(p2, fk, fp, basis);
            log3 += String.format("-( %s )\n",new Polynom(p2t).getPolynomString());
            p1 = subtractPolynom(p1, p2t, basis);
            log3 += String.format("----------\n   %s\n",new Polynom(p1).getPolynomString());
        }

        Polynom pResult = new Polynom( removeTrailingZeros(simplifyBasis(result,basis)) );
        Polynom pRest = new Polynom(p1);
        String log2 = String.format(" = %s\n\n",pResult.getPolynomString());
        log3 += "\n####################";

        return new PolynomDivision(pResult, pRest, log1+log2+log3);
    }

    public String faktorisierePolynom(int basis) {
        Polynom p1 = this;
        boolean loop = true;
        StringBuilder faktorisierung = new StringBuilder();
        while (loop) {
            for (int i = 0; i < basis; i++) {
                Polynom teiler = new Polynom(new int[]{basis-i, 1});
                PolynomDivision d=p1.teilePolynom(teiler,basis);

                if (d.getRest().getPolynomString().equals("0")) {
                    p1 = d.getErgebnis();
                    System.out.println("\nNullstelle: X = "+i+" Faktor: ("+teiler.getPolynomString()+")");
                    faktorisierung.append("\n").append(teiler.getPolynomString());
                    System.out.println("\n"+d.getLog());
                    loop = true;
                    break;
                }
                loop = false;
            }
        }
        faktorisierung.append("\n").append(p1.getPolynomString());
        String pre = "\n"+this.getPolynomString()+" =\n";
        System.out.println(pre+faktorisierung.toString());
        System.out.println("\n####################\n####################");
        return pre+faktorisierung.toString();
    }

    public static void faktorisierePolynome(Polynom[] polys, int basis){
        StringBuilder zerlegung = new StringBuilder();
        for (int i = 0; i < polys.length; i++) {
            zerlegung.append(polys[i].faktorisierePolynom(basis));
            zerlegung.append("\n");
        }
        System.out.println(zerlegung.toString());
        System.out.println("\n####################\n####################");
    }
}
