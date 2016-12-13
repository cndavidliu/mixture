package com.mauvesu.mixture.java.util;

import java.util.Enumeration;
import java.util.Hashtable;

import org.junit.Test;
import static org.junit.Assert.*;

public class BaseEnumerationTest {
	
	/**
	 * Vector HashTable's Enumeration synchronized
	 */
	@Test
	public void test_enumeration() {
		Hashtable<String, Integer> table = new Hashtable<String, Integer>();
		int len = 3;
		this.initial_table(table, len);
		Enumeration<Integer> enumeration = table.elements();
		int i = 0;
		while(enumeration.hasMoreElements()) {
			table.remove(String.valueOf(len / 2));
			enumeration.nextElement();
			i ++;
		}
		assertEquals(len - 1, i);
		assertEquals(len - 1, table.size());
	}
	
	private void initial_table(Hashtable<String, Integer> table, int len) {
		for (int i = 0; i < len; i++) {
			table.put(String.valueOf(i), i);
		}
	}
}
