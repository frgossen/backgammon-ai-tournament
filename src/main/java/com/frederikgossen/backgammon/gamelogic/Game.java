package com.frederikgossen.backgammon.gamelogic;

import com.frederikgossen.backgammon.model.Board;
import com.frederikgossen.backgammon.model.DiceValues;
import com.frederikgossen.backgammon.model.GameResult;
import com.frederikgossen.backgammon.model.ModelPerspective;
import com.frederikgossen.backgammon.model.Move;
import com.frederikgossen.backgammon.model.Player;

public class Game {

	public interface GameListener {

		public void onBeforeMove(ModelPerspective persp, Board b, DiceValues diceVs, int ithMoveInTurn,
				PlayerAndAI activePlayer, PlayerAndAI otherPlayer, Move m);

		public void onAfterMove(ModelPerspective persp, Board b, DiceValues diceVs, int ithMoveInTurn,
				PlayerAndAI activePlayer, PlayerAndAI otherPlayer, Move m);

		public boolean isCancelled();
	}

	private ModelPerspective modelPerspective;
	private Board board;
	private DiceValues diceValues;

	private PlayerAndAI activePlayer;
	private PlayerAndAI otherPlayer;
	private int ithMoveInTurn;

	private GameRandomness random;
	private GameListener gameListener;

	public Game(long randomSeed) {
		this.modelPerspective = ModelPerspective.WHITE_IS_MY_COLOR;
		this.board = new Board();
		this.diceValues = new DiceValues();
		this.activePlayer = null;
		this.otherPlayer = null;
		this.ithMoveInTurn = 0;
		this.random = new GameRandomness(randomSeed);
	}

	public void setGameListener(GameListener gameListener) {
		this.gameListener = gameListener;
	}

	public void setPlayer(PlayerAndAI whitePlayer, PlayerAndAI blackPlayer) {
		/*
		 * It dies not matter yet which one is active as that will be randomly decided
		 * once the match starts
		 */
		this.activePlayer = whitePlayer;
		this.otherPlayer = blackPlayer;
	}

	public GameResult play() {
		setup();
		while (!board.someoneFinished()) {

			/* End game immediately if cancelled */
			if (gameListener != null && gameListener.isCancelled())
				return new GameResult(null, GameResult.Reason.CANCELLED);

			/* Throw dice and find playable situation */
			while (diceValues.isEmpty() || !board.isAnyMovePossibleForMe(diceValues)) {
				random.nextDiceValues(diceValues);
				toggleModelPerspective();
				ithMoveInTurn = 0;
			}

			/* Determine next move */
			Board boardDeepCopy = new Board(board);
			DiceValues diceValuesDeepCopy = new DiceValues(diceValues);
			Move move;
			try {
				move = activePlayer.think(boardDeepCopy, diceValuesDeepCopy, ithMoveInTurn);
			} catch (Exception e) {
				e.printStackTrace(System.out);
				Player p = activePlayer.getPlayer();
				return new GameResult(p, GameResult.Reason.CRASHED);
			}
			if (move == null || !board.isMyMoveValid(move) || !diceValues.isMoveValid(move)) {
				Player cheater = activePlayer.getPlayer();
				return new GameResult(cheater, GameResult.Reason.CHEATED);
			}

			/* Publish state before move was applied */
			if (gameListener != null) {
				gameListener.onBeforeMove(modelPerspective, board, diceValues, ithMoveInTurn, activePlayer, otherPlayer,
						move);
			}

			/* Apply move */
			board.applyMyMove(move);
			diceValues.removeValue(move.getDiff());
			ithMoveInTurn++;

			/* Publish state after move was applied */
			if (gameListener != null) {
				gameListener.onAfterMove(modelPerspective, board, diceValues, ithMoveInTurn, activePlayer, otherPlayer,
						move);
			}
		}

		/* Game finished regularly */
		Player winner = (board.iFinished() ? activePlayer : otherPlayer).getPlayer();
		return new GameResult(winner, GameResult.Reason.FINISHED);
	}

	private void setup() {
		board.setup();
		diceValues.clearValues();
		PlayerAndAI firstActivePlayer = random.randomChoice(activePlayer, otherPlayer);
		if (firstActivePlayer != activePlayer)
			toggleModelPerspective();
	}

	private void toggleModelPerspective() {
		PlayerAndAI tmp = activePlayer;
		activePlayer = otherPlayer;
		otherPlayer = tmp;
		board.togglePerspective();
		modelPerspective = modelPerspective.other();
	}
}
