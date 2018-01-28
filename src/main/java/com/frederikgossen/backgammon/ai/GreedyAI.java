package com.frederikgossen.backgammon.ai;

import com.frederikgossen.backgammon.model.Board;
import com.frederikgossen.backgammon.model.DiceValues;
import com.frederikgossen.backgammon.model.Move;

public class GreedyAI extends MoveScoringAI {

	@Override
	protected int scoreMove(Board board, DiceValues diceValues, int ithMoveInTurn, Move move) {
		int to = move.getTo();
		int score = 0;
		boolean canHitYou = Board.isAnyQuarter(to) && board.getPoint(to).getYourCheckers() == 1;
		if (canHitYou)
			score += 16;
		boolean canFinish = to == Board.MY_END_POINT;
		if (canFinish)
			score += 8;
		return score;
	}
}
