package com.csvsim.wrapper;

import java.util.LinkedList;

import org.apache.commons.math3.random.RandomGenerator;

@SuppressWarnings("rawtypes")
public class DiscreteEvent implements Generator<com.csvsim.generator.utils.Field<java.util.Date>> {

	Generator timeDistribution;
	Period frequency;
	java.lang.Integer numEvents;	
	LinkedList<Generator> generators;	
	
	public Generator getTimeDistribution() {
		return this.timeDistribution;
	}

	public void setTimeDistribution(Generator timeDistribution) {
		this.timeDistribution = timeDistribution;
	}

	public Period getFrequency() {
		return this.frequency;
	}

	public void setFrequency(Period frequency) {
		this.frequency = frequency;
	}

	public java.lang.Integer getNumEvents() {
		return this.numEvents;
	}

	public void setNumEvents(java.lang.Integer numEvents) {
		this.numEvents = numEvents;
	}

	public LinkedList<Generator> getGenerators() {
		return this.generators;
	}

	public void setGenerators(LinkedList<Generator> generators) {
		this.generators = generators;
	}
	
	@Override
	public Field getField() {
		return this.timeDistribution.getField();
	}
	
	@Override
	public boolean isContainer() {
		return true;
	}

	@Override
	public com.csvsim.generator.Generator getGenerator(com.csvsim.generator.utils.Field<java.util.Date> field, RandomGenerator randomGenerator) {
		return null;
	}
}
