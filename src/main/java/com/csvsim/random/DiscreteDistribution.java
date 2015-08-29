package com.csvsim.random;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.math3.random.RandomGenerator;

public class DiscreteDistribution<T> implements Distribution<T> {

	private NavigableMap<Double, T> categoriesByCumProbability = null;
	
	private RandomGenerator randomGenerator;

	private void init(RandomGenerator randomGenerator, Map<T, Double> percentsByCategory) {
		this.randomGenerator = randomGenerator;
		this.categoriesByCumProbability = new TreeMap<Double, T>();

		Double total = 0.0;
		for (Double percent : percentsByCategory.values()) {
			total += percent;
		}

		Double cumProb = 0.0;
		for (Entry<T, Double> percentByCategory : percentsByCategory.entrySet()) {
			if (percentByCategory.getValue() > 0) {
				Double probability = percentByCategory.getValue() / total;
				cumProb += probability;
				this.categoriesByCumProbability.put(cumProb,
						percentByCategory.getKey());
			}
		}
	}

	public DiscreteDistribution(RandomGenerator randomGenerator,Vector<T> categories) {
		HashMap<T, Double> dictionary = new HashMap<T, Double>();
		Double probability = 1.0 / categories.size();
		for (T category : categories) {
			dictionary.put(category, probability);
		}
		this.init(randomGenerator, dictionary);
	}

	public DiscreteDistribution(RandomGenerator randomGenerator,Map<T, Double> dictionary) {
		this.init(randomGenerator, dictionary);
	}

	@Override
	public T sample() {
		double uniformDistributionSample = randomGenerator.nextDouble();
		Double key = this.categoriesByCumProbability.ceilingKey(uniformDistributionSample);
		return this.categoriesByCumProbability.get(key);
	}
}
