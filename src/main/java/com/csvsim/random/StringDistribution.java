package com.csvsim.random;

import java.util.Map;
import java.util.Vector;

import org.apache.commons.math3.random.RandomGenerator;

import com.csvsim.random.utils.Utils;

public class StringDistribution extends DiscreteDistribution<String> {

  public StringDistribution(RandomGenerator randomGenerator, Vector<String> categories) {
    super(randomGenerator, categories);
  }

  public StringDistribution(RandomGenerator randomGenerator, Map<String, Double> dictionary) {
    super(randomGenerator, dictionary);
  }

  public StringDistribution(RandomGenerator randomGenerator, String serializedDefinitions) {
    super(randomGenerator, Utils.createStringMap(serializedDefinitions));
  }

}
