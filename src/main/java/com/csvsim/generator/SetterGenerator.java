package com.csvsim.generator;

import com.csvsim.generator.utils.Field;

public class SetterGenerator<T> implements Generator {

	Field<T> field;
	T value;
	
	public SetterGenerator(Field<T> field, T value) {
		super();
		this.field = field;
		this.value = value;
	}

	@Override
	public void generate() {
		this.field.set(value);
	}

	@Override
	public Field<?> getField() {
		return field;
	}
}
