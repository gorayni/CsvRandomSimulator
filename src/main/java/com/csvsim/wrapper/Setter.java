package com.csvsim.wrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.math3.random.RandomGenerator;

import com.csvsim.generator.SetterGenerator;

public class Setter implements Generator<com.csvsim.generator.utils.Field<?>> {

	Field field;
	java.lang.String value;

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");;
	
	public Setter() {
	}

	public Setter(Field field, java.lang.String value) {
		super();
		this.field = field;
		this.value = value;
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
		return this.field;
	}	
	
	public java.lang.String getValue() {
		return this.value;
	}

	public void setValue(java.lang.String value) {
		this.value = value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public com.csvsim.generator.Generator getGenerator(
			com.csvsim.generator.utils.Field<?> field,
			RandomGenerator randomGenerator) {
		if (this.field.getType() == Field.Type.DOUBLE) {
			return new SetterGenerator<java.lang.Double>(
					(com.csvsim.generator.utils.Field<java.lang.Double>) field,
					java.lang.Double.parseDouble(value));
			
		} else if (this.field.getType() == Field.Type.INTEGER) {
			return new SetterGenerator<java.lang.Integer>(
					(com.csvsim.generator.utils.Field<java.lang.Integer>) field,
					java.lang.Integer.parseInt(value));
		} else if (this.field.getType() == Field.Type.STRING) {
			return new SetterGenerator<java.lang.String>(
					(com.csvsim.generator.utils.Field<java.lang.String>) field,
					value);
		} else if (this.field.getType() == Field.Type.DATE) {
			Date fixedDate = new Date();
			try {
				fixedDate =formatter.parse(value);
			} catch (ParseException e) {
				e.printStackTrace();
			} 
			return new SetterGenerator<Date>((com.csvsim.generator.utils.Field<Date>) field, fixedDate);
		}
		return null;
	}

}
