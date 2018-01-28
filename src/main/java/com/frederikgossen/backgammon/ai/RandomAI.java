package com.frederikgossen.backgammon.ai;

import com.frederikgossen.backgammon.model.Board;
import com.frederikgossen.backgammon.model.DiceValues;
import com.frederikgossen.backgammon.model.Move;

public class RandomAI extends MoveScoringAI {

	@Override
	protected int scoreMove(Board board, DiceValues diceValues, int ithMoveInTurn, Move move) {
		/* All moves are assigned the same score */
		return 0;
	}
}
