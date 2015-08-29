package com.csvsim.wrapper;

import java.util.Date;

import org.apache.commons.math3.random.RandomGenerator;

import com.csvsim.generator.GeneratorWrapper;
import com.csvsim.random.UniformDateDistribution;

public class UniformDate implements Generator<com.csvsim.generator.utils.Field<java.util.Date>>, Distribution<java.util.Date> {

	Field field;
	java.util.Date initialDate;
	Period period;
	
	public java.util.Date getInitialDate() {
		return this.initialDate;
	}

	public void setInitialDate(java.util.Date initialDate) {
		this.initialDate = initialDate;
	}

	public Period getPeriod() {
		return this.period;
	}

	public void setPeriod(Period period) {
		this.period = period;
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
	public com.csvsim.generator.Generator getGenerator(com.csvsim.generator.utils.Field<java.util.Date> field,RandomGenerator randomGenerator) {
		return new GeneratorWrapper<java.util.Date>(field, getDistribution(randomGenerator));
	}

	@Override
	public com.csvsim.random.Distribution<Date> getDistribution(RandomGenerator randomGenerator) {
		return new UniformDateDistribution(randomGenerator, initialDate, period.getJodaPeriod());
	}
}
