package hollstein.kim.polynom;

public class PolynomDivision {
    private final Polynom ergebnis;
    private final Polynom rest;
    private final String log;

    public PolynomDivision(Polynom ergebnis, Polynom rest, String log) {
        this.ergebnis = ergebnis;
        this.rest = rest;
        this.log = log;
    }

    public Polynom getErgebnis() {
        return ergebnis;
    }

    public Polynom getRest() {
        return rest;
    }

    public String getLog() {
        return log;
    }
}
