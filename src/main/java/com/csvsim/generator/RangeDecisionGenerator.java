package com.csvsim.generator;

import java.util.NavigableMap;

import com.csvsim.generator.utils.Field;

public class RangeDecisionGenerator<T> implements Generator{

	Field<T> field;
	NavigableMap<T, Generator> generatorsByValue = null;
		
	public RangeDecisionGenerator(Field<T> field, NavigableMap<T, Generator> generatorsByValue) {
		this.field = field;
		this.generatorsByValue = generatorsByValue;
	}

	@Override
	public void generate() {
		
		Generator generator = generatorsByValue.get(generatorsByValue.ceilingKey(field.get()));	
		if (generator != null) {
			generator.generate();
		}		
	}
	
	@Override
	public Field<?> getField() {
		return null;
	}
}
