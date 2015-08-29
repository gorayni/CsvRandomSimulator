package com.csvsim.wrapper;

import java.util.LinkedList;

@SuppressWarnings("rawtypes")
public class CsvGenerator {
	
	LinkedList<Field> fields;
	LinkedList<Field> hiddenFields;
	Generator generator;
	Long seed;
	
	public LinkedList<Field> getFields() {
		if(this.fields == null){
			this.fields = new LinkedList<Field>();
		}
		return this.fields;
	}
	
	public void addField(Field field) {
		if(this.fields == null){
			this.fields = new LinkedList<Field>();		
		}
		this.fields.add(field);
	}
	
	public void setFields(LinkedList<Field> fields) {
		this.fields = fields;
	}
		
	public void addHiddenField(Field hiddenField) {
		if(this.hiddenFields == null){
			this.hiddenFields = new LinkedList<Field>();		
		}
		this.hiddenFields.add(hiddenField);
	}

	public LinkedList<Field> getHiddenFields() {
		if(this.hiddenFields == null){
			this.hiddenFields = new LinkedList<Field>();
		}
		return this.hiddenFields;
	}

	public void setHiddenFields(LinkedList<Field> hiddenFields) {
		this.hiddenFields = hiddenFields;
	}

	public Generator getGenerator() {
		return this.generator;
	}
	public void setGenerator(Generator generator) {
		this.generator = generator;
	}

	public Long getSeed() {
		return this.seed;
	}

	public void setSeed(Long seed) {
		this.seed = seed;
	}
	
}
