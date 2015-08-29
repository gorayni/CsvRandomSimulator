package com.csvsim.random;

import java.util.Date;

import org.apache.commons.math3.random.RandomGenerator;
import org.joda.time.Instant;
import org.joda.time.MutableDateTime;
import org.joda.time.Period;

public class UniformDateDistribution implements Distribution<Date>{

	private Long initialDate;
	
	private RandomGenerator randomGenerator;
	
	private Long periodMS;
	
	private MutableDateTime mutableDate;	
	
	final static Instant zeroTime = (new MutableDateTime(0, 1, 1, 0, 0, 0,0)).toInstant();
	
	public UniformDateDistribution(RandomGenerator randomGenerator, Date initialDate, Period period) {
		this.randomGenerator = randomGenerator;
		this.initialDate = initialDate.getTime();
		this.periodMS = period.toDurationFrom(zeroTime).getMillis();
		this.mutableDate = new MutableDateTime();
	}
		
	@Override
	public Date sample() {
		Long randomTime = (long) (randomGenerator.nextDouble()*periodMS);
		this.mutableDate.setDate(initialDate);
		this.mutableDate.add(randomTime);
		return this.mutableDate.toDate();
	}

}
