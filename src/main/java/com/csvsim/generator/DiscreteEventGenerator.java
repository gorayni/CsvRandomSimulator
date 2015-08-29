package com.csvsim.generator;

import java.util.Date;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.joda.time.Instant;
import org.joda.time.MutableDateTime;
import org.joda.time.Period;

import com.csvsim.generator.utils.DiscreteEvent;
import com.csvsim.generator.utils.Field;
import com.csvsim.random.Distribution;

@SuppressWarnings("rawtypes")
public class DiscreteEventGenerator implements Generator{

	final static Instant zeroTime = (new MutableDateTime(0, 1, 1, 0, 0, 0,0)).toInstant();	
	
	TreeSet<DiscreteEvent> events = new TreeSet<DiscreteEvent>();	
	Set<Field> fields;
	Field<Date> timeField;
	
	public DiscreteEventGenerator(RandomGenerator randomGenerator,Field<Date> timeField, Distribution<Date> timeDistribution, Set<Field> fields, LinkedList<Generator> generators, int numEvents, Period frequency) {
		
		Long frequencyMS = frequency.toDurationFrom(zeroTime).getMillis();
		this.timeField = timeField;
		this.fields = fields;
		this.events = new TreeSet<DiscreteEvent>();		
		
		for (int i = 0; i < numEvents; i++) {
			PoissonDistribution poisson = new PoissonDistribution(randomGenerator, frequencyMS, 1000, PoissonDistribution.DEFAULT_MAX_ITERATIONS);
			DiscreteEvent event = new DiscreteEvent(timeDistribution.sample(),poisson);
			timeField.set(event.getTimestamp());
			
			for(Generator generator : generators){		
				generator.generate();
			}
			for(Field field : fields){
				event.addObject(field.getPath(), field.getClonedValue());	
			}			
			events.add(event);
		}
	}	

	@Override
	@SuppressWarnings("unchecked")
	public void generate() {
		DiscreteEvent event = (DiscreteEvent) events.pollFirst();		
		timeField.set(event.getTimestamp());
		for(Field field : fields){
			field.set(event.getObjectsByFieldName().get(field.getPath()));
		}
		event.next();
		events.add(event);
	}

	@Override
	public Field<?> getField() {
		return timeField;
	}
	
}
