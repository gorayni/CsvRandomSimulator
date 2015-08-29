package com.csvsim.generator;

import java.util.TreeMap;

import com.csvsim.generator.utils.Field;

public class ExactDecisionGenerator<T> implements Generator {

	Field<T> field;
	TreeMap<T, Generator> generatorsByValue = null;

	public ExactDecisionGenerator(Field<T> field,
			TreeMap<T, Generator> generatorsByValue) {
		this.field = field;
		this.generatorsByValue = generatorsByValue;
	}

	@Override
	public void generate() {
		
		
		Generator generator = generatorsByValue.get(field.get());			
		if (generator != null) {
			generator.generate();
		}
	}

	@Override
	public Field<?> getField() {
		return field;
	}

}
