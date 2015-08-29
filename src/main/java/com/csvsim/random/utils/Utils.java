package com.csvsim.random.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public class Utils {

  private static Utils utils = null;

  private Utils() {
  }

  public static Utils instance() {
    if (Utils.utils == null) {
      Utils.utils = new Utils();
    }
    return Utils.utils;
  }

  public static TreeMap<Double, Double> toPoints(Vector<Double> values) {
    TreeMap<Double, Double> points = new TreeMap<Double, Double>();
    Double i = 0.0;
    double increment = 1.0 / values.size();
    for (Double value : values) {
      points.put(i, value);
      i += increment;
    }
    return points;
  }


  public static Vector<Double> toVector(DoubleRange range, int numBins) {
    if (numBins <= 0) {
      throw new RuntimeException("The number of bins must be positive");
    }
    double increment = (range.getMaximumDouble() - range.getMinimumDouble()) / numBins;
    Vector<Double> rangeVector = new Vector<Double>();
    for (double i = range.getMinimumDouble(); i < range.getMaximumDouble(); i += increment) {
      rangeVector.add(i);
    }
    rangeVector.add(range.getMaximumDouble());
    return rangeVector;
  }

  public static Vector<BigDecimal> toBigDecimal(Vector<Double> doubleVector) {
    Vector<BigDecimal> bigDecimalVector = null;
    if (doubleVector != null) {
      bigDecimalVector = new Vector<BigDecimal>();
      for (Double d : doubleVector) {
        bigDecimalVector.add(new BigDecimal(d));
      }
    }
    return bigDecimalVector;
  }

  public static List<BigInteger> toBigInteger(List<Integer> integerVector) {
    List<BigInteger> bigIntegerVector = null;
    if (integerVector != null) {
      bigIntegerVector = new ArrayList<BigInteger>();
      for (Integer i : integerVector) {
        bigIntegerVector.add(BigInteger.valueOf(i));
      }
    }
    return bigIntegerVector;
  }

  public static PolynomialSplineFunction computeSplineFunction(TreeMap<Double, Double> points) {
    // Setting the range between 0 and 1
    Double minValue = points.firstKey();
    Double maxValue = points.lastKey() - minValue;
    TreeMap<Double, Double> pointsInRange = new TreeMap<Double, Double>();
    for (Entry<Double, Double> p : points.entrySet()) {
      if (p.getValue() < 0) {
        throw new RuntimeException("y axis value can not less than zero");
      }
      pointsInRange.put((p.getKey() - minValue) / maxValue, p.getValue());
    }

    Double[] x = new Double[pointsInRange.keySet().size()];
    Double[] y = new Double[pointsInRange.values().size()];
    int i = 0;
    for (Entry<Double, Double> e : pointsInRange.entrySet()) {
      x[i] = e.getKey().doubleValue();
      y[i] = e.getValue().doubleValue();
      i++;
    }
    
    SplineInterpolator splineInterpolator = new SplineInterpolator();
    return splineInterpolator.interpolate(ArrayUtils.toPrimitive(x), ArrayUtils.toPrimitive(y));
  }

  public static List<Double> createCumulativeVector(List<Date> dates, TreeMap<Double, Double> points) {
    PolynomialSplineFunction splineFunction = Utils.computeSplineFunction(points);
    return Utils.createCumulativeVector(dates, splineFunction);
  }

  private static List<Double> createCumulativeVector(List<Date> dates, PolynomialSplineFunction splineFunction) {

    int numBins = dates.size() - 1;
    Vector<Double> binValues = new Vector<Double>(numBins);
    double sumBinValue = 0.0;
    double increment = 1.0 / numBins;
    for (int i = 1; i <= numBins; i++) {
      double x = i * increment;
      double binValue = splineFunction.value(x);
      binValues.add(binValue);
      sumBinValue += binValue;
    }

    Vector<Double> cumBinValue = new Vector<Double>(numBins);
    double cumSum = 0.0;
    for (Double binValue : binValues) {
      cumSum += binValue;
      cumBinValue.add(cumSum / sumBinValue);
    }
    return cumBinValue;
  }

  public static TreeMap<String, Double> createStringMap(String serializedDefinitions) {
    TreeMap<String, Double> map = new TreeMap<String, Double>();
    String[] definitions = serializedDefinitions.split(";");
    for (String entry : definitions) {
      int index = entry.lastIndexOf(',');
      map.put(entry.substring(0, index), Double.valueOf(entry.substring(index + 1)));
    }
    return map;
  }

  public static interface checkNullStringMethod {
    public String get(Object argument);
  }
}
