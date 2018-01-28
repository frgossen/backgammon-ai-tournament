package com.frederikgossen.backgammon.ai;

import com.frederikgossen.backgammon.model.Board;
import com.frederikgossen.backgammon.model.DiceValues;
import com.frederikgossen.backgammon.model.Move;

public class ProtectiveAI extends MoveScoringAI {

	@Override
	protected int scoreMove(Board board, DiceValues diceValues, int ithMoveInTurn, Move move) {
		int to = move.getTo();
		int from = move.getFrom();
		int score = 0;
		boolean canProtectChecker = Board.isAnyQuarter(to) && board.getPoint(to).getMyCheckers() == 1;
		if (canProtectChecker)
			score += 16;
		boolean leaveNoUnprotectedCheckerBehind = !(Board.isAnyQuarter(from)
				&& board.getPoint(from).getMyCheckers() == 2);
		if (leaveNoUnprotectedCheckerBehind)
			score += 8;
		boolean haveCheckerProtected = !Board.isAnyQuarter(to) || board.getPoint(to).getMyCheckers() > 0;
		if (haveCheckerProtected)
			score += 4;
		return score;
	}
}
