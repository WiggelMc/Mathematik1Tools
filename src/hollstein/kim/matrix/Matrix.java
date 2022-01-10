package hollstein.kim.matrix;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Matrix {

    private int[][][] values;
    private String[] variables;
    private int basis;

    public Matrix(int[][][] values, String[] variables, int basis, boolean shiftVar) {
        this.values = values;
        this.variables = shiftVar ? shiftVariables(variables) : variables;
        this.basis = basis;
    }

    public int[][][] getValues() {
        return values;
    }

    public String[] getVariables() {
        return variables;
    }

    public int getBasis() {
        return basis;
    }

    public static int[] genVar(int x) throws IndexOutOfBoundsException {
        if (x < 1) {
            throw new IndexOutOfBoundsException();
        }
        int[] arr = new int[x+1];
        arr[x] = 1;
        return arr;
    }

    public String[] shiftVariables(String[] arr) {
        String[] newarr = new String[arr.length+1];
        System.arraycopy(arr, 0, newarr, 1, arr.length);
        return newarr;
    }

    public String setStringSize(String str, int length, boolean prepend) throws IllegalArgumentException {
        if (str.length() > length) {
            throw new IllegalArgumentException();
        }
        String padding = new String(new char[length - str.length()]).replace("\0", " ");
        if (prepend) {
            return padding + str;
        } else {
            return str + padding;
        }
    }

    public String getMatrixEntryString(int[] entry) {
        int entryLength = entry.length;
        int variableCount = variables.length;

        StringBuilder str = new StringBuilder();

        for (int i = 1; i < Math.min(entryLength,variableCount); i++) {
            if (entry[i] != 0) {
                if (entry[i] == 1) {
                    str.append(String.format("%s+",variables[i]));
                } else if (entry[i] == -1) {
                    str.append(String.format("-%s+",variables[i]));
                } else {
                        str.append(String.format("%d%s+",entry[i],variables[i]));
                }
            }
        }
        if ((entry[0] == 0) && (str.length() != 0)) {
            str.delete(str.length()-1,str.length());
        } else {
            str.append(entry[0]);
        }

        return str.toString().replace("+-","-");
    }

    public String getMatrixString(Operation[] operations) {
        int lengthX = values.length; //Row
        int lengthY = values[0].length; //Column
        int operationCount = operations != null ? operations.length : 0;
        int[] matrixNumMaxLengths = new int[lengthY];
        for (int i = 0; i < lengthY; i++) {
            matrixNumMaxLengths[i] = Math.max(2,getMaxNumLength(i));
        }
        int operatorMaxLength = 10;
        for (int i = 0; i < operationCount; i++) {
            int v = operations[i].getOperationString().length();
            if (v > operatorMaxLength) {
                operatorMaxLength = v;
            }
        }

        StringBuilder log = new StringBuilder();

        for (int i = 0; i < Math.max(lengthX,operationCount); i++) {
            if (i < operationCount) {
                log.append(String.format(" %s", setStringSize(operations[i].getOperationString(), operatorMaxLength +2, false)));
            } else {
                log.append(String.format(" %s", setStringSize("", operatorMaxLength +2, false)));
            }
            if (i < lengthX) {
                for (int j = 0; j < (lengthY-1); j++) {
                    log.append(String.format(" %s", setStringSize(getMatrixEntryString(values[i][j]), matrixNumMaxLengths[j] + 1, true)));
                }
                log.append(setStringSize(" |", 3, true));
                log.append(String.format(" %s", setStringSize(getMatrixEntryString(values[i][(lengthY - 1)]), matrixNumMaxLengths[lengthY-1] + 1, true)));
            }
            log.append("\n");
        }
        return log.toString();
    }

    public int getMaxNumLength(int column) {
        int length = values.length;
        int max = 0;

        for (int i = 0; i < length; i++) {
            int v = getMatrixEntryString(values[i][column]).length();
            if (v > max) {
                max = v;
            }
        }
        return max;
    }

    public Matrix operate(Operation[] operations) throws IllegalAccessException {
        int lengthX = values.length; //Row
        int lengthY = values[0].length; //Column
        int operationCount = operations.length;
        OperationState[] operationStates = new OperationState[lengthX];

        int[][][] newarr = new int[lengthX][lengthY][];

        for (int i = 0; i < operationCount; i++) {
            OperationType operationType = operations[i].getType();
            int v1 = operations[i].getV1() -1;
            int v2 = operations[i].getV2() -1;
            int factor = operations[i].getFactor();
            switch (operationType) {
                case FACTOR:
                    if (operationStates[v1] != null) {
                        throw new IllegalAccessException();
                    }
                    for (int j = 0; j < lengthY; j++) {
                        newarr[v1][j] = factorArrayEntry(values[v1][j],factor);
                    }
                    operationStates[v1] = OperationState.WRITE;
                    break;
                case SWAP:
                    if ((operationStates[v1] != null) || (operationStates[v2] != null)) {
                        throw new IllegalAccessException();
                    }
                    for (int j = 0; j < lengthY; j++) {
                        newarr[v1][j] = values[v2][j];
                        newarr[v2][j] = values[v1][j];
                    }
                    operationStates[v1] = OperationState.WRITE;
                    operationStates[v2] = OperationState.WRITE;
                    break;
                case ADD:
                    if ((operationStates[v1] != null) || (operationStates[v2] == OperationState.WRITE)) {
                        throw new IllegalAccessException();
                    }
                    for (int j = 0; j < lengthY; j++) {
                        newarr[v1][j] = addArrayEntry(values[v1][j],values[v2][j],factor);
                    }
                    operationStates[v1] = OperationState.WRITE;
                    operationStates[v2] = OperationState.READ;
                    break;
                case SUBTRACT:
                    //noinspection Duplicates
                    if ((operationStates[v1] != null) || (operationStates[v2] == OperationState.WRITE)) {
                        throw new IllegalAccessException();
                    }
                    for (int j = 0; j < lengthY; j++) {
                        newarr[v1][j] = subtractArrayEntry(values[v1][j],values[v2][j],factor);
                    }
                    operationStates[v1] = OperationState.WRITE;
                    operationStates[v2] = OperationState.READ;
            }
        }
        for (int i = 0; i < lengthX; i++) {
            if (operationStates[i] != OperationState.WRITE) {
                for (int j = 0; j < lengthY; j++) {
                    int entryLength = values[i][j].length;
                    newarr[i][j] = new int[entryLength];
                    System.arraycopy(values[i][j], 0, newarr[i][j], 0, entryLength);
                }
            }
        }

        int[][][] newarrb = convertBasis(newarr, basis);
        return new Matrix(newarrb,variables, basis, false);
    }

    public static int[] factorArrayEntry(int[] entry, int factor) {
        int length = entry.length;
        int[] newarr = new int[length];

        for (int i = 0; i < length; i++) {
            newarr[i] = entry[i] * factor;
        }
        return newarr;
    }

    public static int[] addArrayEntry(int[] entry, int[] src, int factor) {
        int entryLength = entry.length;
        int srcLength = src.length;
        int maxLength = Math.max(entryLength, srcLength);
        int[] newarr = new int[maxLength];

        for (int i = 0; i < maxLength; i++) {
            int v1 = (i < entryLength) ? entry[i] : 0;
            int v2 = (i < srcLength) ? src[i] : 0;
            newarr[i] = v1 + (factor*v2);
        }
        return newarr;
    }

    public static int[] subtractArrayEntry(int[] entry, int[] src, int factor) {
        int entryLength = entry.length;
        int srcLength = src.length;
        int maxLength = Math.max(entryLength, srcLength);
        int[] newarr = new int[maxLength];

        for (int i = 0; i < maxLength; i++) {
            int v1 = (i < entryLength) ? entry[i] : 0;
            int v2 = (i < srcLength) ? src[i] : 0;
            newarr[i] = v1 - (factor*v2);
        }
        return newarr;
    }

    public static int[][][] convertBasis(int[][][] arr, int basis) {
        if (basis == 0) {
            return arr;
        }
        int lengthX = arr.length; //Row
        int lengthY = arr[0].length; //Column
        int[][][] newarr = new int[lengthX][lengthY][];

        for (int i = 0; i < lengthX; i++) {
            for (int j = 0; j < lengthY; j++) {
                newarr[i][j] = convertEntryBasis(arr[i][j], basis);
            }
        }
        return newarr;
    }

    public static int[] convertEntryBasis(int[] entry, int basis) {
        int entryLength = entry.length;
        int[] newarr = new int[entryLength];
        for (int i = 0; i < entryLength; i++) {
            newarr[i] = Math.floorMod(entry[i],basis);
        }
        return newarr;
    }

    public MatrixSolve solveMatrix(Scanner scanner) {
        boolean prompt = true;
        ArrayList<Matrix> matrizen = new ArrayList<>();
        ArrayList<String> log = new ArrayList<>();
        ArrayList<String> inputs = new ArrayList<>();
        Matrix currentMatrix = this;

        String prefix = currentMatrix.getBasis() != 0 ? String.format("\n[in Z%d]\n", currentMatrix.getBasis()) : "\n[in R]\n";
        String str1 = prefix + currentMatrix.getMatrixString(null);
        System.out.println(str1);
        log.add(str1);
        matrizen.add(currentMatrix);

        System.out.print("Input >");
        while (prompt) {
            try {
                String input = scanner.nextLine();
                if (input.trim().charAt(0) == 'e') {
                    prompt = false;
                } else if (input.trim().charAt(0) == 'u' && matrizen.size() > 1) {
                    matrizen.remove(matrizen.size() -1);
                    log.remove(log.size() -1);
                    inputs.remove(inputs.size() -1);
                    currentMatrix = matrizen.get(matrizen.size() -1);
                    String str2 = currentMatrix.getMatrixString(null);
                    System.out.println(str2);
                    System.out.print("Input >");
                } else {
                    Operation[] operations = currentMatrix.interpretOperations(input);
                    Matrix newMatrix = currentMatrix.operate(operations);

                    inputs.add(input);
                    String str2 = newMatrix.getMatrixString(operations);
                    System.out.println(str2);
                    log.add(str2);
                    matrizen.add(newMatrix);

                    currentMatrix = newMatrix;
                    System.out.print("Input >");
                }
            } catch (InputMismatchException | IllegalAccessException e) {
                System.out.print("Invalid Input >");
            }
        }

        StringBuilder logS = new StringBuilder();

        int logSize = log.size();
        for (int i = 0; i < logSize; i++) {
            logS.append(log.get(i)+"\n");
        }
        logS.append("\nInput Log:\n");
        int inputSize = inputs.size();
        for (int i = 0; i < inputSize; i++) {
            logS.append(inputs.get(i)+"\n");
        }
        System.out.println(logS.toString());
        return new MatrixSolve( currentMatrix, logS.toString());
    }

    public Operation[] interpretOperations(String str) throws InputMismatchException {
        int lengthX = values.length; //Row

        String[] exp = str.split(",");
        if (exp.length == 0) {
            throw new InputMismatchException();
        }
        Operation[] operations = new Operation[exp.length];
        for (int i = 0; i < exp.length; i++) {
            String op = exp[i].replaceAll("\\s+","");
            int v1 = 1;
            int v2 = 1;
            int factor = 1;
            OperationType type;

            int[] numbers = new int[8];
            Matcher m = Pattern.compile("(\\d+)").matcher(op);
            try {
                int j = 0;
                while (m.find()) {
                    numbers[j] = Integer.parseInt(m.group());
                    j++;
                }
            } catch (Exception e) {
                throw new InputMismatchException();
            }

            //FACTOR: rD*D // rD*!cDvD
            //ADD: rD+DrD // rD+rD // rD+rD!cDvD
            //SWAP rD<>rD
            //SUB: rD-DrD // rD-rD // rD-rD!cDvD
            if (op.matches("r(\\d+)\\*(\\d+)")) { //rD*D
                type = OperationType.FACTOR;
                v1 = numbers[0];
                factor = numbers[1];

            } else if (op.matches("r(\\d+)\\*!c(\\d+)v(\\d+)")) { //rD*!cDvD
                type = OperationType.FACTOR;
                v1 = numbers[0];
                int column = numbers[1];
                int value = numbers[2];
                int v1val;
                try {
                    v1val = values[v1-1][column-1][0];
                } catch (Exception e) {
                    throw new InputMismatchException();
                }
                boolean error = true;
                for (int j = 1; j < basis; j++) {
                    if (Math.floorMod(v1val * j, basis) == value) {
                        factor = j;
                        error = false;
                        break;
                    }
                }
                if (error) {
                    throw new InputMismatchException();
                }
                //Dynamic Factor

            } else if (op.matches("r(\\d+)\\+(\\d+)r(\\d+)")) { //rD+DrD
                type = OperationType.ADD;
                v1 = numbers[0];
                factor = numbers[1];
                v2 = numbers[2];

            } else if (op.matches("r(\\d+)\\+r(\\d+)")) { //rD+rD
                type = OperationType.ADD;
                v1 = numbers[0];
                v2 = numbers[1];

            } else if (op.matches("r(\\d+)\\+r(\\d+)!c(\\d+)v(\\d+)")) { //rD+rD!cDvD
                type = OperationType.ADD;
                v1 = numbers[0];
                v2 = numbers[1];
                int column = numbers[2];
                int value = numbers[3];
                int v1val;
                int v2val;
                try {
                    v1val = values[v1-1][column-1][0];
                    v2val = values[v2-1][column-1][0];
                } catch (Exception e) {
                    throw new InputMismatchException();
                }
                boolean error = true;
                for (int j = 1; j < basis; j++) {
                    if (Math.floorMod(v1val + j*v2val, basis) == value) {
                        factor = j;
                        error = false;
                        break;
                    }
                }
                if (error) {
                    throw new InputMismatchException();
                }

            } else if (op.matches("r(\\d+)<>r(\\d+)")) { //rD<>rD
                type = OperationType.SWAP;
                v1 = numbers[0];
                v2 = numbers[1];

            } else if (op.matches("r(\\d+)-(\\d+)r(\\d+)")) { //rD-DrD
                type = OperationType.SUBTRACT;
                v1 = numbers[0];
                factor = numbers[1];
                v2 = numbers[2];

            } else if (op.matches("r(\\d+)-r(\\d+)")) { //rD-rD
                type = OperationType.SUBTRACT;
                v1 = numbers[0];
                v2 = numbers[1];

            } else if (op.matches("r(\\d+)-r(\\d+)!c(\\d+)v(\\d+)")) { //rD-rD!cDvD
                type = OperationType.SUBTRACT;
                v1 = numbers[0];
                v2 = numbers[1];
                int column = numbers[2];
                int value = numbers[3];
                int v1val;
                int v2val;
                try {
                    v1val = values[v1-1][column-1][0];
                    v2val = values[v2-1][column-1][0];
                } catch (Exception e) {
                    throw new InputMismatchException();
                }
                boolean error = true;
                for (int j = 1; j < basis; j++) {
                    if (Math.floorMod(v1val - j*v2val, basis) == value) {
                        factor = j;
                        error = false;
                        break;
                    }
                }
                if (error) {
                    throw new InputMismatchException();
                }

            } else {
                throw new InputMismatchException();
            }

            if ((basis != 0 && Math.floorMod(factor, basis) == 0) || factor == 0|| v1 > lengthX || v2 > lengthX || v1 < 1 || v2 < 1 || (v1 == v2 && type != OperationType.FACTOR)) {
                throw new InputMismatchException();
            }

            Operation operation = new Operation(type, v1, v2, factor);
            operations[i] = operation;
        }
        return operations;
    }
}
