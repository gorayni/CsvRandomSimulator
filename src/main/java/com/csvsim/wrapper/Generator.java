package com.csvsim.wrapper;

import org.apache.commons.math3.random.RandomGenerator;

public interface Generator<T> {
	public boolean isContainer();
	public com.csvsim.wrapper.Field getField();
	public com.csvsim.generator.Generator getGenerator(T argument, RandomGenerator randomGenerator);
}
