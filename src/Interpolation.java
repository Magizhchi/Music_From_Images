import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Interpolation {

//    public List<Float> interpolateValuesNewton(List<Float> dataPoints, List<Float> interpolationPoints){
//        List<Float> interpolatedValues = new ArrayList<>();
//        Float[] dividedDifference = new Float[dataPoints.size()];
//        if(checkAllValuesAreDifferent(dataPoints)){
//            float psum = dataPoints.get(0);
//            for (int i = 1; i < dataPoints.size(); i++) {
//                float pprod = 1;
//                float pexpr = 1;
//                for (int j = 0; j < i-1; j++) {
//                    pexpr *= (5-dataPoints.get(j));
//                    pprod *= (dataPoints.get(i) - dataPoints.get(j));
//                }
//                dataPoints
//
//            }
//        }
//        return interpolatedValues;
//    }

    public Boolean checkAllValuesAreDifferent(List<Double> dataPoints){
        return dataPoints.size() != dataPoints.stream().distinct().count();
    }

    public List<Double> interpolateAllValues(List<Double> dataPoints, List<Double> interpolationPoints){
        return interpolationPoints.stream()
                                  .map(value -> interpolateValuesLagrange(dataPoints, value, getStartValue(value)))
                                  .collect(Collectors.toList());
    }

    private Double getStartValue(Double value) {
        return (double) (value.intValue() - 1);
    }

    private Double interpolateValuesLagrange(List<Double> dataPoints, Double interpolationPoints, Double startValue){
        Double fsum = 0.0;
        int start = startValue.intValue();
        for (int i = start; i < start +4; i++) {
            Double fProd = dataPoints.get(i);
            for (int j = start; j < start +4; j++) {
                if(i != j)
                    fProd *= (interpolationPoints - j) / (i-j);
            }
            fsum += fProd;
        }
        return fsum;
    }


    public List<Double> getDataPointsForInterpolation(Double totalNumberOfPoints, int requiredPoints){
        Double incrementFactor = totalNumberOfPoints/requiredPoints;
        Double currentValue = 1.0;
        List<Double> dataPoints = new ArrayList<>();
        dataPoints.add(currentValue);
        do {
            currentValue += incrementFactor;
            dataPoints.add(currentValue);
        }while (currentValue < totalNumberOfPoints - incrementFactor);

        return dataPoints;
    }
}
