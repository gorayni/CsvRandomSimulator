package com.csvsim.wrapper;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.math3.random.RandomGenerator;

import com.csvsim.generator.GeneratorWrapper;
import com.csvsim.random.DiscreteDistribution;
import com.csvsim.random.IntegerDistribution;

public class Integer implements Generator<com.csvsim.generator.utils.Field<java.lang.Integer>>, Distribution<java.lang.Integer> {

	Field field;
	java.lang.Integer min;
	java.lang.Integer max;
	LinkedList<java.lang.Double> spline;
	Map<java.lang.Integer, java.lang.Double> discreteHistogram;

	public LinkedList<java.lang.Double> getSpline() {
		return this.spline;
	}

	public void setSpline(LinkedList<java.lang.Double> spline) {
		this.spline = spline;
	}

	public java.lang.Integer getMin() {
		return this.min;
	}

	public void setMin(java.lang.Integer min) {
		this.min = min;
	}

	public java.lang.Integer getMax() {
		return this.max;
	}

	public void setMax(java.lang.Integer max) {
		this.max = max;
	}

	public Map<java.lang.Integer, java.lang.Double> getDiscreteHistogram() {
		return this.discreteHistogram;
	}

	public void setDiscreteHistogram(
			Map<java.lang.Integer, java.lang.Double> discreteHistogram) {
		this.discreteHistogram = discreteHistogram;
	}

	public Integer() {
	}
	
	public Integer(Field field, java.lang.Integer... valores) {
		this.field =field;
		 this.discreteHistogram = new LinkedHashMap<java.lang.Integer, java.lang.Double>();
		 for(int valor :valores){
			 this.discreteHistogram.put(valor, 1.);
		 }
	}
	
	public Integer(Field field, Map<java.lang.Integer, java.lang.Double> discreteHistogram) {
		this.field = field;
		this.discreteHistogram = discreteHistogram;
	}

	public Field getField() {
		return this.field;
	}

	public void setField(Field field) {
		this.field = field;
	}


	public LinkedList<java.lang.Double> getHistogramaContinuo() {
		return this.spline;
	}

	public java.lang.String getSerializedDefinition() {
		StringBuffer sb = new StringBuffer();
		sb.append(min + "," + max);
		if (spline != null && !spline.isEmpty()) {
			sb.append(";");
			for (int i = 0; i < spline.size() - 1; i++) {
				sb.append(spline.get(i) + ",");
			}
			sb.append(spline.getLast());
		}
		return sb.toString();
	}

	public com.csvsim.generator.Generator getGenerator(com.csvsim.generator.utils.Field<java.lang.Integer> field, RandomGenerator randomGenerator) {
		return new GeneratorWrapper<java.lang.Integer>(field, getDistribution(randomGenerator));
	}

	@Override
	public boolean isContainer() {
		return false;
	}

	@Override
	public com.csvsim.random.Distribution<java.lang.Integer> getDistribution(RandomGenerator randomGenerator) {
		if (discreteHistogram == null) {
			return new IntegerDistribution(randomGenerator, getSerializedDefinition());
		} else {
			return new DiscreteDistribution<java.lang.Integer>(randomGenerator, discreteHistogram);
		}
	}
}
