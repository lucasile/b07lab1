import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Driver {
    public static void main(String [] args) {

        //Polynomial p1 = new Polynomial(new double[]{5, -2, 3, 1}, new int[]{0, 2, 3, 4});
        //printPolynomial(p1);
        //System.out.println("\n\n");

        //Polynomial p2 = new Polynomial(new double[]{2, 1, -6, 1}, new int[]{0, 2, 3, 5});

        //Polynomial p1plusp2 = p1.add(p2);

        //printPolynomial(p1plusp2);

        //Polynomial p1timesp2 = p1.multiply(p2);

        //printPolynomial(p1timesp2);

        //try {
            //p1timesp2.saveToFile("test.txt");
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}

        File polynomialFile = new File("./polynomial.txt");

        try {
            Polynomial importedPolynomial = new Polynomial(polynomialFile);
            printPolynomial(importedPolynomial);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    private static void printPolynomial(Polynomial polynomial) {
        for (int i = 0; i < polynomial.getExponents().length; i++) {
            System.out.println(polynomial.getCoefficients()[i] + "x^" + polynomial.getExponents()[i]);
        }
    }

}