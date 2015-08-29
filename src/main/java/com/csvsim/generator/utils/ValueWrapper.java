package com.csvsim.generator.utils;

import java.lang.reflect.Method;
import java.util.TreeMap;

import org.apache.commons.collections.bidimap.TreeBidiMap;

import com.csvsim.random.utils.Utils.checkNullStringMethod;

public class ValueWrapper {

  Object setObject;
  public Object setObject() {
    return this.setObject;
  }

  Method setMethod;
  public Method setMethod() {
    return this.setMethod;
  }

  Object getObject;
  public Object getObject() {
    return this.getObject;
  }

  Method getMethod;
  public Method getMethod() {
    return this.getMethod;
  }

  @SuppressWarnings("rawtypes")
  Class classArg;

  @SuppressWarnings("rawtypes")
  public Class classArg() {
    return this.classArg;
  }

  @SuppressWarnings("rawtypes")
  Class returnType;

  ValueWrapper valueWrapper = null;

  @SuppressWarnings("rawtypes")
  private Method initMethod(Object object, String method, Class classArg) throws SecurityException, NoSuchMethodException {
    Method methodAux = null;
    try {
      methodAux = object.getClass().getMethod(method, classArg);
    } catch (NoSuchMethodException noSuchMethodException) {

      if (classArg == java.lang.Boolean.class) {
        methodAux = object.getClass().getMethod(method, boolean.class);
      } else if (classArg == java.lang.Integer.class) {
        methodAux = object.getClass().getMethod(method, int.class);
      } else if (classArg == java.lang.Short.class) {
        methodAux = object.getClass().getMethod(method, short.class);
      } else if (classArg == java.lang.Long.class) {
        methodAux = object.getClass().getMethod(method, long.class);
      } else if (classArg == java.lang.Float.class) {
        methodAux = object.getClass().getMethod(method, float.class);
      } else if (classArg == java.lang.Double.class) {
        methodAux = object.getClass().getMethod(method, double.class);
      } else if (classArg == java.lang.Character.class) {
        methodAux = object.getClass().getMethod(method, char.class);
      } else if (classArg == java.lang.Byte.class) {
        methodAux = object.getClass().getMethod(method, byte.class);
      } else {
        throw noSuchMethodException;
      }
    }
    return methodAux;
  }
  @SuppressWarnings("rawtypes")
  private void init(Object setObject, String setMethod, Object getObject, String getMethod, Class classArg, Class returnType, ValueWrapper valueWrapper) throws SecurityException,
      NoSuchMethodException {
    this.setObject = setObject;
    this.getObject = getObject;
    this.classArg = classArg;
    this.valueWrapper = valueWrapper;
    this.returnType = returnType;
    this.setMethod = this.initMethod(setObject, setMethod, classArg);
    this.getMethod = this.initMethod(getObject, getMethod, returnType);
  }

  @SuppressWarnings("rawtypes")
  protected ValueWrapper(Object setObject, String setMethod, Object getObject, String getMethod, Class classArg, Class returnType, ValueWrapper valueWrapper) {
    try {
      this.init(setObject, setMethod, getObject, getMethod, classArg, returnType, valueWrapper);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static ValueWrapper getWrapper(TreeMap<String, String> map) {
    TreeBidiMap bidiMap = new TreeBidiMap(map);
    final ValueWrapper treeMapWrapper = new ValueWrapper(bidiMap, "get", bidiMap, "getKey", Object.class, Object.class, null);

    return new ValueWrapper(new checkNullStringMethod() {
      @Override
      public String get(Object argument) {
        String result = null;
        if (argument != null) {
          result = (String) treeMapWrapper.invokeSetMethod(argument);
        }
        return result;
      }
    }, "get", new checkNullStringMethod() {
      @Override
      public String get(Object argument) {
        String result = null;
        if (argument != null) {
          result = (String) treeMapWrapper.invokeGetMethod(argument);
        }
        return result;
      }
    }, "get", Object.class, Object.class, null);
  }

  @SuppressWarnings("rawtypes")
  public Class getInputClass() {
    return this.valueWrapper == null ? this.classArg : this.valueWrapper.getInputClass();
  }

  @SuppressWarnings("rawtypes")
  public Class getOutputClass() {
    return this.returnType == null ? this.setMethod.getReturnType() : this.returnType;
  }

  public Object invokeSetMethod(Object argument) {
    Object result = null;
    try {
      if (this.valueWrapper == null) {
        result = this.setMethod.invoke(this.setObject, argument);
      } else {
        result = this.setMethod.invoke(this.setObject, this.valueWrapper.invokeSetMethod(argument));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  public Object invokeGetMethod(Object argument) {
    Object result = null;
    try {
      if (this.valueWrapper == null) {
        result = this.getMethod.invoke(this.getObject, argument);
      } else {
        result = this.getMethod.invoke(this.getObject, this.valueWrapper.invokeGetMethod(argument));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }
}