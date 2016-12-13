package com.mauvesu.mixture.java.util;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BaseIteratorTest {
	
	@Rule
	public ExpectedException expected = ExpectedException.none();
	
	@Test
	public void test_iterate_list() {
		int len = 10;
		ArrayList<Integer> list = new ArrayList<Integer>();
		intialList(list, len);
		Iterator<Integer> iterator = list.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
		assertEquals(0, list.size());
		intialList(list, len);
		iterator = list.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			int item = list.get(i);
			list.set(i, list.get(i ++) + 1);
			assertEquals(item + 1, iterator.next().intValue());
		}
		try {
			for (Integer integer : list) {
				list.remove(integer);
			}
			fail();
		} catch (ConcurrentModificationException e) {
		}
		expected.expect(ConcurrentModificationException.class);
		iterator = list.iterator();
		while (iterator.hasNext()) {
			list.remove(iterator.next());
		}
	}
	
	/**
	 * test for fast-safe iterator in concurrent package
	 */
	@Test
	public void test_iterate_concurrent_list() {
		CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<Integer>();
		this.intialList(list, 10);
		for (Integer integer : list) {
			list.remove(integer);
		}
		assertEquals(0, list.size());
		this.intialList(list, 10);
		Iterator<Integer> iterator = list.iterator();
		while (iterator.hasNext()) {
			list.remove(iterator.next());
		}
		assertEquals(0, list.size());
		int len = 10;
		this.intialList(list, len);
		iterator = list.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			list.remove(new Integer(len / 2));
			iterator.next();
			i ++;
		}
		assertEquals(len, i);
		assertEquals(len - 1, list.size());
	}
	
	private void intialList(List<Integer> list, int len) {
		for (int i = 0; i < len; i ++) {
			list.add(i);
		}
	}

}
