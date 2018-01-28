package com.frederikgossen.backgammon;

import com.frederikgossen.backgammon.model.Board;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class BoardTest extends TestCase {

	public BoardTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(BoardTest.class);
	}

	public void testSetup() {
		Board b = new Board();
		assertTrue(b.getMyStartPoint().isEmpty());
		assertEquals(2, b.getPoint(1).getMyCheckers());
		assertEquals(0, b.getPoint(1).getYourCheckers());
		assertTrue(b.getPoint(2).isEmpty());
		assertTrue(b.getPoint(3).isEmpty());
		assertTrue(b.getPoint(4).isEmpty());
		assertTrue(b.getPoint(5).isEmpty());
		assertEquals(0, b.getPoint(6).getMyCheckers());
		assertEquals(5, b.getPoint(6).getYourCheckers());
		assertTrue(b.getPoint(7).isEmpty());
		assertEquals(0, b.getPoint(8).getMyCheckers());
		assertEquals(3, b.getPoint(8).getYourCheckers());
		assertTrue(b.getPoint(9).isEmpty());
		assertTrue(b.getPoint(10).isEmpty());
		assertTrue(b.getPoint(11).isEmpty());
		assertEquals(5, b.getPoint(12).getMyCheckers());
		assertEquals(0, b.getPoint(12).getYourCheckers());
		assertEquals(0, b.getPoint(13).getMyCheckers());
		assertEquals(5, b.getPoint(13).getYourCheckers());
		assertTrue(b.getPoint(14).isEmpty());
		assertTrue(b.getPoint(15).isEmpty());
		assertTrue(b.getPoint(16).isEmpty());
		assertEquals(3, b.getPoint(17).getMyCheckers());
		assertEquals(0, b.getPoint(17).getYourCheckers());
		assertTrue(b.getPoint(18).isEmpty());
		assertEquals(5, b.getPoint(19).getMyCheckers());
		assertEquals(0, b.getPoint(19).getYourCheckers());
		assertTrue(b.getPoint(20).isEmpty());
		assertTrue(b.getPoint(21).isEmpty());
		assertTrue(b.getPoint(22).isEmpty());
		assertTrue(b.getPoint(23).isEmpty());
		assertEquals(0, b.getPoint(24).getMyCheckers());
		assertEquals(2, b.getPoint(24).getYourCheckers());
		assertTrue(b.getMyEndPoint().isEmpty());
	}
}


