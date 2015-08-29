package com.csvsim.wrapper;

import java.util.Arrays;
import java.util.LinkedList;

import org.apache.commons.math3.random.RandomGenerator;

@SuppressWarnings("rawtypes")
public class List implements Generator {

	LinkedList<Generator> generators;

	public List() {
	}

	public List(LinkedList<Generator> generators) {
		this.generators = generators;
	}
	
	public List(Generator... generators) {
		this.generators = new LinkedList<Generator>(Arrays.asList(generators));
	}

	public LinkedList<Generator> getGenerators() {
		return this.generators;
	}

	public void setGenerators(LinkedList<Generator> generators) {
		this.generators = generators;
	}
	
	public void add(Generator generator){
		this.generators.add(generator);
	}

	@Override
	public com.csvsim.generator.Generator getGenerator(Object object, RandomGenerator randomGenerator) {
		return null;
	}

	@Override
	public Field getField() {
		return null;
	}

	@Override
	public boolean isContainer() {
		return true;
	}

}
