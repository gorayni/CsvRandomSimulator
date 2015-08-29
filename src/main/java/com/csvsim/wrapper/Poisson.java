package com.csvsim.wrapper;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.random.RandomGenerator;

import com.csvsim.generator.GeneratorWrapper;
import com.csvsim.random.AbstractIntegerDistributionWrapper;

public class Poisson implements
		Generator<com.csvsim.generator.utils.Field<java.lang.Integer>>, Distribution<java.lang.Integer> {

	Field field;
	java.lang.Double p;
	java.lang.Double epsilon;
	java.lang.Integer maxIterations;

	public Poisson() {
	}

	public java.lang.Double getP() {
		return this.p;
	}

	public void setP(java.lang.Double p) {
		this.p = p;
	}

	public java.lang.Double getEpsilon() {
		return this.epsilon;
	}

	public void setEpsilon(java.lang.Double epsilon) {
		this.epsilon = epsilon;
	}

	public java.lang.Integer getMaxIterations() {
		return this.maxIterations;
	}

	public void setMaxIterations(java.lang.Integer maxIterations) {
		this.maxIterations = maxIterations;
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
	public com.csvsim.generator.Generator getGenerator(com.csvsim.generator.utils.Field<java.lang.Integer> field, RandomGenerator randomGenerator) {
		return new GeneratorWrapper<java.lang.Integer>(field, getDistribution(randomGenerator));
	}

	@Override
	public com.csvsim.random.Distribution<java.lang.Integer> getDistribution(RandomGenerator randomGenerator) {
		if (p != null) {
			if (epsilon != null) {
				if (maxIterations != null) {
					return new AbstractIntegerDistributionWrapper(new PoissonDistribution(randomGenerator, p, epsilon, maxIterations));
				}
				return new AbstractIntegerDistributionWrapper(new PoissonDistribution(randomGenerator, p, epsilon, PoissonDistribution.DEFAULT_MAX_ITERATIONS));

			} else {
				if (maxIterations != null) {
					return new AbstractIntegerDistributionWrapper(new PoissonDistribution(randomGenerator, p,PoissonDistribution.DEFAULT_EPSILON, maxIterations));
				}
				return new AbstractIntegerDistributionWrapper(new PoissonDistribution(randomGenerator, p, PoissonDistribution.DEFAULT_EPSILON, PoissonDistribution.DEFAULT_MAX_ITERATIONS));
			}
		}
		return null;
	}

}
