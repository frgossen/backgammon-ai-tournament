package com.frederikgossen.backgammon.gamelogic;

import java.util.ArrayList;

import com.frederikgossen.backgammon.gamelogic.Match.MatchListener;
import com.frederikgossen.backgammon.model.Board;
import com.frederikgossen.backgammon.model.DiceValues;
import com.frederikgossen.backgammon.model.MatchResult;
import com.frederikgossen.backgammon.model.ModelPerspective;
import com.frederikgossen.backgammon.model.Move;
import com.frederikgossen.backgammon.model.Player;
import com.frederikgossen.backgammon.model.TournamentResult;

public class Tournament {

	public interface TournamentListener {

		public void onBeforeMatch(int i, int n, Player whitePlayer, Player blackPlayer);

		public void onAfterMatch(int i, int n);

		public void onBeforeGame(int i, int n, Player whitePlayer, Player blackPlayer);

		public void onAfterGame(int i, int n);

		public void onProgress(double p);

		public void onBeforeMove(ModelPerspective persp, Board b, DiceValues diceVs, int ithMoveInTurn,
				PlayerAndAI activePlayer, PlayerAndAI otherPlayer, Move m);

		public void onAfterMove(ModelPerspective persp, Board b, DiceValues diceVs, int ithMoveInTurn,
				PlayerAndAI activePlayer, PlayerAndAI otherPlayer, Move m);

		public boolean isCancelled();
	}

	private Match match;
	private TournamentListener tournamentListener;
	private ArrayList<PlayerAndAI> playerAndAIs;
	private ArrayList<Player> player;
	private double progressOffset;
	private double progressFactor;

	public Tournament(long randomSeed) {
		this.match = new Match(randomSeed);
		this.tournamentListener = null;
		this.playerAndAIs = new ArrayList<PlayerAndAI>();
		this.player = new ArrayList<Player>();
	}

	public void setTournamentListener(final TournamentListener tournamentListener) {
		this.tournamentListener = tournamentListener;
		match.setMatchListener(new MatchListener() {

			public void onBeforeGame(int i, int n, Player whitePlayer, Player blackPlayer) {
				tournamentListener.onBeforeGame(i, n, whitePlayer, blackPlayer);
			}

			public void onAfterGame(int i, int n) {
				tournamentListener.onAfterGame(i, n);
			}

			public void onProgress(double p) {
				double progressTotal = progressOffset + progressFactor * p;
				tournamentListener.onProgress(progressTotal);
			}

			public void onBeforeMove(ModelPerspective persp, Board b, DiceValues diceVs, int ithMoveInTurn,
					PlayerAndAI activePlayer, PlayerAndAI otherPlayer, Move m) {

				tournamentListener.onBeforeMove(persp, b, diceVs, ithMoveInTurn, activePlayer, otherPlayer, m);
			}

			public void onAfterMove(ModelPerspective persp, Board b, DiceValues diceVs, int ithMoveInTurn,
					PlayerAndAI activePlayer, PlayerAndAI otherPlayer, Move m) {

				tournamentListener.onAfterMove(persp, b, diceVs, ithMoveInTurn, activePlayer, otherPlayer, m);
			}

			public boolean isCancelled() {
				return tournamentListener.isCancelled();
			}
		});
	}

	public void setPlayer(Iterable<PlayerAndAI> playerAndAIs) {
		this.playerAndAIs.clear();
		this.player.clear();
		for (PlayerAndAI p : playerAndAIs) {
			this.playerAndAIs.add(p);
			this.player.add(p.getPlayer());
		}
	}

	public TournamentResult play(int nGames) {
		TournamentResult tournamentResult = new TournamentResult(player.toArray(new Player[0]));
		int i = 0;
		int nPlayer = playerAndAIs.size();
		int nMatches = nPlayer * (nPlayer - 1) / 2;
		progressFactor = 1.0 / nMatches;

		for (int iPlayer0 = 0; iPlayer0 < nPlayer; iPlayer0++) {
			PlayerAndAI player0 = playerAndAIs.get(iPlayer0);
			for (int iPlayer1 = iPlayer0 + 1; iPlayer1 < nPlayer; iPlayer1++) {
				PlayerAndAI player1 = playerAndAIs.get(iPlayer1);

				/* Publish match begins */
				if (tournamentListener != null)
					tournamentListener.onBeforeMatch(i, nMatches, player0.getPlayer(), player1.getPlayer());

				match.setPlayer(player0, player1);
				progressOffset = 1.0 * i / nMatches;
				MatchResult matchResult = match.play(nGames);
				tournamentResult.inc(matchResult);

				/* Publish match finished */
				if (tournamentListener != null) {
					tournamentListener.onAfterMatch(i, nMatches);
					tournamentListener.onProgress(1.0 * (i + 1) / nMatches);
				}
				i++;
			}
		}
		return tournamentResult;
	}
}
