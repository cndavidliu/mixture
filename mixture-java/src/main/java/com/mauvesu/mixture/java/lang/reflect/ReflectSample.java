package com.mauvesu.mixture.java.lang.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

abstract class Base {
	
	public abstract boolean ifMapping(); 
	
	private String say(String content) {
		return String.format("Hello %s!", content);
	}
	
	private String response(String content) {
		return String.format("Response to %s!", content);
	}
	
	public void talk(String content) {
		System.out.println(this.say(content));
		System.out.println(this.response(content));
	}
}

/**
 * sample used to reflect and test 
 *
 * @author mauvesu
 *
 */
public class ReflectSample extends Base {
	
	private static final int MAX_LEN = 10;
	
	private String name;
	private int id;
	
	public ReflectSample() {
		this(null, -1);
	}
	
	public ReflectSample(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	private int hash() {
		return this.name.hashCode();
	}

	@Override
	public boolean ifMapping() {
		return this.id == this.hash() % MAX_LEN;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		ReflectSample sample = ReflectSample.class.newInstance();
		Method method = ReflectSample.class.getMethod("talk", new Class<?>[]{String.class});
		method.invoke(sample, new Object[]{"test"});
		method = ReflectSample.class.getDeclaredMethod("hash", new Class<?>[]{});
		method.setAccessible(true);
		method.invoke(sample, new Object[]{});
	}
}