package com.csvsim.wrapper;


public class Field {
	public enum Type {
	    INTEGER, DOUBLE, STRING, DATE, LONG
	}
	private java.lang.String name;
	
	private java.lang.String constantValue = null;
	
	private java.lang.String format = null;
	
	private Type type;

	public java.lang.String getName() {
		return this.name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public java.lang.String getFormat() {
		return this.format;
	}

	public void setFormat(java.lang.String format) {
		this.format = format;
	}

	public java.lang.String getConstantValue() {
		return constantValue;
	}

	public void setConstantValue(java.lang.String constantValue) {
		this.constantValue = constantValue;
	}
	
	public Class<?> getFieldClass(){		
		if (type == Type.INTEGER) {			
			return java.lang.Integer.class;
		} else if (type == Type.DOUBLE) {
			return java.lang.Double.class;
		} else if (type == Type.STRING) {
			return java.lang.String.class;
		} else if (type == Type.DATE) {
			return java.util.Date.class;
		} else if (type == Type.LONG) {
			return java.lang.Long.class;
		}
		return null;
	}	
	
}
