package com.frederikgossen.backgammon;

import com.frederikgossen.backgammon.model.DiceValues;
import com.frederikgossen.backgammon.model.Move;
import com.frederikgossen.backgammon.model.Point;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DiceValuesTest extends TestCase {

	public DiceValuesTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(DiceValuesTest.class);
	}

	public void testMoveValid() {
		DiceValues d = new DiceValues();
		assertTrue(d.isEmpty());
		assertEquals(0, d.countValues());

		Move m4 = new Move(3, 7);
		Move m6 = new Move(3, 9);
		assertFalse(d.isMoveValid(m4));
		assertFalse(d.isMoveValid(m6));

		d.setValues(1, 6);
		assertFalse(d.isMoveValid(m4));
		assertTrue(d.isMoveValid(m6));
		assertEquals(2, d.countValues());

		d.setValues(4, 5);
		assertTrue(d.isMoveValid(m4));
		assertFalse(d.isMoveValid(m6));
		assertEquals(2, d.countValues());

		d.removeValue(5);
		assertTrue(d.isMoveValid(m4));
		assertFalse(d.isMoveValid(m6));
		assertEquals(1, d.countValues());

		d.removeValue(4);
		assertFalse(d.isMoveValid(m4));
		assertFalse(d.isMoveValid(m6));
		assertEquals(0, d.countValues());
		assertTrue(d.isEmpty());
	}

	public void testDuplicateValue() {
		DiceValues d = new DiceValues();
		d.setValues(3, 3);
		assertEquals(2, d.countValues());

		d.removeValue(3);
		assertEquals(1, d.countValues());

		d.removeValue(3);
		assertTrue(d.isEmpty());
	}
}
