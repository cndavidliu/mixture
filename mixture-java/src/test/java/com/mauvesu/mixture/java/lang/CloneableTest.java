package com.mauvesu.mixture.java.lang;

import static org.junit.Assert.*;
import org.junit.Test;
import static com.mauvesu.mixture.java.lang.CloneableSample.*;

public class CloneableTest {
		
	@Test
	public void testDeepClone() throws CloneNotSupportedException {
		CloneableSample sample = new CloneableSample("father", new Daughter("daughter"));
		CloneableSample clone = (CloneableSample)sample.clone();
		
		assertNotEquals(sample, clone);
		assertEquals(sample.getName(), clone.getName());
		assertNotEquals(sample.getDaughter(), clone.getDaughter());
		assertEquals(sample.getDaughter().getName(), clone.getDaughter().getName());
		
		sample.setName("grandpa");
		assertNotEquals(sample.getName(), clone.getName());
		assertEquals("father", clone.getName());
		
		sample.getDaughter().setName("sister");
		assertNotEquals(sample.getDaughter().getName(), clone.getDaughter().getName());
		assertEquals("daughter", clone.getDaughter().getName());
	}
	
	@Test
	public void testClone() throws CloneNotSupportedException {
		Son original = new Son("son", new Daughter("daughter"));
		Son clone = (Son)original.clone();
		
		assertNotEquals(original, clone);
		assertEquals(original.getName(), clone.getName());
		assertEquals(original.getDaughter(), original.getDaughter());
		
		original.setName("father");
		assertNotEquals(original.getName(), clone.getName());
		assertEquals("son", clone.getName());
		
		original.getDaughter().setName("sister");
		assertEquals(original.getDaughter(), clone.getDaughter());
		assertEquals("sister", clone.getDaughter().getName());
		
		original.setDaughter(new Daughter("daughter"));
		assertNotEquals(original.getDaughter(), clone.getDaughter());
		assertEquals("sister", clone.getDaughter().getName());
	}

}
