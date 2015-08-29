package com.csvsim.random;

import org.apache.commons.math3.distribution.AbstractRealDistribution;

public class AbstractRealDistributionWrapper implements Distribution<Double> {

	AbstractRealDistribution distribution;	
	
	public AbstractRealDistributionWrapper(AbstractRealDistribution distribution) {
		super();
		this.distribution = distribution;
	}	
	
	@Override
	public Double sample() {
		return distribution.sample();
	}

}
