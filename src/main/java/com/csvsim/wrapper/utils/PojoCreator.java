
package com.csvsim.wrapper.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.csvsim.wrapper.Field;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

public class PojoCreator {

	@SuppressWarnings("rawtypes")
	public static Class createPojo(String className, LinkedList<Field> fields, LinkedList<Field> auxFields) throws NotFoundException, CannotCompileException {

		ClassPool pool = ClassPool.getDefault();
		CtClass cc = pool.makeClass(className);

		// add this to define an interface to implement
		cc.addInterface(resolveCtClass(Serializable.class));

		if(fields!=null){
			for (Field field : fields) {
				if(field.getConstantValue() == null){
					cc.addField(new CtField(resolveCtClass(field.getFieldClass()), field.getName(), cc));
					cc.addMethod(generateGetter(cc, field.getName(), field.getFieldClass()));
					cc.addMethod(generateSetter(cc, field.getName(), field.getFieldClass()));
				}
			}
		}
	
		if(auxFields!=null){
			for (Field field : auxFields) {
				if(field.getConstantValue() == null){
					cc.addField(new CtField(resolveCtClass(field.getFieldClass()), field.getName(), cc));
					cc.addMethod(generateGetter(cc, field.getName(), field.getFieldClass()));
					cc.addMethod(generateSetter(cc, field.getName(), field.getFieldClass()));
				}
			}
		}
		
		cc.addMethod(generateToString(cc, fields));

		return cc.toClass();
	}
	

	@SuppressWarnings("rawtypes")
	private static CtMethod generateGetter(CtClass declaringClass, String fieldName, Class fieldClass)
			throws CannotCompileException {

		String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

		StringBuffer sb = new StringBuffer();
		sb.append("public ").append(fieldClass.getName()).append(" ")
				.append(getterName).append("(){").append("return this.")
				.append(fieldName).append(";").append("}");
		return CtMethod.make(sb.toString(), declaringClass);
	}

	@SuppressWarnings("rawtypes")
	private static CtMethod generateSetter(CtClass declaringClass, String fieldName, Class fieldClass) throws CannotCompileException {

		String setterName = "set" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);

		StringBuffer sb = new StringBuffer();
		sb.append("public void ").append(setterName).append("(")
				.append(fieldClass.getName()).append(" ").append(fieldName)
				.append(")").append("{").append("this.").append(fieldName)
				.append("=").append(fieldName).append(";").append("}");
		return CtMethod.make(sb.toString(), declaringClass);
	}
	
	private static void addCvsField(StringBuffer sb, Field field){
		sb.append("builder.append(");
		if(field.getFormat()!=null){
			if(field.getConstantValue() == null){				
				if(field.getFieldClass() == Date.class){
					sb.append("String.format(\"").append(field.getFormat()).append("\",  new Object[]{");					
					for(int i = 1; i<numberOfDateFormats(field.getFormat()); i++){
						sb.append("this.").append(field.getName()).append(",");
					}
					sb.append("this.").append(field.getName()).append("})");
				}
				else{				
					sb.append("String.format(\"").append(field.getFormat()).append("\",  new Object[]{this.").append(field.getName()).append("})");
				}
			}
			else{
				if(field.getFieldClass() == String.class){
					sb.append("String.format(\"").append(field.getFormat()).append("\",  new Object[]{\"").append(field.getConstantValue()).append("\"})");
				}				
				else{
					sb.append("String.format(\"").append(field.getFormat()).append("\",  new Object[]{").append(field.getConstantValue()).append("})");
				}				
			}			
		}
		else if(field.getFieldClass() == String.class){
			if(field.getConstantValue() == null){
				sb.append("\"\\\"\"+String.valueOf(this.").append(field.getName()).append(")+\"\\\"\"");
			}
			else{
				sb.append("\"\\\"\"+\"").append(field.getConstantValue()).append("\"+\"\\\"\"");
			}
		}
		else{
			if(field.getConstantValue() == null){
				sb.append("String.valueOf(this.").append(field.getName()).append(")");
			}
			else{
				sb.append(field.getConstantValue());
			}
		}
	}

	private static CtMethod generateToString(CtClass declaringClass,  LinkedList<Field> properties) throws CannotCompileException {

		StringBuffer sb = new StringBuffer();
		sb.append("public String toString() { StringBuilder builder = new StringBuilder(); ");

		Iterator<Field> itr = properties.iterator();		
		Field field =  itr.next();
		while(itr.hasNext()){
			addCvsField(sb, field);
			sb.append("+\", \"); ");
			field = itr.next();
		}		
		if(field!=null){
			addCvsField(sb, field);			
			sb.append("); ");
		}				
		sb.append("return builder.toString(); }");
		return CtMethod.make(sb.toString(), declaringClass);
	}	
	
	@SuppressWarnings("rawtypes")
	private static CtClass resolveCtClass(Class clazz) throws NotFoundException {
		ClassPool pool = ClassPool.getDefault();
		return pool.get(clazz.getName());
	}
	
	private static int numberOfDateFormats(String format){
		Pattern pattern = Pattern.compile("%t");
		Matcher matcher = pattern.matcher(format);

		int count = 0;
		while (matcher.find())
			count++;
		return count;
	}

	
	
}