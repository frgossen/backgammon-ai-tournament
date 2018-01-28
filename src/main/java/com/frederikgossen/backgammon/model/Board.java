package com.frederikgossen.backgammon.model;

import java.util.Arrays;

public class Board {

	private Point[] points;

	public static final int N_POINTS_PER_QUARTER = 6;
	public static final int MY_START_POINT = 0;
	public static final int MY_FIRST_QUARTER = MY_START_POINT + 1;
	public static final int MY_SECOND_QUARTER = MY_FIRST_QUARTER + N_POINTS_PER_QUARTER;
	public static final int MY_THIRD_QUARTER = MY_SECOND_QUARTER + N_POINTS_PER_QUARTER;
	public static final int MY_FOURTH_QUARTER = MY_THIRD_QUARTER + N_POINTS_PER_QUARTER;
	public static final int MY_END_POINT = MY_FOURTH_QUARTER + N_POINTS_PER_QUARTER;
	public static final int N_POINTS = MY_END_POINT + 1;

	public Board() {
		points = new Point[N_POINTS];
		for (int i = 0; i < points.length; i++)
			points[i] = new Point();
		setup();
	}

	public Board(Board board) {
		points = new Point[N_POINTS];
		for (int i = 0; i < points.length; i++)
			points[i] = new Point(board.points[i]);
	}

	public void setup() {
		for (int i = 0; i < N_POINTS; i++) {
			points[i].clearMyCheckers();
			points[i].clearYourCheckers();
		}
		points[1].setMyCheckers(2);
		points[6].setYourCheckers(5);
		points[8].setYourCheckers(3);
		points[12].setMyCheckers(5);
		points[13].setYourCheckers(5);
		points[17].setMyCheckers(3);
		points[19].setMyCheckers(5);
		points[24].setYourCheckers(2);
	}

	public void togglePerspective() {
		/* Toggle order */
		for (int i = 0; i < N_POINTS / 2; i++) {
			Point tmp = points[i];
			points[i] = points[N_POINTS - 1 - i];
			points[N_POINTS - 1 - i] = tmp;
		}
		/* Toggle points */
		for (Point p : points)
			p.togglePerspective();
	}

	public Point getPoint(int i) {
		return isPoint(i) ? points[i] : null;
	}

	private boolean isPoint(int i) {
		return MY_START_POINT <= i && i <= MY_END_POINT;
	}

	public Point getMyStartPoint() {
		return getPoint(MY_START_POINT);
	}

	public Point getMyEndPoint() {
		return getPoint(MY_END_POINT);
	}

	public Point getYourStartPoint() {
		return getMyEndPoint();
	}

	public Point getYourEndPoint() {
		return getMyStartPoint();
	}

	public boolean theyFinished(boolean includingMe, boolean includingYou) {
		for (int i = 0; i < points.length; i++) {
			if (i != MY_END_POINT) {
				if (includingMe && points[i].isMine()) {
					/*
					 * If I own a point on the board there must be some of my checkers left and I
					 * did not finish yet
					 */
					return false;
				}
				if (includingYou && points[i].isYours()) {
					/*
					 * If you own a point on the board there must be some of your checkers left and
					 * you did not finish yet
					 */
					return false;
				}
			}
		}
		return true;
	}

	public boolean iFinished() {
		boolean includingMe = true;
		boolean includingYou = false;
		return theyFinished(includingMe, includingYou);
	}

	public boolean youFinished() {
		boolean includingMe = false;
		boolean includingYou = true;
		return theyFinished(includingMe, includingYou);
	}

	public boolean someoneFinished() {
		return iFinished() || youFinished();
	}

	public void applyMyMove(Move move) {
		if (isMyMoveValid(move)) {
			int from = move.getFrom();
			int to = move.getTo();
			getPoint(from).decMyCheckers();
			Point pTo = getPoint(to);
			pTo.incMyCheckers();
			if (to != MY_END_POINT) {
				int yourCheckers = pTo.getYourCheckers();
				pTo.clearYourCheckers();
				getYourStartPoint().incYourCheckers(yourCheckers);
			}
		}
	}

	public boolean isMyMoveValid(Move move) {
		return isMyMoveValid(move.getFrom(), move.getTo());
	}

