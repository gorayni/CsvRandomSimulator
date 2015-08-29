package com.csvsim.wrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.random.RandomGenerator;

import com.csvsim.generator.GeneratorWrapper;
import com.csvsim.random.PoissonProcessDistribution;

public class PoissonProcess implements Generator<com.csvsim.generator.utils.Field<java.util.Date>>, Distribution<java.util.Date> {

	Field field;
	int numArrivalsPerCycle;
	Map<Period, java.lang.Double> probabilityByPeriod;
	Date startDate = null;

	public int getNumArrivalsPerCycle() {
		return this.numArrivalsPerCycle;
	}

	public void setNumArrivalsPerCycle(int numArrivalsPerCycle) {
		this.numArrivalsPerCycle = numArrivalsPerCycle;
	}

	public Map<Period, java.lang.Double> getProbabilityByPeriod() {
		return this.probabilityByPeriod;
	}

	public void setProbabilityByPeriod(Map<Period, java.lang.Double> probabilityByPeriod) {
		this.probabilityByPeriod = probabilityByPeriod;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setField(Field field) {
		this.field = field;
	}

	@Override
	public boolean isContainer() {
		return false;
	}

	@Override
	public com.csvsim.wrapper.Field getField() {
		return field;
	}

	@Override
	public com.csvsim.generator.Generator getGenerator(com.csvsim.generator.utils.Field<Date> field, RandomGenerator randomGenerator) {
		return new GeneratorWrapper<Date>(field, getDistribution(randomGenerator));
	}

	@Override
	public com.csvsim.random.Distribution<java.util.Date> getDistribution(RandomGenerator randomGenerator) {
		Map<org.joda.time.Period, java.lang.Double> probabilityByPeriod = new HashMap<org.joda.time.Period, java.lang.Double>();
		for (Entry<Period, java.lang.Double> entry : this.probabilityByPeriod.entrySet()) {
			probabilityByPeriod.put(entry.getKey().getJodaPeriod(), entry.getValue());
		}
		
		if(startDate==null){
			return new PoissonProcessDistribution(randomGenerator, probabilityByPeriod, numArrivalsPerCycle);
		}
		return new PoissonProcessDistribution(randomGenerator, probabilityByPeriod, numArrivalsPerCycle,startDate);
	}

}
