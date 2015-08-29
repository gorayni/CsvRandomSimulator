package com.csvsim.random;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.random.RandomGenerator;

import com.csvsim.random.utils.Utils;

public class DoubleDistribution implements Distribution<Double> {

	private RandomGenerator randomGenerator;
	
	private NavigableMap<Double, Category<Double, Double>> categoriesByCumProbability = null;

  private void init(RandomGenerator randomGenerator, Vector<Double> numbers, Vector<Double> cumulativeProbability) {
	this.randomGenerator=randomGenerator;
    this.categoriesByCumProbability = new TreeMap<Double, Category<Double, Double>>();
    for (int i = 1; i < numbers.size(); i++) {
      Double binSize = numbers.get(i) - numbers.get(i - 1);
      Category<Double, Double> category = new Category<Double, Double>(numbers.get(i - 1), binSize);
      this.categoriesByCumProbability.put(cumulativeProbability.get(i - 1), category);
    }
  }

  public DoubleDistribution(RandomGenerator randomGenerator, String serializedDefinitions) {
	if(serializedDefinitions.contains(";")) {	  
	    String[] doublesStr = StringUtils.substringBefore(serializedDefinitions, ";").split(",");
	    DoubleRange range = new DoubleRange(new Double(doublesStr[0]), new Double(doublesStr[1]));
	
	    String[] valuesStr = StringUtils.substringAfter(serializedDefinitions, ";").split(",");
	    Vector<Double> values = new Vector<Double>();
	    for (String valueStr : valuesStr) {
	      values.add(new Double(valueStr));
	    }
	
	    if (range.getMaximumDouble() - range.getMinimumDouble() > 0) {
	      Vector<Double> numbers = Utils.toVector(range, values.size());
	      Vector<Double> cumulative = DoubleDistribution.createCumulativeVector(numbers, values);
	      this.init(randomGenerator, numbers, cumulative);
	    } else {
	      Vector<Double> numbers = new Vector<Double>();
	      Vector<Double> cumulative = new Vector<Double>();
	      numbers.add(range.getMinimumDouble());
	      cumulative.add(1.0);
	      this.init(randomGenerator, numbers, cumulative);
	    }
	}
	else {
	    String[] doublesStr = serializedDefinitions.split(",");	    
	    Vector<Double> numbers = new Vector<Double>(2);
	    numbers.add(new Double(doublesStr[0]));
	    numbers.add(new Double(doublesStr[1]));
	    Vector<Double> cumulativeProbability = new Vector<Double>(1);
	    cumulativeProbability.add(1.);	   
	    this.init(randomGenerator, numbers, cumulativeProbability);		
	}
  }

  private static Vector<Double> createCumulativeVector(Vector<Double> numbers, Vector<Double> values) {
    TreeMap<Double, Double> points = Utils.toPoints(values);
    PolynomialSplineFunction splineFunction = Utils.computeSplineFunction(points);

    double minNum = numbers.firstElement().doubleValue();
    double maxNum = numbers.lastElement().doubleValue() - minNum;

    int numBins = numbers.size() - 1;
    Vector<Double> binValues = new Vector<Double>(numBins);
    double sumBinValue = 0.0;
    for (int i = 1; i <= numBins; i++) {
      double x = ((numbers.get(i).doubleValue() + numbers.get(i - 1).doubleValue()) / 2 - minNum) / maxNum;
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

  @Override
  public Double sample() {
	if(this.categoriesByCumProbability.size() > 1) {	  
	    double uniformDistributionSample = randomGenerator.nextDouble();
	    Double key = this.categoriesByCumProbability.ceilingKey(uniformDistributionSample);
	    Category<Double, Double> category = this.categoriesByCumProbability.get(key);
	    return category.getValue() + (randomGenerator.nextDouble() * category.getBinSize());
	}
	else {
		Category<Double, Double> category = this.categoriesByCumProbability.values().iterator().next();	
		return category.getValue() + (randomGenerator.nextDouble() * category.getBinSize());
	}
  }
}