package com.csvsim.wrapper;

import java.util.Map;

import org.apache.commons.math3.random.RandomGenerator;

@SuppressWarnings("rawtypes")
public class Decision<T> implements Generator{

	Field field;
	Map<T, Generator> generatorsByValue;
	boolean exact;
	
	public Decision() {
		super();
	}
		
	public Map<T, Generator> getGeneratorsByValue() {
		return this.generatorsByValue;
	}

	public void setGeneratorsByValue(Map<T, Generator> generatorsByValue) {
		this.generatorsByValue = generatorsByValue;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public boolean isExact() {
		return this.exact;
	}

	public void setExact(boolean exact) {
		this.exact = exact;
	}

	@Override
	public com.csvsim.generator.Generator getGenerator(Object object, RandomGenerator randomGenerator) {
		return null;
	}
		

	@Override
	public Field getField() {
		return field;
	}

	@Override
	public boolean isContainer() {
		return true;
	}


}
