package com.csvsim.wrapper;

import org.apache.commons.math3.random.RandomGenerator;

public interface Distribution<T> {
	public com.csvsim.random.Distribution<T> getDistribution(RandomGenerator randomGenerator);
}
