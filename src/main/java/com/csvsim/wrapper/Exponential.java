package com.csvsim.wrapper;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.random.RandomGenerator;

import com.csvsim.generator.GeneratorWrapper;
import com.csvsim.random.AbstractRealDistributionWrapper;

public class Exponential implements Generator<com.csvsim.generator.utils.Field<java.lang.Double>>, Distribution<java.lang.Double> {

	Field field;
	java.lang.Double mean;
	java.lang.Double inverseCumAccuracy;

	public Exponential() {
	}

	public java.lang.Double getMean() {
		return this.mean;
	}

	public void setMean(java.lang.Double mean) {
		this.mean = mean;
	}

	public java.lang.Double getInverseCumAccuracy() {
		return this.inverseCumAccuracy;
	}

	public void setInverseCumAccuracy(java.lang.Double inverseCumAccuracy) {
		this.inverseCumAccuracy = inverseCumAccuracy;
	}

	public void setField(Field field) {
		this.field = field;
	}

	@Override
	public boolean isContainer() {
		return false;
	}

	@Override
	public Field getField() {
		return this.field;
	}

	@Override
	public com.csvsim.generator.Generator getGenerator(com.csvsim.generator.utils.Field<java.lang.Double> field, RandomGenerator randomGenerator) {
		return new GeneratorWrapper<java.lang.Double>(field, this.getDistribution(randomGenerator));
	}

	@Override
	public com.csvsim.random.Distribution<java.lang.Double> getDistribution(RandomGenerator randomGenerator) {
		if (mean != null) {
			if (inverseCumAccuracy == null) {
				return new AbstractRealDistributionWrapper(new ExponentialDistribution(randomGenerator, mean));
			}
			else{
				return new AbstractRealDistributionWrapper(new ExponentialDistribution(randomGenerator, mean, inverseCumAccuracy));
			}
		}
		return null;
	}

}
