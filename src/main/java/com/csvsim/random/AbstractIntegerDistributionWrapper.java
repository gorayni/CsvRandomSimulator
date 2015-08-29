package com.csvsim.random;

import org.apache.commons.math3.distribution.AbstractIntegerDistribution;

public class AbstractIntegerDistributionWrapper implements Distribution<Integer> {

	AbstractIntegerDistribution distribution;	
	
	public AbstractIntegerDistributionWrapper(AbstractIntegerDistribution distribution) {
		super();
		this.distribution = distribution;
	}	
	
	@Override
	public Integer sample() {
		return distribution.sample();
	}

}
