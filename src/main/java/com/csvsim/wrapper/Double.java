package com.csvsim.wrapper;

import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.math3.random.RandomGenerator;

import com.csvsim.generator.GeneratorWrapper;
import com.csvsim.random.DiscreteDistribution;
import com.csvsim.random.DoubleDistribution;

public class Double implements Generator<com.csvsim.generator.utils.Field<java.lang.Double>>, Distribution<java.lang.Double> {

	Field field;
	java.lang.Double min;
	java.lang.Double max;
	LinkedList<java.lang.Double> spline;
	Map<java.lang.Double, java.lang.Double> discreteHistogram;

	public Double() {
	}

	public LinkedList<java.lang.Double> getSpline() {
		return this.spline;
	}

	public void setSpline(LinkedList<java.lang.Double> spline) {
		this.spline = spline;
	}

	public Field getField() {
		return this.field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public java.lang.Double getMin() {
		return this.min;
	}

	public void setMin(java.lang.Double min) {
		this.min = min;
	}

	public java.lang.Double getMax() {
		return this.max;
	}

	public void setMax(java.lang.Double max) {
		this.max = max;
	}

	public Map<java.lang.Double, java.lang.Double> getDiscreteHistogram() {
		return this.discreteHistogram;
	}

	public void setDiscreteHistogram(
			Map<java.lang.Double, java.lang.Double> discreteHistogram) {
		this.discreteHistogram = discreteHistogram;
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

	public com.csvsim.generator.Generator getGenerator( com.csvsim.generator.utils.Field<java.lang.Double> field, RandomGenerator randomGenerator) {
		return new GeneratorWrapper<java.lang.Double>(field, this.getDistribution(randomGenerator));
	}

	@Override
	public boolean isContainer() {
		return false;
	}

	@Override
	public com.csvsim.random.Distribution<java.lang.Double> getDistribution(RandomGenerator randomGenerator) {
		if (discreteHistogram == null) {
			return new DoubleDistribution(randomGenerator, getSerializedDefinition());
		} else {
			return new DiscreteDistribution<java.lang.Double>(randomGenerator, discreteHistogram);
		}
	}
	
	
}
