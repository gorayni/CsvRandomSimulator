package com.csvsim.wrapper;

import org.apache.commons.math3.distribution.ParetoDistribution;
import org.apache.commons.math3.random.RandomGenerator;

import com.csvsim.generator.GeneratorWrapper;
import com.csvsim.random.AbstractRealDistributionWrapper;


public class Pareto implements Generator<com.csvsim.generator.utils.Field<java.lang.Double>>, Distribution<java.lang.Double> {

	Field field;
	java.lang.Double scale;
	java.lang.Double shape;
	java.lang.Double inverseCumAccuracy;

	public Pareto(){}
	
	public java.lang.Double getScale() {
		return this.scale;
	}

	public void setScale(java.lang.Double scale) {
		this.scale = scale;
	}

	public java.lang.Double getShape() {
		return this.shape;
	}

	public void setShape(java.lang.Double shape) {
		this.shape = shape;
	}

	public java.lang.Double getInverseCumAccuracy() {
		return this.inverseCumAccuracy;
	}

	public void setInverseCumAccuracy(java.lang.Double inverseCumAccuracy) {
		this.inverseCumAccuracy = inverseCumAccuracy;
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

	@Override
	public com.csvsim.generator.Generator getGenerator(com.csvsim.generator.utils.Field<java.lang.Double> field, RandomGenerator randomGenerator) {
		return new GeneratorWrapper<java.lang.Double>(field, getDistribution(randomGenerator));
	}

	@Override
	public com.csvsim.random.Distribution<java.lang.Double> getDistribution(RandomGenerator randomGenerator) {
		if(scale == null && shape == null && inverseCumAccuracy == null){
			return new AbstractRealDistributionWrapper(new ParetoDistribution(randomGenerator,1,1));
		}
		else if(scale != null && shape != null){
			if(inverseCumAccuracy == null){
				return new AbstractRealDistributionWrapper(new ParetoDistribution(randomGenerator,scale,shape));				
			}
			else{
				return new AbstractRealDistributionWrapper(new ParetoDistribution(randomGenerator,scale,shape,inverseCumAccuracy));
			}
		}
		return null;
	}
	

}
