package com.csvsim.generator;

import java.util.LinkedList;

import com.csvsim.generator.utils.Field;

public class ListGenerator implements Generator {
	
	LinkedList<Generator> generators;
	
	public ListGenerator(LinkedList<Generator> generators){
		this.generators = generators;
	}

	public LinkedList<Generator> getGenerators() {
		return this.generators;
	}

	public void setGenerators(LinkedList<Generator> generators) {
		this.generators = generators;
	}

	@Override
	public void generate() {
		for(Generator generator : generators){
			generator.generate();
		}
	}

	@Override
	public Field<?> getField() {
		return null;
	}
}
