package hollstein.kim.polynom;

public class PolynomPotenz {
    private final Polynom polynom;
    private final int potenz;

    public PolynomPotenz(Polynom polynom, int potenz) {
        this.polynom = polynom;
        this.potenz = potenz;
    }

    public Polynom getPolynom() {
        return polynom;
    }

    public int getPotenz() {
        return potenz;
    }
}
