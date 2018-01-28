package com.frederikgossen.backgammon.model;

import java.security.InvalidParameterException;

public class PlayerStats {

	private Player player;
	private int countWins;
	private int countLosses;
	private int countCheats;
	private int countCrashes;

	public PlayerStats(Player player) {
		this.player = player;
		this.countWins = 0;
		this.countLosses = 0;
		this.countCheats = 0;
		this.countCrashes = 0;
	}

	public PlayerStats(PlayerStats playerStats) {
		this.player = new Player(playerStats.player);
		this.countWins = playerStats.countWins;
		this.countLosses = playerStats.countLosses;
		this.countCheats = playerStats.countCheats;
		this.countCrashes = playerStats.countCrashes;
	}

	public void incWins() {
		countWins++;
	}

	public void incLosses() {
		countLosses++;
	}

	public void incCheats() {
		countCheats++;
	}

	public void incCrashes() {
		countCrashes++;
	}

	public Player getPlayer() {
		return player;
	}

	public int countWins() {
		return countWins;
	}

	public int countLosses() {
		return countLosses;
	}

	public int countCheats() {
		return countCheats;
	}

	public int countCrashes() {
		return countCrashes;
	}

	public void inc(PlayerStats playerStats) {
		if (player != playerStats.getPlayer())
			throw new InvalidParameterException("player not equal");
		this.countWins += playerStats.countWins;
		this.countLosses += playerStats.countLosses;
		this.countCheats += playerStats.countCheats;
		this.countCrashes += playerStats.countCrashes;
	}

	@Override
	public String toString() {
		return player.getName() + " won " + countWins + " time(s), lost " + countLosses + " time(s), cheated "
				+ countCheats + " time(s) and crashed " + countCrashes + " time(s)";
	}
}