	public boolean isMyMoveValid(int from, int to) {
		/* If I have checkers on start I must move them first */
		if (getMyStartPoint().getMyCheckers() > 0 && from != MY_START_POINT)
			return false;

		/* Validate move's origin */
		boolean fromValid = from == MY_END_POINT ? false : isPoint(from) && getPoint(from).getMyCheckers() > 0;
		if (!fromValid)
			return false;

		/* Validate move's destination */
		boolean toValid = to == MY_END_POINT ? allMyCheckersInFourthQuarterOrBeyond()
				: isPoint(to) && getPoint(to).getYourCheckers() <= 1;
		return toValid;
	}

	public boolean allMyCheckersInFourthQuarterOrBeyond() {
		for (int i = 0; i < points.length; i++) {
			if (MY_START_POINT <= i && i < MY_FOURTH_QUARTER && points[i].isMine()) {
				/*
				 * If I own a point prior to the fourth quarter there must be some checkers left
				 */
				return false;
			}
		}
		return true;
	}

	public boolean isAnyMovePossibleForMe(DiceValues diceValues) {
		for (int diceValue : diceValues) {
			if (isAnyMovePossibleForMe(diceValue))
				return true;
		}
		return false;
	}

	public boolean isAnyMovePossibleForMe(int diceValue) {
		return countPossibleMovesForMe(diceValue, 1) > 0;
	}

	public int countPossibleMovesForMe(int diceValue) {
		return countPossibleMovesForMe(diceValue, -1);
	}

	public int countPossibleMovesForMe(int diceValue, int limit) {
		int count = 0;
		for (int from = 0; from < points.length; from++) {
			int to = from + diceValue;
			if (isMyMoveValid(from, to)) {
				count++;
				if (limit >= 0 && count >= limit)
					return count;
			}
		}
		return count;
	}

	public static boolean isMyFirstQuarter(int i) {
		return MY_FIRST_QUARTER <= i && i < MY_SECOND_QUARTER;
	}

	public static boolean isMySecondQuarter(int i) {
		return MY_SECOND_QUARTER <= i && i < MY_THIRD_QUARTER;
	}

	public static boolean isMyThirdQuarter(int i) {
		return MY_THIRD_QUARTER <= i && i < MY_FOURTH_QUARTER;
	}

	public static boolean isMyFourthQuarter(int i) {
		return MY_FOURTH_QUARTER <= i && i < MY_END_POINT;
	}

	public static boolean isMyStartPoint(int i) {
		return MY_START_POINT == i;
	}

	public static boolean isMyEndPoint(int i) {
		return MY_END_POINT == i;
	}

	public static boolean isYourFirstQuarter(int i) {
		return isMyFourthQuarter(i);
	}

	public static boolean isYourSecondQuarter(int i) {
		return isMyThirdQuarter(i);
	}

	public static boolean isYourThirdQuarter(int i) {
		return isMySecondQuarter(i);
	}

	public static boolean isYourFourthQuarter(int i) {
		return isMyFirstQuarter(i);
	}

	public static boolean isYourStartPoint(int i) {
		return isMyEndPoint(i);
	}

	public static boolean isYourEndPoint(int i) {
		return isMyStartPoint(i);
	}

	public static boolean isFirstQuarter(int i, boolean forMe) {
		return forMe ? isMyFirstQuarter(i) : isYourFirstQuarter(i);
	}

	public static boolean isSecondQuarter(int i, boolean forMe) {
		return forMe ? isMySecondQuarter(i) : isYourSecondQuarter(i);
	}

	public static boolean isThirdQuarter(int i, boolean forMe) {
		return forMe ? isMyThirdQuarter(i) : isYourThirdQuarter(i);
	}

	public static boolean isFourthQuarter(int i, boolean forMe) {
		return forMe ? isMyFourthQuarter(i) : isYourFourthQuarter(i);
	}

	public static boolean isStartPoint(int i, boolean forMe) {
		return forMe ? isMyStartPoint(i) : isYourStartPoint(i);
	}

	public static boolean isEndPoint(int i, boolean forMe) {
		return forMe ? isMyEndPoint(i) : isYourEndPoint(i);
	}

	public static boolean isAnyQuarter(int i) {
		return MY_START_POINT != i && MY_END_POINT != i;
	}

	@Override
	public String toString() {
		return Arrays.toString(points);
	}
}
