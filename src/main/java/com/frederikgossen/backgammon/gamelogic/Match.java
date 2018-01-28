package com.frederikgossen.backgammon.gamelogic;

import com.frederikgossen.backgammon.gamelogic.Game.GameListener;
import com.frederikgossen.backgammon.model.Board;
import com.frederikgossen.backgammon.model.DiceValues;
import com.frederikgossen.backgammon.model.GameResult;
import com.frederikgossen.backgammon.model.MatchResult;
import com.frederikgossen.backgammon.model.ModelPerspective;
import com.frederikgossen.backgammon.model.Move;
import com.frederikgossen.backgammon.model.Player;

public class Match {

	public interface MatchListener {

		public void onBeforeGame(int i, int n, Player whitePlayer, Player blackPlayer);

		public void onAfterGame(int i, int n);

		public void onProgress(double p);

		public void onBeforeMove(ModelPerspective persp, Board b, DiceValues diceVs, int ithMoveInTurn,
				PlayerAndAI activePlayer, PlayerAndAI otherPlayer, Move m);

		public void onAfterMove(ModelPerspective persp, Board b, DiceValues diceVs, int ithMoveInTurn,
				PlayerAndAI activePlayer, PlayerAndAI otherPlayer, Move m);

		public boolean isCancelled();
	}

	private Player whitePlayer;
	private Player blackPlayer;

	private Game game;
	private MatchListener matchListener;

	public Match(long randomSeed) {
		this.game = new Game(randomSeed);
		this.matchListener = null;
	}

	public void setMatchListener(final MatchListener matchListener) {
		this.matchListener = matchListener;
		game.setGameListener(new GameListener() {

			public void onBeforeMove(ModelPerspective persp, Board b, DiceValues diceVs, int ithMoveInTurn,
					PlayerAndAI activePlayer, PlayerAndAI otherPlayer, Move m) {

				matchListener.onBeforeMove(persp, b, diceVs, ithMoveInTurn, activePlayer, otherPlayer, m);
			}

			public void onAfterMove(ModelPerspective persp, Board b, DiceValues diceVs, int ithMoveInTurn,
					PlayerAndAI activePlayer, PlayerAndAI otherPlayer, Move m) {

				matchListener.onAfterMove(persp, b, diceVs, ithMoveInTurn, activePlayer, otherPlayer, m);
			}

			public boolean isCancelled() {
				return matchListener.isCancelled();
			}
		});
	}

	public void setPlayer(PlayerAndAI whitePlayer, PlayerAndAI blackPlayer) {
		this.game.setPlayer(whitePlayer, blackPlayer);
		this.whitePlayer = whitePlayer.getPlayer();
		this.blackPlayer = blackPlayer.getPlayer();
	}

	public MatchResult play(int n) {
		MatchResult matchResult = new MatchResult(whitePlayer, blackPlayer);
		for (int i = 0; i < n; i++) {

			/* End game immediately if cancelled */
			if (matchListener != null && matchListener.isCancelled())
				break;

			/* Publish game begins */
			if (matchListener != null)
				matchListener.onBeforeGame(i, n, whitePlayer, blackPlayer);

			GameResult gameResult = game.play();
			matchResult.inc(gameResult);

			/* Publish game finished */
			if (matchListener != null) {
				matchListener.onAfterGame(i, n);
				matchListener.onProgress(1.0 * (i + 1) / n);
			}
		}
		return matchResult;
	}
}
