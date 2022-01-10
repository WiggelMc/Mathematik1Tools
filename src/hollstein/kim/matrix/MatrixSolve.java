package hollstein.kim.matrix;

public class MatrixSolve {

    private Matrix ergebnis;
    private String log;

    public MatrixSolve(Matrix ergebnis, String log) {
        this.ergebnis = ergebnis;
        this.log = log;
    }

    public Matrix getErgebnis() {
        return ergebnis;
    }

    public String getLog() {
        return log;
    }
}
