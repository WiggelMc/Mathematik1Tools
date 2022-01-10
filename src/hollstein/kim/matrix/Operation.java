package hollstein.kim.matrix;

public class Operation {

    public static String[] romanNumerals = {
            "0",
            "I",
            "II",
            "III",
            "IV",
            "V",
            "VI",
            "VII",
            "VIII",
            "IX",
            "X",
            "XI",
            "XII",
    };

    private OperationType type;
    private int v1;
    private int v2;
    private int factor;

    public Operation(OperationType type, int v1, int v2, int factor) {
        this.v1 = v1;
        this.v2 = v2;
        this.factor = factor;
        this.type = type;
    }

    public String getOperationString() {
        String oStr = romanNumerals[v1];
        switch (type) {
            case ADD:
                if (factor == 1) {
                    oStr += String.format(" +%s",romanNumerals[v2]);
                } else {
                    oStr += String.format(" +%d*%s",factor,romanNumerals[v2]);
                }
                break;
            case SWAP:
                oStr += String.format(" <> %s",romanNumerals[v2]);
                break;
            case FACTOR:
                oStr += String.format(" *%s",factor);
                break;
            case SUBTRACT:
                if (factor == 1) {
                    oStr += String.format(" -%s",romanNumerals[v2]);
                } else {
                    oStr += String.format(" -%d*%s",factor,romanNumerals[v2]);
                }
        }
        return oStr;
    }

    public int getV1() {
        return v1;
    }

    public int getV2() {
        return v2;
    }

    public int getFactor() {
        return factor;
    }

    public OperationType getType() {
        return type;
    }
}