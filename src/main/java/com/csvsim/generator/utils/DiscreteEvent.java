package com.csvsim.generator.utils;

import java.util.Date;
import java.util.TreeMap;

import org.apache.commons.math3.distribution.IntegerDistribution;
import org.joda.time.MutableDateTime;

import com.google.common.collect.Maps;

public class DiscreteEvent implements Comparable<DiscreteEvent> {

	private MutableDateTime counterDate;
	private TreeMap<String,Object> objectsByFieldName = Maps.newTreeMap();
	private IntegerDistribution distribution;
	
	public DiscreteEvent(){}
	
	public DiscreteEvent(Date timestamp, IntegerDistribution distribution) {
		super();
		this.distribution = distribution;
		this.counterDate = new MutableDateTime(timestamp.getTime());
	}
	
//	private DiscreteEvent(DiscreteEvent discreteEvent, int milliseconds) {
//		super();
//		this.distribution = discreteEvent.distribution;
//		this.objectsByFieldName = discreteEvent.objectsByFieldName;
//		this.counterDate = discreteEvent.counterDate.copy();
//		this.counterDate.addMillis(milliseconds);
//	}

	public Date getTimestamp() {
		return this.counterDate.toDate();
	}

	public TreeMap<String, Object> getObjectsByFieldName() {
		return this.objectsByFieldName;
	}

	public void setObjectsByFieldName(TreeMap<String, Object> objectsByFieldName) {
		this.objectsByFieldName = objectsByFieldName;
	}
	
	public void addObject(String fieldName, Object object){
		this.objectsByFieldName.put(fieldName, object);
	}
	@Override
	public int compareTo(DiscreteEvent anotherEvent) {
		return this.counterDate.compareTo(anotherEvent.counterDate);
	}
	
	public void next(){
		this.counterDate.addMillis(distribution.sample());
	}
}
