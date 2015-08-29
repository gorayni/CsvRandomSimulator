package com.csvsim.generator.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathNotFoundException;
import org.apache.commons.jxpath.Pointer;

@SuppressWarnings("rawtypes")
public class Field<T> implements Comparable<Field> {

  private Pointer pointer = null;
  private final ValueWrapper wrapper;
  private String label = "";

  public Field(Object object, String path) throws JXPathNotFoundException {
	  this(object, path, "", null);
  }
  
  public Field(Object object, String path, String etiqueta, ValueWrapper wrapper) throws JXPathNotFoundException {
    JXPathContext context = JXPathContext.newContext(object);
    Pointer pointer = context.getPointer(path);

    this.pointer = pointer;
    this.label = etiqueta;
    this.wrapper = wrapper;
  }

  public String getPath() {
    return this.pointer.asPath();
  }

  public String getLabel() {
    return this.label;
  }

  public void set(T value) {

    if (this.wrapper == null) {
      this.pointer.setValue(value);
    } else {
      this.pointer.setValue(this.wrapper.invokeSetMethod(value));
    }
  }

  @SuppressWarnings("unchecked")
  public T get() {
    T value;
    if (this.wrapper == null) {
      value = (T) this.pointer.getValue();
    } else {
      value = (T) this.wrapper.invokeGetMethod(this.pointer.getValue());
    }
    return value;
  }
  
  @SuppressWarnings("unchecked")
  public T getClonedValue() {
    T value = this.get();
    if (value instanceof Cloneable) {
    	Cloneable cloneableObject = (Cloneable) value;
    	try {
			Method cloneMethod = cloneableObject.getClass().getMethod("clone");
			return (T) cloneMethod.invoke(cloneableObject);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} 
	}
    return value;
  }
  

	@Override
	public int compareTo(Field that) {
		return this.getPath().compareTo(that.getPath());
	}
}
