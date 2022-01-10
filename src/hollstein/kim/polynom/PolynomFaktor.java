package hollstein.kim.polynom;

import java.util.Arrays;
import java.util.stream.Stream;

public class PolynomFaktor {
    private PolynomFaktor p1;
    private PolynomFaktor p2;
    private Polynom poly;

    public PolynomFaktor() {

    }

    public PolynomFaktor getP1() {
        return p1;
    }

    public PolynomFaktor getP2() {
        return p2;
    }

    public Polynom getPoly() {
        return poly;
    }

    public Polynom[] getPolys() {
        if (p1 != null && p2 != null) {
            return null;
        } else {
            return (new Polynom[]{poly});
        }
    }
}
