package com.mauvesu.mixture.java.lang.reflect;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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
		ReflectSample sample = ReflectSample.class.newInstance();
		assertNull(sample.getName());
		assertEquals(-1, sample.getId());
		
		@SuppressWarnings("unchecked")
		Constructor<ReflectSample>[] constructors = (Constructor<ReflectSample>[]) sample.getClass().getConstructors();
		assertEquals(2, constructors.length);
		for(Constructor<ReflectSample> constructor : constructors) {
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
		
		Constructor<ReflectSample> constructor = ReflectSample.class.getConstructor(new Class<?>[]{String.class, int.class});
		sample = constructor.newInstance(new Object[]{"try", "try".hashCode()});
		assertEquals("try", sample.getName());
		
		expected.expect(NoSuchMethodException.class);
		constructor = ReflectSample.class.getConstructor(new Class<?>[]{String.class});
	}
	
	@Test
	public void testMethod() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ReflectSample sample = new ReflectSample("content", 1);
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
		
		//invoke static method
		method = ReflectSample.class.getMethod("main", new Class<?>[]{String[].class});
		try {
			method.invoke(ReflectSample.class, new Object[]{new String[]{}});
			fail();
		} catch (InvocationTargetException e) {
		}
		
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
		assertFalse(ReflectSample.class.isInterface());
		assertTrue(ReflectSample.class.isInstance(new ReflectSample()));
		assertFalse(ReflectSample.class.isInstance(new String()));
		assertTrue(ReflectSample.class.isInstance(new SampleChild()));
		assertFalse(SampleChild.class.isInstance(new ReflectSample()));
		System.out.println(ReflectSample.class.getPackage());
	}
	
	@Test
	public void testField() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		// private static field
		Field field = ReflectSample.class.getDeclaredField("MAX_LEN");
		field.setAccessible(true);
		System.out.println(field.get(ReflectSample.class));
		//private field
		ReflectSample sample = new ReflectSample(null, 1);
		field = ReflectSample.class.getDeclaredField("id");
		field.setAccessible(true);
		assertEquals(sample.getId(), field.get(sample));
		field.setInt(sample, 2);
		assertEquals(2, sample.getId());	
	}
	
	private static class SampleChild extends ReflectSample {		
	}
}
