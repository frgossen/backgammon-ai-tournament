package com.frederikgossen.backgammon.model;

public class TournamentResult {

	private Player[] player;
	/* Entry i, j holds PlayerStats for player i's matches against plater j */
	private PlayerStats[][] playerStats;

	public TournamentResult(Player[] player) {
		this.player = player;
		int n = player.length;
		playerStats = new PlayerStats[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++)
				playerStats[i][j] = new PlayerStats(player[i]);
		}
	}

	public void inc(MatchResult matchResult) {
		PlayerStats whitePlayerStats = matchResult.getWhitePlayerStats();
		PlayerStats blackPlayerStats = matchResult.getBlackPlayerStats();
		int iWhite = findPlayerIndex(whitePlayerStats.getPlayer());
		int iBlack = findPlayerIndex(blackPlayerStats.getPlayer());
		playerStats[iWhite][iBlack].inc(whitePlayerStats);
		playerStats[iBlack][iWhite].inc(blackPlayerStats);
	}

	private int findPlayerIndex(Player p) {
		for (int i = 0; i < player.length; i++) {
			if (player[i] == p)
				return i;
		}
		return -1;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int n = playerStats.length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i != j) {
					Player opponent = player[j];
					sb.append(playerStats[i][j] + " against " + opponent.getName());
					sb.append(System.lineSeparator());
				}
			}
		}
		return sb.toString();
	}
}
