package com.csvsim.generator;

import com.csvsim.generator.utils.Field;
import com.csvsim.random.Distribution;

public class GeneratorWrapper<T> implements Generator {
	
	Field<T> field;
	Distribution<T> distribution;

	public GeneratorWrapper(Field<T> field, Distribution<T> distribution){
		this.field = field;
		this.distribution = distribution;		
	}

	@Override
	public void generate() {
		this.field.set(distribution.sample());
	}

	@Override
	public Field<?> getField() {
		return field;
	}	
}
