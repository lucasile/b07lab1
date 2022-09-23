public class Polynomial {

    private double[] coefficients;

    public Polynomial() {
        coefficients = new double[] {0.0};
    }

    public Polynomial(double[] coefficients) {
        this.coefficients = coefficients.clone();
    }

    public Polynomial add(Polynomial polynomial) {

        double[] otherCoefficients = polynomial.getCoefficients();

        double[] newCoefficients;
        double[] toAdd;

        if (otherCoefficients.length > coefficients.length) {
            newCoefficients = otherCoefficients.clone();
            toAdd = coefficients;
        } else {
            newCoefficients = coefficients.clone();
            toAdd = otherCoefficients;
        }

        for (int i = 0; i < toAdd.length; i++)
            newCoefficients[i] += toAdd[i];

        return new Polynomial(newCoefficients);
    }

    public double evaluate(double input) {

        double result = 0.0;

        for (int i = 0; i < coefficients.length; i++)
            result += coefficients[i] * Math.pow(input, i);

        return result;
    }

    public boolean hasRoot(double input) {
        return evaluate(input) == 0.0;
    }

    public double[] getCoefficients() {
        return coefficients;
    }
}

