package com.frederikgossen.backgammon.model;

import java.util.ArrayList;
import java.util.Iterator;

public class DiceValues implements Iterable<Integer> {

	private ArrayList<Integer> values;

	public DiceValues() {
		values = new ArrayList<Integer>(2);
	}

	public DiceValues(DiceValues dieceResult) {
		this();
		values.addAll(dieceResult.values);
	}

	public void setValues(int value0, int value1) {
		values.clear();
		values.add(value0);
		values.add(value1);
	}

	public int getValue(int i) {
		return values.get(i);
	}

	public boolean containValue(int expectedValue) {
		return values.contains(expectedValue);
	}

	public void removeValue(int value) {
		values.remove(new Integer(value));
	}

	public int countValues() {
		return values.size();
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}

	public Iterator<Integer> iterator() {
		return values.iterator();
	}

	public boolean isMoveValid(Move move) {
		return containValue(move.getDiff());
	}

	public void clearValues() {
		values.clear();
	}

	@Override
	public String toString() {
		return values.toString();
	}
}
