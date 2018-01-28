package com.frederikgossen.backgammon.model;

public class GameResult {

	public enum Reason {
		FINISHED, CHEATED, CRASHED, CANCELLED
	}

	private Player player;
	private Reason reason;

	public GameResult(Player player, Reason reason) {
		this.player = player;
		this.reason = reason;
	}

	public GameResult(GameResult gameResult) {
		this.player = new Player(gameResult.player);
		this.reason = gameResult.reason;
	}

	public Player getPlayer() {
		return player;
	}

	public Reason getReason() {
		return reason;
	}
}
