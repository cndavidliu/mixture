package com.mauvesu.mixture.java.lang.reflect;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ReflectTest {
	
	@Rule
	public ExpectedException expected = ExpectedException.none();
	
	@Test
	public void testConstruct() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Sample sample = Sample.class.newInstance();
		assertNull(sample.getName());
		assertEquals(-1, sample.getId());
		
		@SuppressWarnings("unchecked")
		Constructor<Sample>[] constructors = (Constructor<Sample>[]) sample.getClass().getConstructors();
		assertEquals(2, constructors.length);
		for(Constructor<Sample> constructor : constructors) {
			assertFalse(constructor.isAccessible());
			if(constructor.getParameterCount() == 0) {
				sample = constructor.newInstance(new Object[]{});
				assertNull(sample.getName());
			} else if (constructor.getParameterCount() == 2) {
				sample = constructor.newInstance(new Object[]{"test", 1});
				assertEquals("test", sample.getName());
				assertEquals(1, sample.getId());
			}
		}
		
		Constructor<Sample> constructor = Sample.class.getConstructor(new Class<?>[]{String.class, int.class});
		sample = constructor.newInstance(new Object[]{"try", "try".hashCode()});
		assertEquals("try", sample.getName());
		
		expected.expect(NoSuchMethodException.class);
		constructor = Sample.class.getConstructor(new Class<?>[]{String.class});
	}
	
	@Test
	public void testMethod() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Sample sample = new Sample("content", 1);
		Method[] methods = sample.getClass().getMethods();
		Method[] declaredMethods = sample.getClass().getDeclaredMethods();
		assertEquals(8, declaredMethods.length);
		assertEquals(16, methods.length);
		
		Method method = sample.getClass().getMethod("main", new Class<?>[]{String[].class});
		try {
			method.invoke(sample, new Object[]{new String[]{}});
		} catch (InvocationTargetException e) {
			
		}
		method = sample.getClass().getMethod("talk", new Class<?>[]{String.class});
		method.invoke(sample, new Object[]{"test"});
		try {
			method = sample.getClass().getDeclaredMethod("say", new Class<?>[]{String.class});
			method.invoke(sample, new Object[]{"test"});
			fail();
		} catch (NoSuchMethodException e) {
		}
		method = sample.getClass().getDeclaredMethod("hash", new Class<?>[]{});
		try {
			method.invoke(sample, new Object[]{});
			fail();
		} catch (IllegalAccessException e) {
		}
		method.setAccessible(true);
		System.out.println(method.invoke(sample, new Object[]{}));
		try {
			method = sample.getClass().getMethod("hash", new Class<?>[]{});
			fail();
		} catch (NoSuchMethodException e) {			
		}
		method = sample.getClass().getMethod("wait", new Class<?>[]{});
		try {
			method = sample.getClass().getDeclaredMethod("wait", new Class<?>[]{});
			fail();
		} catch (NoSuchMethodException e) {
		}
		expected.expect(NoSuchMethodException.class);
		sample.getClass().getDeclaredMethod("say", new Class<?>[]{String.class});
	}
	
	@Test
	public void testApis() {
		assertFalse(Sample.class.isInterface());
		assertTrue(Sample.class.isInstance(new Sample()));
		assertFalse(Sample.class.isInstance(new String()));
		assertTrue(Sample.class.isInstance(new SampleChild()));
		assertFalse(SampleChild.class.isInstance(new Sample()));
		System.out.println(Sample.class.getPackage());
	}
	
	private static class SampleChild extends Sample {		
	}
}
