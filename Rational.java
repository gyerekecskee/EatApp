public class Rational {
    private double nominator;
    private double denominator;

    /**
     * Constructor for rational.
     * @param nominator of the number
     * @param denominator of the number
     */
    public Rational(double nominator, double denominator){
        this.nominator = nominator;
        this.denominator = denominator;
    }

    /**
     * Calculates the value of the rational
     * @return nominator / denominator
     */
    public double value(){
        return nominator / denominator;
    }

    /**
     * Getter for nominator
     * @return the nominator
     */
    public double getNominator(){
        return nominator;
    }

    /**
     * returns the value of the number
     * @return a human-readable string
     */
    @Override
    public String toString() {
        return String.valueOf(value());
    }

    /**
     * Adds a number to the denominator
     * @param n a number
     */
    public void addDenominator(double n){
        denominator += n;
    }

    /**
     * Adds a number to the nominator
     * @param n a number
     */
    public void addNominator(double n){
        nominator += n;
    }

    /**
     * Getter for denominator
     * @return the denominator of the number
     */
    public double getDenominator() {
        return denominator;
    }
}
