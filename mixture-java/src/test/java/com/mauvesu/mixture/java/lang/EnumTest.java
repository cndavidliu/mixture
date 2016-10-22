package com.mauvesu.mixture.java.lang;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.mauvesu.mixture.java.lang.EnumSample;

public class EnumTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testEnum() {
		EnumSample subject = EnumSample.CHINESE;
		assertTrue(EnumSample.valueOf("CHINESE") instanceof EnumSample);
		assertEquals(subject, EnumSample.valueOf("CHINESE"));
		this.exception.expect(IllegalArgumentException.class);
		EnumSample.valueOf("Chinese");
		assertEquals(3, EnumSample.values().length);
	}
}
