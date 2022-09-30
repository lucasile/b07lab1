import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Polynomial {

    private double[] coefficients;
    private int[] exponents;

    public Polynomial() {
        coefficients = new double[] {0.0};
        exponents = new int[] {0};
    }

    public Polynomial(double[] coefficients, int[] exponents) {
        this.coefficients = coefficients.clone();
        this.exponents = exponents.clone();
    }

    public Polynomial(File file) throws FileNotFoundException {

        Scanner scanner = new Scanner(file);

        String polynomialString = null;

        if (scanner.hasNextLine())
            polynomialString = scanner.nextLine();

        scanner.close();

        if (polynomialString == null) {
            coefficients = new double[]{0.0};
            exponents = new int[] {0};
            return;
        }

        createFromString(polynomialString);

    }

    public Polynomial add(Polynomial polynomial) {

        Map<Integer, Double> expToCoeff = new HashMap<>();

        int[] otherExponents = polynomial.getExponents();
        double[] otherCoefficients = polynomial.getCoefficients();

        for (int i = 0; i < otherExponents.length; i++) {
            if (expToCoeff.containsKey(otherExponents[i]))
                expToCoeff.put(otherExponents[i], expToCoeff.get(otherExponents[i]) + otherCoefficients[i]);
            else
                expToCoeff.put(otherExponents[i], otherCoefficients[i]);
        }

        for (int i = 0; i < exponents.length; i++) {
            if (expToCoeff.containsKey(exponents[i]))
                expToCoeff.put(exponents[i], expToCoeff.get(exponents[i]) + coefficients[i]);
            else
                expToCoeff.put(exponents[i], coefficients[i]);
        }

        return createPolynomialFromMap(expToCoeff);
    }

    public Polynomial multiply(Polynomial polynomial) {

        Map<Integer, Double> expToCoeff = new HashMap<>();

        double[] otherCoefficients = polynomial.getCoefficients();
        int[] otherExponents = polynomial.getExponents();

        for (int i = 0; i < exponents.length; i++) {

            double coefficient = coefficients[i];
            int exponent = exponents[i];

            for (int j = 0; j < otherExponents.length; j++) {

                double otherCoefficient = otherCoefficients[j];
                int otherExponent = otherExponents[j];

                double newCoefficient = coefficient * otherCoefficient;
                int newExponent = exponent + otherExponent;

                if (expToCoeff.containsKey(newExponent))
                    expToCoeff.put(newExponent, expToCoeff.get(newExponent) + newCoefficient);
                else
                    expToCoeff.put(newExponent, newCoefficient);

            }

        }

        // purge zeroes from the map
        // would use iterator, but we haven't learned that yet

        List<Integer> keysToRemove = new ArrayList<>();

        for (Map.Entry<Integer, Double> entry : expToCoeff.entrySet()) {
            if (entry.getValue() == 0)
                keysToRemove.add(entry.getKey());
        }

        for (Integer key : keysToRemove)
            expToCoeff.remove(key);

        return createPolynomialFromMap(expToCoeff);
    }

    public double evaluate(double input) {

        double result = 0.0;

        for (int i = 0; i < coefficients.length; i++)
            result += coefficients[i] * Math.pow(input, exponents[i]);

        return result;
    }

    public boolean hasRoot(double input) {
        return evaluate(input) == 0.0;
    }

    public void saveToFile(String fileName) throws IOException {

        StringBuilder polynomialString = new StringBuilder();

        for (int i = 0; i < coefficients.length; i++) {

            double coefficient = coefficients[i];
            int exponent = exponents[i];

            if (coefficient != 1)
                polynomialString.append(coefficient);

            if (exponent != 0) {
                polynomialString.append("x");
                if (exponent != 1)
                    polynomialString.append(exponent);
            }

            if (i + 1 != coefficients.length && coefficients[i + 1] > 0)
                polynomialString.append("+");

        }

        File file = new File(fileName);

        // will create new file iff it doesn't exist... don't care about the return type
        file.createNewFile();

        FileWriter fileWriter = new FileWriter(file, false);

        fileWriter.write(polynomialString.toString());
        fileWriter.close();

    }

    private void createFromString(String polynomialString) {

        List<String> terms = new ArrayList<>();
        char[] stringCharacters = polynomialString.toCharArray();

        int i = 1;
        int length = stringCharacters.length;

        // I would use regex, but we haven't learned that in class

        StringBuilder lastTerm = new StringBuilder(String.valueOf(stringCharacters[0]));

        while (i < length) {

            char character = stringCharacters[i];

            if (character == '+' || character == '-' || i + 1 == length) {
                if (i + 1 == length)
                    lastTerm.append(character);
                terms.add(lastTerm.toString());
                lastTerm = new StringBuilder(String.valueOf(character));
                i++;
                continue;
            }

            lastTerm.append(character);

            i++;
        }

        coefficients = new double[terms.size()];
        exponents = new int[terms.size()];

        for (int j = 0; j < terms.size(); j++) {

            String term = terms.get(j);

            String sanitized = term.replaceAll("\\+", "");
            String[] splitTerm = sanitized.split("x");

            // first term is x, then first element in array will be ""
            if (splitTerm[0].equals(""))
                coefficients[j] = 1.0;
            else
                coefficients[j] = Double.parseDouble(splitTerm[0]);

            // then we have some coefficient followed by just x, like 5x, or just 5 -> default exponent to 1
            if (splitTerm.length == 1) {
                if (splitTerm[0].equals(sanitized))
                    exponents[j] = 0;
                else
                    exponents[j] = 1;
            } else
                exponents[j] = Integer.parseInt(splitTerm[1]);

        }

    }

    private Polynomial createPolynomialFromMap(Map<Integer, Double> map) {

        int size = map.size();

        int[] newExponents = new int[size];
        double[] newCoefficients = new double[size];

        int i = 0;
        for (int exponentKey : map.keySet()) {
            newExponents[i] = exponentKey;
            newCoefficients[i] = map.get(exponentKey);
            i++;
        }

        return new Polynomial(newCoefficients, newExponents);

    }

    public int[] getExponents() {
        return exponents;
    }

    public double[] getCoefficients() {
        return coefficients;
    }
}

