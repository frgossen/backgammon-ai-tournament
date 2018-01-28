package com.frederikgossen.backgammon.model;

import com.frederikgossen.backgammon.model.GameResult.Reason;

public class MatchResult {

	private PlayerStats whitePlayerStats;
	private PlayerStats blackPlayerStats;

	public MatchResult(Player whitePlayer, Player blackPlayer) {
		this.whitePlayerStats = new PlayerStats(whitePlayer);
		this.blackPlayerStats = new PlayerStats(blackPlayer);
	}

	public MatchResult(MatchResult matchResult) {
		this.whitePlayerStats = new PlayerStats(matchResult.whitePlayerStats);
		this.blackPlayerStats = new PlayerStats(matchResult.blackPlayerStats);
	}

	public PlayerStats getWhitePlayerStats() {
		return whitePlayerStats;
	}

	public PlayerStats getBlackPlayerStats() {
		return blackPlayerStats;
	}

	public void inc(GameResult gameResult) {
		Reason reason = gameResult.getReason();
		boolean isWhitePlayer = whitePlayerStats.getPlayer() == gameResult.getPlayer();
		PlayerStats playerStats = isWhitePlayer ? whitePlayerStats : blackPlayerStats;
		if (reason == Reason.FINISHED) {
			playerStats.incWins();
			PlayerStats otherPlayerStats = isWhitePlayer ? blackPlayerStats : whitePlayerStats;
			otherPlayerStats.incLosses();
		} else if (reason == Reason.CHEATED) {
			playerStats.incCheats();
		} else if (reason == Reason.CRASHED) {
			playerStats.incCrashes();
		}
	}

	public void inc(MatchResult matchResult) {
		whitePlayerStats.inc(matchResult.whitePlayerStats);
		blackPlayerStats.inc(matchResult.blackPlayerStats);
	}

	@Override
	public String toString() {
		return whitePlayerStats + System.lineSeparator() + blackPlayerStats + System.lineSeparator();
	}
}
