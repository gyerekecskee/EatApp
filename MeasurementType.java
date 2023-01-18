import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MeasurementType {
//TODO convert to enum
    private final String type;
    private final List<Measurement> measurements;

    public MeasurementType(String type) {
        this.type = type;
        measurements = new ArrayList<>(0);
    }

    public String getType() {
        return type;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void addMeasurement(Measurement measurement){
        if(!measurements.contains(measurement)){
            measurements.add(measurement);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasurementType that = (MeasurementType) o;
        return Objects.equals(type, that.type) && Objects.equals(measurements, that.measurements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, measurements);
    }

    @Override
    public String toString() {
        return type + "s: " + Catalog.listToFlatString(measurements);
    }
}
