package com.frederikgossen.backgammon;

public class SynchronizedVariable<T> {

	private T x;

	public SynchronizedVariable(T x) {
		set(x);
	}

	public synchronized T get() {
		return x;
	}

	public synchronized void set(T x) {
		this.x = x;
	}
}
