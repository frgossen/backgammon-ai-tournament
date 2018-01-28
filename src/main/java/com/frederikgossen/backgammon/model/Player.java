package com.frederikgossen.backgammon.model;

public class Player {

	private String name;

	public Player(String name) {
		this.name = name;
	}

	public Player(Player player) {
		this(player.name);
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
