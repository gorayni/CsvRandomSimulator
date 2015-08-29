package com.csvsim.wrapper;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.random.RandomGenerator;

import com.csvsim.generator.GeneratorWrapper;
import com.csvsim.random.StringDistribution;

public class String implements Generator<com.csvsim.generator.utils.Field<java.lang.String>>, Distribution<java.lang.String> {

	Field field;
	Map<java.lang.String, java.lang.Double> discreteHistogram;
	
	java.util.List<java.lang.String> values;

	public String() {
	}
	
	public String(Field field, java.util.List<java.lang.String> values) {
		this.field = field;
		this.values = values;
	}

	public String(Field field, Map<java.lang.String, java.lang.Double> discreteHistogram) {
		this.field = field;
		this.discreteHistogram = discreteHistogram;
	}	
	
	public Field getField() {
		return this.field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Map<java.lang.String, java.lang.Double> getDiscreteHistogram() {
		return this.discreteHistogram;
	}

	public void setDiscreteHistogram(
			Map<java.lang.String, java.lang.Double> discreteHistogram) {
		this.discreteHistogram = discreteHistogram;
	}

	public java.util.List<java.lang.String> getValues() {
		return this.values;
	}

	public void setValues(java.util.List<java.lang.String> values) {
		this.values = values;
	}

	@Override
	public boolean isContainer() {
		return false;
	}

	public com.csvsim.generator.Generator getGenerator(com.csvsim.generator.utils.Field<java.lang.String> field, RandomGenerator randomGenerator) {
		return new GeneratorWrapper<java.lang.String>(field, this.getDistribution(randomGenerator));
	}
	
	@Override
	public com.csvsim.random.Distribution<java.lang.String> getDistribution(RandomGenerator randomGenerator) {
		if(this.discreteHistogram!=null){
			return new StringDistribution(randomGenerator, this.discreteHistogram);
		}
		else {
			Map<java.lang.String, java.lang.Double> discreteHistogram = new HashMap<java.lang.String, java.lang.Double>();
			for(java.lang.String value : this.values){
				discreteHistogram.put(value, 1.);
			}
			return new StringDistribution(randomGenerator, discreteHistogram);
		}
	}	
}
