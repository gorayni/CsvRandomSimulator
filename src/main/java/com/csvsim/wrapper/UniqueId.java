package com.csvsim.wrapper;

import org.apache.commons.math3.random.RandomGenerator;

import com.csvsim.generator.GeneratorWrapper;
import com.csvsim.random.UniqueIdDistribution;

public class UniqueId implements Generator<com.csvsim.generator.utils.Field<java.lang.Long>>, Distribution<java.lang.Long> {

	Field field;
	java.lang.Long startNumber;

	public UniqueId() {
	}
	
	public UniqueId(Field field, Long startNumber) {
		super();
		this.field = field;
		this.startNumber = startNumber;
	}

	public java.lang.Long getStartNumber() {
		return this.startNumber;
	}

	public void setStartNumber(java.lang.Long startNumber) {
		this.startNumber = startNumber;
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
		return field;
	}

	@Override
	public com.csvsim.generator.Generator getGenerator(com.csvsim.generator.utils.Field<java.lang.Long> field, RandomGenerator randomGenerator) {
		return new GeneratorWrapper<java.lang.Long>(field, this.getDistribution(randomGenerator));
	}

	@Override
	public com.csvsim.random.Distribution<java.lang.Long> getDistribution(RandomGenerator randomGenerator) {
		return new UniqueIdDistribution(randomGenerator, startNumber);
	}


}
