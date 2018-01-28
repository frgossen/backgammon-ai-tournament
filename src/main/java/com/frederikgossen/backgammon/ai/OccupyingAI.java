package com.frederikgossen.backgammon.ai;

import com.frederikgossen.backgammon.model.Board;

public class OccupyingAI extends BoardScoringAI {

	@Override
	protected int scoreBoard(Board board) {
		int countSafePoints = 0;
		int countVulnerablePoints = 0;
		for (int i = Board.MY_FIRST_QUARTER; i < Board.MY_END_POINT; i++) {
			int nCheckers = board.getPoint(i).getMyCheckers();
			if (nCheckers == 1)
				countVulnerablePoints++;
			else if (nCheckers >= 2)
				countSafePoints++;
		}
		return countSafePoints - countVulnerablePoints;
	}
}
