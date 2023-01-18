import java.util.NoSuchElementException;
import java.util.Objects;


/**
 * @param multiplier base * multiplier = name. 0.001 kg = g
 */
public record Measurement(String name, double multiplier) {
    /**
     * Equals method
     * @param o   the reference object with which to compare.
     * @return whether the two objects are equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measurement unit = (Measurement) o;
        return Objects.equals(name, unit.name);
    }

    /**
     * toString method
     * @return a human-readable string
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Decides whether two measurements are of the same type
     * @param measurement the measurement that we compare to this
     * @return true if from same type, false otherwise
     */
    public boolean sameTypeAs(Measurement measurement){
        return Main.getCatalog().getMeasurementType(this)
            .equals(Main.getCatalog().getMeasurementType(measurement));
    }

    public static Measurement parseMeasurement(String input){
        return Main.getCatalog().getMeasurement(input);
    }

    public Measurement nextMeasurement(){
        Measurement nextMeasurement = new Measurement("temp", Double.MAX_VALUE);
        for (Measurement measurement :
            Main.getCatalog().getMeasurementType(this).getMeasurements()){
            if (measurement.multiplier < multiplier && measurement.multiplier <
                nextMeasurement.multiplier){
                nextMeasurement = measurement;
            }
        }
        if (nextMeasurement.multiplier != Double.MAX_VALUE)
            return nextMeasurement;
        throw new NoSuchElementException();
    }

    public Measurement previousMeasurement(){
        Measurement previousMeasurement = new Measurement("temp", Double.MIN_VALUE);
        for (Measurement measurement :
            Main.getCatalog().getMeasurementType(this).getMeasurements()){
            if (measurement.multiplier > multiplier && measurement.multiplier <
                previousMeasurement.multiplier){
                previousMeasurement = measurement;
            }
        }
        if (previousMeasurement.multiplier != Double.MIN_VALUE)
            return previousMeasurement;
        throw new NoSuchElementException();
    }

}
