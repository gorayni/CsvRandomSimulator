package com.csvsim.random;

import org.apache.commons.math3.random.RandomGenerator;

public class UniqueIdDistribution implements Distribution<Long> {

	private Long counter = 0L;
	
	private RandomGenerator randomGenerator;

	public UniqueIdDistribution(RandomGenerator randomGenerator,Long startNumber) {
		this.counter = startNumber;
		this.randomGenerator = randomGenerator;
	}

	@Override
	public Long sample() {
		this.counter += (long) this.randomGenerator.nextInt(10) + 1L;
		return this.counter;
	}
}
