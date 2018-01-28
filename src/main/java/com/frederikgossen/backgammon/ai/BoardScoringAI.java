package com.frederikgossen.backgammon.ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import com.frederikgossen.backgammon.model.Board;
import com.frederikgossen.backgammon.model.DiceValues;
import com.frederikgossen.backgammon.model.Move;

public abstract class BoardScoringAI implements AI {

	private Random random = new Random();
	private ArrayList<LinkedList<Move>> bestDoubleMoves = new ArrayList<LinkedList<Move>>();
	private int bestScore;
	private LinkedList<Move> randomBestDoubleMove;

	public Move think(Board board, DiceValues diceValues, int ithMoveInTurn) {
		boolean firstMove = ithMoveInTurn == 0;
		if (firstMove)
			findBestSequenceOfMoves(board, diceValues);
		return randomBestDoubleMove.get(ithMoveInTurn);
	}

	private void findBestSequenceOfMoves(Board board, DiceValues diceValues) {
		clear();
		LinkedList<Move> doubleMoves = new LinkedList<Move>();
		scoreDoubleMoves(board, diceValues, doubleMoves);
		selectRandomBestDoubleMove();
	}

	/*
	 * Searches through all possible double moves that begin with the given prefix
	 */
	private void scoreDoubleMoves(Board board, DiceValues diceValues, LinkedList<Move> doubleMovePrefix) {

		if (!board.isAnyMovePossibleForMe(diceValues)) {
			/* This is a maximal sequence of moves */
			considerAsBestDoubleMove(doubleMovePrefix, board);
		} else {
			for (int diceValue : diceValues) {
				for (int from = 0; from < Board.N_POINTS; from++) {
					Move move = new Move(from, from + diceValue);
					if (board.isMyMoveValid(move)) {
						doubleMovePrefix.add(move);

						Board boardAfterMove = boardAfterMove(board, move);
						DiceValues diceValuesAfterMove = diceValuesAfterMove(diceValues, move);
						scoreDoubleMoves(boardAfterMove, diceValuesAfterMove, doubleMovePrefix);

						doubleMovePrefix.removeLast();
					}
				}
			}
		}
	}

	private void clear() {
		bestDoubleMoves.clear();
		bestScore = 0;
		randomBestDoubleMove = null;
	}

	private Board boardAfterMove(Board board, Move move) {
		Board boardAfterMove = new Board(board);
		boardAfterMove.applyMyMove(move);
		return boardAfterMove;
	}

	private DiceValues diceValuesAfterMove(DiceValues diceValues, Move move) {
		DiceValues diceValuesAfterMove = new DiceValues(diceValues);
		diceValuesAfterMove.removeValue(move.getDiff());
		return diceValuesAfterMove;
	}

	private void considerAsBestDoubleMove(LinkedList<Move> sequenceOfMoves, Board board) {
		int score = scoreBoard(board);
		if (bestDoubleMoves.isEmpty() || score >= bestScore) {
			if (score > bestScore)
				bestDoubleMoves.clear();
			bestDoubleMoves.add(new LinkedList<Move>(sequenceOfMoves));
		}
	}

	private void selectRandomBestDoubleMove() {
		if (bestDoubleMoves.isEmpty())
			throw new IllegalStateException("no possible move");
		int n = bestDoubleMoves.size();
		int i = random.nextInt(n);
		randomBestDoubleMove = bestDoubleMoves.get(i);
	}

	protected abstract int scoreBoard(Board board);
}
