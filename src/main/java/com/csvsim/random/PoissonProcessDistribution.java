package com.csvsim.random;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.joda.time.Instant;
import org.joda.time.MutableDateTime;
import org.joda.time.Period;

public class PoissonProcessDistribution implements Distribution<Date> {

	private Integer numArrivalsPerCycle;
	
	private NavigableMap<Double, Integer> indicesByNumCumArrivals;
	
	private ArrayList<PoissonDistribution> poissonDistributions;
	
	private MutableDateTime counterDate;
	
	private int generatedCounter;

	final static Instant startInstant = (new MutableDateTime(0, 1, 1, 0, 0, 0,0)).toInstant();

	public PoissonProcessDistribution(RandomGenerator randomGenerator, Map<Period, Double> percentsByPeriod,int numArrivalsPerCycle) {
		this(randomGenerator, percentsByPeriod, numArrivalsPerCycle, PoissonProcessDistribution.getDay(new Date()));
	}

	public PoissonProcessDistribution(RandomGenerator randomGenerator, Map<Period, Double> percentsByPeriod, int numArrivalsPerCycle, Date startDate) {

		this.numArrivalsPerCycle = numArrivalsPerCycle;

		int numPeriods = percentsByPeriod.size();
		this.poissonDistributions = new ArrayList<PoissonDistribution>(numPeriods);
		this.indicesByNumCumArrivals = new TreeMap<Double, Integer>();

		NavigableMap<Long, Double> numCumArrivalsByCumLapseDuration = new TreeMap<Long, Double>();

		TreeMap<Period, Double> probabilitiesByPeriod = getProbabilitiesByPeriod(percentsByPeriod);

		Long lapseDurationCumSum = (long) 0;
		Double numArrivalsCumSum = 0.0;
		Period lastPeriod = new Period(0);
		int index = 0;
		for (Entry<Period, Double> probabilityByPeriod : probabilitiesByPeriod.entrySet()) {
			Double probability = probabilityByPeriod.getValue();
			Period period = probabilityByPeriod.getKey();

			Double numArrivalsPerLapse = numArrivalsPerCycle * probability;
			Long lapseDuration = period.minus(lastPeriod).toDurationFrom(startInstant).getMillis();

			Double arrivalRate = lapseDuration / numArrivalsPerLapse;
			poissonDistributions.add(new PoissonDistribution(randomGenerator, arrivalRate, 1000, numArrivalsPerCycle));
			
			numArrivalsCumSum += numArrivalsPerLapse;
			indicesByNumCumArrivals.put(numArrivalsCumSum, index++);

			lapseDurationCumSum += lapseDuration;
			numCumArrivalsByCumLapseDuration.put(lapseDurationCumSum,numArrivalsCumSum);
			lastPeriod = period;
		}

		this.counterDate = new MutableDateTime(startDate.getTime());
		Long millisOfStartDate = (long) this.counterDate.millisOfDay().get();
		generatedCounter = (int) Math.round(numCumArrivalsByCumLapseDuration.ceilingEntry(millisOfStartDate).getValue());
	}

	private static Date getDay(Date date) {
		Calendar dayDate = Calendar.getInstance();
		dayDate.setTime(date);
		dayDate.set(Calendar.HOUR_OF_DAY, 0);
		dayDate.set(Calendar.MINUTE, 0);
		dayDate.set(Calendar.SECOND, 0);
		dayDate.set(Calendar.MILLISECOND, 0);
		return dayDate.getTime();
	}

	private static TreeMap<Period, Double> getProbabilitiesByPeriod(Map<Period, Double> percentsByPeriod) {
		Double cumPercentSum = 0.;
		for (Double percent : percentsByPeriod.values()) {
			cumPercentSum += percent;
		}

		TreeMap<Period, Double> probabilitiesByPeriod = new TreeMap<Period, Double>(
				new Comparator<org.joda.time.Period>() {
					@Override
					public int compare(org.joda.time.Period o1,org.joda.time.Period o2) {
						return o1.toDurationFrom(startInstant).compareTo(o2.toDurationFrom(startInstant));
					}
				});

		for (Entry<Period, Double> percentByPeriod : percentsByPeriod.entrySet()) {
			Period period = percentByPeriod.getKey();
			Double percent = percentByPeriod.getValue();
			probabilitiesByPeriod.put(period, percent / cumPercentSum);
		}
		return probabilitiesByPeriod;
	}

	@Override
	public Date sample() {
		int i = indicesByNumCumArrivals.ceilingEntry((double) generatedCounter).getValue();
		int millisecondsSample = poissonDistributions.get(i).sample();
		counterDate.addMillis(millisecondsSample);

		if (++generatedCounter >= numArrivalsPerCycle) {
			generatedCounter = 0;
		}
		return counterDate.toDate();
	}

}
