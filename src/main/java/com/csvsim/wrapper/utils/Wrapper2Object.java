package com.csvsim.wrapper.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.RandomGeneratorFactory;
import org.joda.time.Period;

import com.csvsim.generator.DiscreteEventGenerator;
import com.csvsim.generator.ExactDecisionGenerator;
import com.csvsim.generator.Generator;
import com.csvsim.generator.ListGenerator;
import com.csvsim.generator.RangeDecisionGenerator;
import com.csvsim.generator.utils.Field;
import com.csvsim.wrapper.CsvGenerator;
import com.csvsim.wrapper.Decision;
import com.csvsim.wrapper.Field.Type;

public class Wrapper2Object {

	public static Object createCsvObject(CsvGenerator csvGenerator) throws InstantiationException, IllegalAccessException, NotFoundException, CannotCompileException {
		return PojoCreator.createPojo("mx.itam.CsvPojo$Generated" + System.nanoTime(), csvGenerator.getFields(), csvGenerator.getHiddenFields()).newInstance();
	}	
	
	private static TreeMap<String, Field<?>> getFields( CsvGenerator csvGenerator, Object obj) {
		TreeMap<String, Field<?>> fields = new TreeMap<String, Field<?>>();
		LinkedList<com.csvsim.wrapper.Field> allFields = new LinkedList<com.csvsim.wrapper.Field>();
		if (csvGenerator.getFields() != null) {
			allFields.addAll(csvGenerator.getFields());
		}
		if (csvGenerator.getHiddenFields() != null) {
			allFields.addAll(csvGenerator.getHiddenFields());
		}
		for (com.csvsim.wrapper.Field field : allFields) {
			if(field.getConstantValue() == null){
				if (field.getType() == Type.INTEGER) {
					fields.put(field.getName(), new Field<Integer>(obj, field.getName()));
				} else if (field.getType() == Type.DOUBLE) { 
					fields.put(field.getName(), new Field<Double>(obj, field.getName()));
				} else if (field.getType() == Type.STRING) {
					fields.put(field.getName(), new Field<String>(obj, field.getName()));
				} else if (field.getType() == Type.DATE) {
					fields.put(field.getName(), new Field<Date>(obj, field.getName()));
				}else if (field.getType() == Type.LONG) {
					fields.put(field.getName(), new Field<Long>(obj, field.getName()));
				}
			}
		}
		return fields;
	}
	
	
	@SuppressWarnings("rawtypes")
	private static void getFields( TreeMap<String, Field<?>> knownFields,TreeSet<Field> fields, com.csvsim.wrapper.Generator generatorDef){
		if (generatorDef.isContainer()) {
			if (generatorDef.getClass() == com.csvsim.wrapper.List.class) {
				com.csvsim.wrapper.List listDef = (com.csvsim.wrapper.List) generatorDef;
				for (com.csvsim.wrapper.Generator subGenDef : listDef.getGenerators()) {	
					getFields(knownFields, fields,subGenDef);
				}
			} else if(generatorDef.getClass() == com.csvsim.wrapper.Decision.class){
				Decision decisionDef = (Decision) generatorDef;
				for(Object object : decisionDef.getGeneratorsByValue().values()){
					com.csvsim.wrapper.Generator subGenDef = (com.csvsim.wrapper.Generator) object; 
					getFields(knownFields, fields,subGenDef);
				}
				fields.add(knownFields.get(decisionDef.getField().getName()));
			} else {				
				com.csvsim.wrapper.DiscreteEvent discreteEvent = (com.csvsim.wrapper.DiscreteEvent) generatorDef;				
				getFields(knownFields, fields, discreteEvent.getTimeDistribution());
				for (com.csvsim.wrapper.Generator subGenDef : discreteEvent.getGenerators()) {	
					getFields(knownFields, fields,subGenDef);
				}
			}			
		} else {
			fields.add(knownFields.get(generatorDef.getField().getName()));
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static TreeSet<Field> getFields(TreeMap<String, Field<?>> knownFields, List<com.csvsim.wrapper.Generator> generatorsDef){
		TreeSet<Field> fields = new TreeSet<Field>();
		for(com.csvsim.wrapper.Generator generatorDef : generatorsDef){
			getFields(knownFields,fields, generatorDef);
		}
		return fields;		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Generator getGenerator(final RandomGenerator random, TreeMap<String, Field<?>> fieldsByName, HashMap<com.csvsim.wrapper.Generator<?>, Generator> createdGeneratorByDef, com.csvsim.wrapper.Generator generatorDef) {
		Generator generator = null;
		
		if (createdGeneratorByDef.containsKey(generatorDef)) {
			generator = createdGeneratorByDef.get(generatorDef);
		} else if (generatorDef.isContainer()) {
			if(generatorDef.getClass() == com.csvsim.wrapper.DiscreteEvent.class){
				com.csvsim.wrapper.DiscreteEvent discreteEvent = (com.csvsim.wrapper.DiscreteEvent) generatorDef;				
				Field timeField = fieldsByName.get(discreteEvent.getField().getName());
				Period frequency = discreteEvent.getFrequency().getJodaPeriod();
				int numEvents = discreteEvent.getNumEvents();
				com.csvsim.wrapper.Distribution timeDistribution = (com.csvsim.wrapper.Distribution) discreteEvent.getTimeDistribution();
		
				Set<Field> fields = getFields(fieldsByName, discreteEvent.getGenerators());

				LinkedList<Generator> generadores = new LinkedList<Generator>();
				for (com.csvsim.wrapper.Generator subGenDef : discreteEvent.getGenerators()) {
					generadores.add(getGenerator(random,fieldsByName,createdGeneratorByDef,subGenDef));
				}				
				generator = new DiscreteEventGenerator(random, timeField, timeDistribution.getDistribution(random), fields, generadores, numEvents, frequency);				
				createdGeneratorByDef.put(generatorDef, generator);
			}
			else if (generatorDef.getClass() == com.csvsim.wrapper.List.class) {
				com.csvsim.wrapper.List listDef = (com.csvsim.wrapper.List) generatorDef;
				LinkedList<Generator> generadores = new LinkedList<Generator>();

				for (com.csvsim.wrapper.Generator subGenDef : listDef.getGenerators()) {
					generadores.add(getGenerator(random,fieldsByName,createdGeneratorByDef,subGenDef));
				}
				generator = new ListGenerator(generadores);
				createdGeneratorByDef.put(generatorDef, generator);
			} else {
				Decision decisionDef = (Decision) generatorDef;

				if (decisionDef.getField().getType() == Type.INTEGER) {
					TreeMap<Integer, Generator> generatorsByValue = new TreeMap<Integer, Generator>();
					Iterator itr = decisionDef.getGeneratorsByValue().entrySet().iterator();
					while (itr.hasNext()) {
						Entry<Integer, com.csvsim.wrapper.Generator> entry = (Entry<Integer, com.csvsim.wrapper.Generator>) itr.next();
						generatorsByValue.put(entry.getKey(), getGenerator(random,fieldsByName, createdGeneratorByDef, entry.getValue()));
					}
					Field<Integer> field = (Field<Integer>) fieldsByName .get(decisionDef.getField().getName());
					if (decisionDef.isExact()) {
						generator = new ExactDecisionGenerator<Integer>(field, generatorsByValue);
					} else {
						generator = new RangeDecisionGenerator<Integer>(field, generatorsByValue);
					}
					createdGeneratorByDef.put(generatorDef, generator);
				} else if (decisionDef.getField().getType() == Type.DOUBLE) {
					TreeMap<Double, Generator> generatorsByValue = new TreeMap<Double, Generator>();
					Iterator itr = decisionDef.getGeneratorsByValue().entrySet().iterator();
					while (itr.hasNext()) {
						Entry<Double, com.csvsim.wrapper.Generator> entry = (Entry<Double, com.csvsim.wrapper.Generator>) itr.next();						
						generatorsByValue.put(entry.getKey(), getGenerator(random,fieldsByName, createdGeneratorByDef, entry.getValue()));
					}
					Field<Double> field = (Field<Double>) fieldsByName.get(decisionDef.getField().getName());
					if (decisionDef.isExact()) {
						generator = new ExactDecisionGenerator<Double>(field, generatorsByValue);
					} else {
						generator = new RangeDecisionGenerator<Double>(field, generatorsByValue);
					}
					createdGeneratorByDef.put(generatorDef, generator);
				} else if (decisionDef.getField().getType() == Type.STRING) {
					TreeMap<String, Generator> generatorsByValue = new TreeMap<String, Generator>();
					Iterator itr = decisionDef.getGeneratorsByValue().entrySet().iterator();
					while (itr.hasNext()) {
						Entry<String, com.csvsim.wrapper.Generator> entry = (Entry<String, com.csvsim.wrapper.Generator>) itr.next();
						generatorsByValue.put(entry.getKey(), getGenerator(random,fieldsByName, createdGeneratorByDef, entry.getValue()));
					}
					Field<String> field = (Field<String>) fieldsByName.get(decisionDef.getField().getName());
					if (decisionDef.isExact()) {
						generator = new ExactDecisionGenerator<String>(field,generatorsByValue);
					} else {
						generator = new RangeDecisionGenerator<String>(field,generatorsByValue);
					}
					createdGeneratorByDef.put(generatorDef, generator);
				} else if (decisionDef.getField().getType() == Type.LONG) {
					TreeMap<Long, Generator> generatorsByValue = new TreeMap<Long, Generator>();
					Iterator itr = decisionDef.getGeneratorsByValue().entrySet().iterator();
					while (itr.hasNext()) {
						Entry<Long, com.csvsim.wrapper.Generator> entry = (Entry<Long, com.csvsim.wrapper.Generator>) itr.next();
						generatorsByValue.put(entry.getKey(), getGenerator(random,fieldsByName, createdGeneratorByDef, entry.getValue()));
					}
					Field<Long> field = (Field<Long>) fieldsByName .get(decisionDef.getField().getName());
					if (decisionDef.isExact()) {
						generator = new ExactDecisionGenerator<Long>(field, generatorsByValue);
					} else {
						generator = new RangeDecisionGenerator<Long>(field, generatorsByValue);
					}
					createdGeneratorByDef.put(generatorDef, generator);
				}
				else if (decisionDef.getField().getType() == Type.DATE) {
					TreeMap<Date, Generator> generatorsByValue = new TreeMap<Date, Generator>();
					Iterator itr = decisionDef.getGeneratorsByValue().entrySet().iterator();
					while (itr.hasNext()) {
						Entry<Date, com.csvsim.wrapper.Generator> entry = (Entry<Date, com.csvsim.wrapper.Generator>) itr.next();
						generatorsByValue.put(entry.getKey(), getGenerator(random,fieldsByName, createdGeneratorByDef, entry.getValue()));
					}
					Field<Date> field = (Field<Date>) fieldsByName.get(decisionDef.getField().getName());
					if (decisionDef.isExact()) {
						generator = new ExactDecisionGenerator<Date>(field,generatorsByValue);
					} else {
						generator = new RangeDecisionGenerator<Date>(field,generatorsByValue);
					}
					createdGeneratorByDef.put(generatorDef, generator);
				}
			}
		} else {
			Field field = fieldsByName.get(generatorDef.getField().getName());
			generator = generatorDef.getGenerator(field, random);
			createdGeneratorByDef.put(generatorDef, generator);
		}
		return generator;
	}

	public static Generator getGenerator(CsvGenerator csvGenerator, Object csvObject) {
		
		HashMap<com.csvsim.wrapper.Generator<?>, Generator> createdGeneratorByYamlDef = new HashMap<com.csvsim.wrapper.Generator<?>, Generator>();		
		RandomGenerator randomGenerator;
		if(csvGenerator.getSeed()!=null){
			randomGenerator = RandomGeneratorFactory.createRandomGenerator(new Random(csvGenerator.getSeed()));
		}
		else{
			 randomGenerator = RandomGeneratorFactory.createRandomGenerator(new Random());
		}
		
		TreeMap<String, Field<?>> fields = Wrapper2Object.getFields(csvGenerator, csvObject);
		return getGenerator(randomGenerator, fields, createdGeneratorByYamlDef, csvGenerator.getGenerator());		
	}
}
