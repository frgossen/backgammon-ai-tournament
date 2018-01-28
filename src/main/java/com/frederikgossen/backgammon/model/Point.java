package com.frederikgossen.backgammon.model;

public class Point {

	private int myCheckers;
	private int yourCheckers;

	public Point() {
		myCheckers = 0;
		yourCheckers = 0;
	}

	public Point(Point point) {
		myCheckers = point.myCheckers;
		yourCheckers = point.yourCheckers;
	}

	public int getMyCheckers() {
		return myCheckers;
	}

	public void setMyCheckers(int myCheckers) {
		this.myCheckers = myCheckers;
	}

	public void clearMyCheckers() {
		this.myCheckers = 0;
	}

	public int getYourCheckers() {
		return yourCheckers;
	}

	public void setYourCheckers(int yourCheckers) {
		this.yourCheckers = yourCheckers;
	}

	public void clearYourCheckers() {
		this.yourCheckers = 0;
	}

	public void togglePerspective() {
		int tmp = myCheckers;
		myCheckers = yourCheckers;
		yourCheckers = tmp;
	}

	public boolean isMine() {
		return myCheckers > 0 && yourCheckers <= 0;
	}

	public boolean isYours() {
		return yourCheckers > 0 && myCheckers <= 0;
	}

	public boolean isEmpty() {
		return myCheckers <= 0 && yourCheckers <= 0;
	}

	public void incMyCheckers() {
		myCheckers++;
	}

	public void incMyCheckers(int n) {
		myCheckers += n;
	}

	public void decMyCheckers() {
		myCheckers--;
	}

	public void incYourCheckers() {
		yourCheckers++;
	}

	public void incYourCheckers(int n) {
		yourCheckers += n;
	}

	public void decYourCheckers() {
		yourCheckers--;
	}

	@Override
	public String toString() {
		return "(myCks: " + myCheckers + ", yourCks: " + yourCheckers + ")";
	}
}
