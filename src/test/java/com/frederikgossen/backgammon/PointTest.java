package com.frederikgossen.backgammon;

import com.frederikgossen.backgammon.model.Point;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PointTest extends TestCase {

	public PointTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(PointTest.class);
	}

	public void testSequenceOfOperationsOnMyCheckers() {
		Point p = new Point();
		assertEquals(0, p.getMyCheckers());
		assertEquals(0, p.getYourCheckers());

		p.incMyCheckers();
		assertEquals(1, p.getMyCheckers());
		assertEquals(0, p.getYourCheckers());

		p.incMyCheckers();
		p.incMyCheckers();
		p.incMyCheckers();
		assertEquals(4, p.getMyCheckers());
		assertEquals(0, p.getYourCheckers());

		p.incMyCheckers(2);
		p.incMyCheckers(3);
		assertEquals(9, p.getMyCheckers());
		assertEquals(0, p.getYourCheckers());

		p.decMyCheckers();
		p.decMyCheckers();
		assertEquals(7, p.getMyCheckers());
		assertEquals(0, p.getYourCheckers());

		p.incMyCheckers(-3);
		assertEquals(4, p.getMyCheckers());
		assertEquals(0, p.getYourCheckers());

		p.incMyCheckers(-4);
		assertEquals(0, p.getMyCheckers());
		assertEquals(0, p.getYourCheckers());
	}

	public void testSequenceOfOperationsOnYourCheckers() {
		Point p = new Point();
		assertEquals(0, p.getYourCheckers());
		assertEquals(0, p.getMyCheckers());

		p.incYourCheckers();
		assertEquals(1, p.getYourCheckers());
		assertEquals(0, p.getMyCheckers());

		p.incYourCheckers();
		p.incYourCheckers();
		p.incYourCheckers();
		assertEquals(4, p.getYourCheckers());
		assertEquals(0, p.getMyCheckers());

		p.incYourCheckers(2);
		p.incYourCheckers(3);
		assertEquals(9, p.getYourCheckers());
		assertEquals(0, p.getMyCheckers());

		p.decYourCheckers();
		p.decYourCheckers();
		assertEquals(7, p.getYourCheckers());
		assertEquals(0, p.getMyCheckers());

		p.incYourCheckers(-3);
		assertEquals(4, p.getYourCheckers());
		assertEquals(0, p.getMyCheckers());

		p.incYourCheckers(-4);
		assertEquals(0, p.getYourCheckers());
		assertEquals(0, p.getMyCheckers());
	}

	public void testEmptyness() {
		Point p = new Point();
		assertTrue(p.isEmpty());

		p.setMyCheckers(3);
		assertFalse(p.isEmpty());

		p.setYourCheckers(5);
		assertFalse(p.isEmpty());

		p.clearMyCheckers();
		assertFalse(p.isEmpty());

		p.setYourCheckers(0);
		assertTrue(p.isEmpty());
	}
}
