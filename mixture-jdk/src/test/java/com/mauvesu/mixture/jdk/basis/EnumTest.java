package com.mauvesu.mixture.jdk.basis;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.mauvesu.mixture.jdk.basis.Subject;

public class EnumTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testEnum() {
		Subject subject = Subject.CHINESE;
		assertTrue(Subject.valueOf("CHINESE") instanceof Subject);
		assertEquals(subject, Subject.valueOf("CHINESE"));
		this.exception.expect(IllegalArgumentException.class);
		Subject.valueOf("Chinese");
		assertEquals(3, Subject.values().length);
	}
}
