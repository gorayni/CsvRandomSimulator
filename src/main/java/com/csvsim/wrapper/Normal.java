package com.csvsim.wrapper;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.RandomGenerator;

import com.csvsim.generator.GeneratorWrapper;
import com.csvsim.random.AbstractRealDistributionWrapper;

public class Normal implements
		Generator<com.csvsim.generator.utils.Field<java.lang.Double>>,
		Distribution<java.lang.Double> {

	Field field;
	java.lang.Double mean;
	java.lang.Double sd;
	java.lang.Double inverseCumAccuracy;

	public Normal() {
	}

	public Normal(Field field) {
		this.field = field;
	}

	public Normal(Field field, java.lang.Double mean, java.lang.Double sd) {
		this.field = field;
		this.mean = mean;
		this.sd = sd;
	}

	public java.lang.Double getMean() {
		return this.mean;
	}

	public void setMean(java.lang.Double mean) {
		this.mean = mean;
	}

	public java.lang.Double getSd() {
		return this.sd;
	}

	public void setSd(java.lang.Double sd) {
		this.sd = sd;
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
	public com.csvsim.generator.Generator getGenerator(
			com.csvsim.generator.utils.Field<java.lang.Double> field,
			RandomGenerator randomGenerator) {
		return new GeneratorWrapper<java.lang.Double>(field,
				this.getDistribution(randomGenerator));
	}

	@Override
	public com.csvsim.random.Distribution<java.lang.Double> getDistribution(RandomGenerator randomGenerator) {
		if (mean == null && sd == null && inverseCumAccuracy == null) {
			return new AbstractRealDistributionWrapper(new NormalDistribution(randomGenerator,0,1));
		} else if (mean != null && sd != null) {
			if (inverseCumAccuracy == null) {
				return new AbstractRealDistributionWrapper(new NormalDistribution(randomGenerator, mean, sd));
			} else {
				return new AbstractRealDistributionWrapper(new NormalDistribution(randomGenerator, mean, sd, inverseCumAccuracy));
			}
		}		 
		return null;
	}

}
