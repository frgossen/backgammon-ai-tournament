package com.frederikgossen.backgammon.ai;

import java.util.ArrayList;
import java.util.Random;

import com.frederikgossen.backgammon.model.Board;
import com.frederikgossen.backgammon.model.DiceValues;
import com.frederikgossen.backgammon.model.Move;

public abstract class MoveScoringAI implements AI {

	private Random random = new Random();
	private ArrayList<Move> bestMoves = new ArrayList<Move>();
	int bestScore = 0;
	private Move randomBestMove;

	public Move think(Board board, DiceValues diceValues, int ithMoveInTurn) {
		findBestMove(board, diceValues, ithMoveInTurn);
		return randomBestMove;
	}

	private void findBestMove(Board board, DiceValues diceValues, int ithMoveInTurn) {
		clear();
		for (Integer diceValue : diceValues) {
			for (int from = 0; from < Board.N_POINTS; from++) {
				int to = from + diceValue;
				if (board.isMyMoveValid(from, to)) {
					Move move = new Move(from, to);
					considerAsBestMove(board, diceValues, ithMoveInTurn, move);
				}
			}
		}
		selectRandomBestMove();
	}

	private void clear() {
		bestMoves.clear();
		bestScore = 0;
		randomBestMove = null;
	}

	private void considerAsBestMove(Board board, DiceValues diceValues, int ithMoveInTurn, Move move) {
		int score = scoreMove(board, diceValues, ithMoveInTurn, move);
		if (bestMoves.isEmpty() || score >= bestScore) {
			if (score > bestScore)
				bestMoves.clear();
			bestScore = score;
			bestMoves.add(move);
		}
	}

	private void selectRandomBestMove() {
		if (bestMoves.isEmpty())
			throw new IllegalStateException("no possible move");
		int n = bestMoves.size();
		int i = random.nextInt(n);
		randomBestMove = bestMoves.get(i);
	}

	protected abstract int scoreMove(Board board, DiceValues diceValues, int ithMoveInTurn, Move move);
}
