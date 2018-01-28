package com.frederikgossen.backgammon.model;

public class Move {

	private int from;
	private int to;

	public Move(int from, int to) {
		this.from = from;
		this.to = to;
	}

	public Move(Move move) {
		this.from = move.from;
		this.to = move.to;
	}

	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}

	public int getDiff() {
		return to - from;
	}

	@Override
	public String toString() {
		return from + " -> " + to;
	}
}
