package com.csvsim.random;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.random.RandomGenerator;

import com.csvsim.random.utils.Utils;

public class IntegerDistribution implements Distribution<Integer> {

  private RandomGenerator randomGenerator;
  
  private NavigableMap<Double, Category<Double, Double>> categoriesByCumProbability = null;
  
  private boolean useAllPoints = false;
  
  private void init(RandomGenerator randomGenerator, Vector<Double> numbers, Vector<Double> cumulativeProbability, boolean useAllPoints) {
    this.randomGenerator = randomGenerator;

    this.categoriesByCumProbability = new TreeMap<Double, Category<Double, Double>>();
    for (int i = 1; i < numbers.size(); i++) {
      Double binSize = numbers.get(i) - numbers.get(i - 1);
      Category<Double, Double> category = new Category<Double, Double>(numbers.get(i - 1), binSize);
      this.categoriesByCumProbability.put(cumulativeProbability.get(i - 1), category);
    }
   
    this.useAllPoints = useAllPoints;
  }

  public IntegerDistribution(RandomGenerator randomGenerator, String serializedDefinitions) {
	  this(randomGenerator, serializedDefinitions, false);
  }
  
  public IntegerDistribution(RandomGenerator randomGenerator, String serializedDefinitions, boolean useAllPoints) {
	if(serializedDefinitions.contains(";"))
	{
	    String[] doublesStr = StringUtils.substringBefore(serializedDefinitions, ";").split(",");
	    DoubleRange range = new DoubleRange(new Integer(doublesStr[0])-0.5, new Integer(doublesStr[1])+0.5);
	
	    String[] valuesStr = StringUtils.substringAfter(serializedDefinitions, ";").split(",");
	    Vector<Double> values = new Vector<Double>();
	    for (String valueStr : valuesStr) {
	      values.add(new Double(valueStr));
	    }
	
	    Vector<Double> numbers = null;
	    if(useAllPoints) {
	    	numbers = Utils.toVector(range, range.getMaximumInteger()-range.getMinimumInteger());	   
	    }
	    else {
	    	numbers = Utils.toVector(range, values.size());
	    	useAllPoints = range.getMaximumInteger()-range.getMinimumInteger()==values.size();
	    }
	    	    	
	    
	    Vector<Double> cumulativeProbability = IntegerDistribution.createCumulativeVector(numbers, values);
	    this.init(randomGenerator, numbers, cumulativeProbability, useAllPoints);
	}
	else {
	    String[] doublesStr = serializedDefinitions.split(",");	    
	    Vector<Double> numbers = new Vector<Double>(2);
	    numbers.add(new Double(doublesStr[0])-0.5);
	    numbers.add(new Double(doublesStr[1])+0.5);
	    Vector<Double> cumulativeProbability = new Vector<Double>(1);
	    cumulativeProbability.add(1.);	   
	    this.init(randomGenerator, numbers, cumulativeProbability, false);
	}
  }

  private static Vector<Double> createCumulativeVector(Vector<Double> numbers, Vector<Double> values) {

    TreeMap<Double, Double> points = Utils.toPoints(values);
    PolynomialSplineFunction splineFunction = Utils.computeSplineFunction(points);

    double minNum = numbers.firstElement().doubleValue();
    double maxNum = numbers.elementAt(numbers.size()-2).doubleValue() - minNum;

    int numBins = numbers.size() - 1;
    Vector<Double> binValues = new Vector<Double>(numBins);
    
    double sumBinValue = 0.0;
    for (int i = 0; i < numBins; i++) {
      double x = (numbers.get(i).doubleValue() - minNum) / maxNum;      
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
	public Integer sample() {
		if(this.categoriesByCumProbability.size() > 1)
		{
			if(!useAllPoints) {			    
			    double uniformDistributionSample = randomGenerator.nextDouble();
			    Double key = this.categoriesByCumProbability.ceilingKey(uniformDistributionSample);
			    Category<Double, Double> category = this.categoriesByCumProbability.get(key);
			    return (int)Math.round(category.getValue() + (randomGenerator.nextDouble() * category.getBinSize()));
			}
			else {
			    double uniformDistributionSample = randomGenerator.nextDouble();
			    Double key = this.categoriesByCumProbability.ceilingKey(uniformDistributionSample);
			    Category<Double, Double> category = this.categoriesByCumProbability.get(key);		    
			    return (int)Math.round(category.getValue());			
			}
		}
		else
		{
			Category<Double, Double> category = this.categoriesByCumProbability.values().iterator().next();	
			return (int)Math.round(category.getValue() + (randomGenerator.nextDouble() * category.getBinSize()));
		}
	}
}