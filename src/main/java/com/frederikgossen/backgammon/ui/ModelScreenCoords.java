package com.frederikgossen.backgammon.ui;

import java.awt.FontMetrics;

import com.frederikgossen.backgammon.model.Board;
import com.frederikgossen.backgammon.model.ModelPerspective;

public class ModelScreenCoords {

	private static final int N_COLUMNS = 14;
	private static final int N_ROWS = 2;
	private static final int N_TRIANGLE_VTX = N_COLUMNS * 2 + 1;

	private ModelPerspective modelPerspective;
	private int viewportW;
	private int viewportH;
	private double columnW;
	private double rowH;
	private int[] trianglesBufferXs = new int[N_TRIANGLE_VTX];
	private int[] trianglesBufferYs = new int[N_TRIANGLE_VTX];
	private int[] highlightTriangleBufferXs = new int[3];
	private int[] highlightTriangleBufferYs = new int[3];

	public ModelScreenCoords() {
		modelPerspective = ModelPerspective.WHITE_IS_MY_COLOR;
		viewportW = viewportH = 0;
		columnW = rowH = 0;
	}

	public void setModelPerspective(ModelPerspective modelPerspective) {
		this.modelPerspective = modelPerspective;
	}

	public void setViewport(int viewportW, int viewportH) {
		this.viewportW = viewportW;
		this.viewportH = viewportH;
		columnW = 1.0 * viewportW / N_COLUMNS;
		rowH = 1.0 * viewportH / N_ROWS;
	}

	public int viewportW() {
		return viewportW;
	}

	public int viewportH() {
		return viewportH;
	}

	public int checkerDiameter() {
		return (int) (0.8 * columnW);
	}

	public int barX() {
		return (int) (6 * columnW);
	}

	public int barY() {
		return 0;
	}

	public int barW() {
		return (int) Math.ceil(columnW);
	}

	public int barH() {
		return (int) Math.ceil(2 * rowH);
	}

	public int finishX() {
		return (int) (13 * columnW);
	}

	public int finishY() {
		return 0;
	}

	public int finishW() {
		return (int) Math.ceil(columnW);
	}

	public int finishH() {
		return (int) Math.ceil(2 * rowH);
	}

	public int[] trianglesXs() {
		for (int i = 0; i < trianglesBufferXs.length; i++)
			trianglesBufferXs[i] = (int) (0.5 * i * columnW);
		return trianglesBufferXs;
	}

	public int[] trianglesYs(boolean lowerRow) {
		double triangleH = 0.9 * rowH;
		for (int i = 0; i < trianglesBufferYs.length; i++) {
			double upperRowY = (i % 2) * triangleH;
			trianglesBufferYs[i] = (int) (lowerRow ? 2 * rowH - upperRowY : upperRowY);
		}
		return trianglesBufferYs;
	}

	private boolean isUpperPoint(int iPoint, boolean forMe) {
		int iPointWhite = fromWhitePerspective(iPoint);
		return Board.isMyFirstQuarter(iPointWhite) || Board.isMySecondQuarter(iPointWhite);
	}

	private boolean isLowerPoint(int iPoint, boolean forMe) {
		int iPointWhite = fromWhitePerspective(iPoint);
		return Board.isMyThirdQuarter(iPointWhite) || Board.isMyFourthQuarter(iPointWhite);
	}

	private int fromWhitePerspective(int i) {
		if (modelPerspective == ModelPerspective.WHITE_IS_MY_COLOR)
			return i;
		else
			return Board.N_POINTS - 1 - i;
	}

	private boolean isForWhite(boolean forMe) {
		return forMe == (modelPerspective == ModelPerspective.WHITE_IS_MY_COLOR);
	}

	public int anchor(int x, int y) {
		boolean forMe = true;
		return anchor(x, y, forMe);
	}

	public int anchor(int x, int y, boolean forMe) {
		/* Determine column */
		int col = (int) (x / columnW);
		if (col < 0)
			col = 0;
		else if (col >= N_COLUMNS)
			col = N_COLUMNS - 1;

		if (col == 6)
			return Board.MY_START_POINT;
		else if (col == 13)
			return Board.MY_END_POINT;

		/* Determine row */
		int row = (int) (y / rowH);
		if (row < 0)
			row = 0;
		else if (row >= N_ROWS)
			row = N_ROWS - 1;
		
		if ((row <= 0 && isForWhite(forMe)) || row > 0 && !isForWhite(forMe))
			return (col < 6) ? 12 - col : 12 - (col - 1);
		else
			return (col < 6) ? 13 + col : 13 + (col - 1);
	}

	public int anchorX(int iPoint) {
		boolean forMe = true;
		return anchorX(iPoint, forMe);
	}

	public int anchorX(int iPoint, boolean forMe) {
		/* Color independent cases */
		if ((forMe && Board.MY_START_POINT == iPoint) || (!forMe && Board.MY_END_POINT == iPoint))
			return (int) (6.5 * columnW);
		else if ((forMe && Board.MY_END_POINT == iPoint) || (!forMe && Board.MY_START_POINT == iPoint))
			return (int) (13.5 * columnW);

		/* See color dependent cases from a white perspective */
		int iPointWhite = fromWhitePerspective(iPoint);
		if (Board.isMyFirstQuarter(iPointWhite))
			return (int) ((13.5 - iPointWhite) * columnW);
		else if (Board.isMySecondQuarter(iPointWhite))
			return (int) ((12.5 - iPointWhite) * columnW);
		else if (Board.isMyThirdQuarter(iPointWhite))
			return (int) ((iPointWhite - 12.5) * columnW);
		else if (Board.isMyFourthQuarter(iPointWhite))
			return (int) ((iPointWhite - 11.5) * columnW);
		return 0;
	}

	public int anchorY(int iPoint) {
		boolean forMe = true;
		return anchorY(iPoint, forMe);
	}

	public int anchorY(int iPoint, boolean forMe) {
		if (isUpperPoint(iPoint, forMe))
			return (int) (columnW / 2);
		else if (isLowerPoint(iPoint, forMe))
			return (int) (2 * rowH - columnW / 2);
		else if (isForWhite(forMe))
			return (int) (rowH - columnW / 2);
		else
			return (int) (rowH + columnW / 2);
	}

	public int checkerX(int iPoint, boolean forMe) {
		int anchorX = anchorX(iPoint, forMe);
		anchorX -= checkerDiameter() / 2;
		return anchorX;
	}

	public int checkerY(int iPoint, boolean forMe, int iChecker, int nChecker) {
		/* See color dependent cases from a white perspective */
		int iPointWhite = fromWhitePerspective(iPoint);

		/* Determine orientation */
		boolean upwards;
		if (Board.isMyFirstQuarter(iPointWhite) || Board.isMySecondQuarter(iPointWhite))
			upwards = false;
		else if (Board.isMyThirdQuarter(iPointWhite) || Board.isMyFourthQuarter(iPointWhite))
			upwards = true;
		else
			upwards = isForWhite(forMe);

		/* Determine coordinate */
		int anchorY = anchorY(iPoint, forMe);
		double spaceLeft = rowH - checkerDiameter();
		double delta = Math.min(checkerDiameter(), spaceLeft / nChecker) * iChecker;
		anchorY += (int) (upwards ? -delta : delta);
		anchorY -= checkerDiameter() / 2;
		return anchorY;
	}

	public int moveStrokeD() {
		return (int) (0.1 * columnW);
	}

	public boolean isMoveArcApplicable(int iFrom, int iTo) {
		boolean forMe = true;
		boolean isUpperMove = isUpperPoint(iFrom, forMe) && isUpperPoint(iTo, forMe);
		if (isUpperMove)
			return true;
		boolean isLowerMove = isLowerPoint(iFrom, forMe) && isLowerPoint(iTo, forMe);
		return isLowerMove;
	}

	public int moveArcX(int iFrom, int iTo) {
		return Math.min(anchorX(iFrom), anchorX(iTo));
	}

	public int moveArcY(int iFrom, int iTo) {
		boolean forMe = true;
		int anchorY = anchorY(iFrom, forMe);
		return anchorY - moveArcH(iFrom, iTo) / 2;
	}

	public int moveArcW(int iFrom, int iTo) {
		return Math.abs(anchorX(iTo) - anchorX(iFrom));
	}

	public int moveArcH(int iFrom, int iTo) {
		double maxArcH = 1.5 * rowH;
		double preferredArcH = 0.9 * moveArcW(iFrom, iTo);
		return (int) Math.min(maxArcH, preferredArcH);
	}

	public int moveArcAngle0(int iFrom, int iTo) {
		boolean forMe = true;
		boolean isUpperMove = isUpperPoint(iFrom, forMe) && isUpperPoint(iTo, forMe);
		return isUpperMove ? 180 : 0;
	}

	public int moveArcAngle1() {
		return 180;
	}

	public int fontH() {
		return playerH() * 2 / 3;
	}

	public int playerX(int playerW) {
		return (int) (10 * columnW) - playerW / 2;
	}

	public int playerY(int i) {
		return (int) (rowH) + (i - 1) * playerH();
	}

	public int playerW(FontMetrics fm, String name0, String name1) {
		int padding = playerH() / 2;
		int maxNameW = Math.max(playerNameW(fm, name0), playerNameW(fm, name1));
		return maxNameW + 2 * padding;
	}

	public int playerH() {
		return checkerDiameter() / 2;
	}

	public int playerNameX(FontMetrics fm, String name) {
		return (int) (10 * columnW) - playerNameW(fm, name) / 2;
	}

	public int playerNameY(FontMetrics fm, int i) {
		return playerY(i) + playerH() / 2 - fm.getHeight() / 2 + fm.getAscent();
	}

	public int playerNameW(FontMetrics fm, String name) {
		return fm.stringWidth(name);
	}

	public int playerNameH() {
		return playerH() / 2;
	}

	public int[] highlightTriangleXs(int playerW) {
		int dX = playerH() / 2;
		int centreX = playerX(playerW);
		highlightTriangleBufferXs[0] = centreX - dX;
		highlightTriangleBufferXs[1] = centreX + dX;
		highlightTriangleBufferXs[2] = centreX - dX;
		return highlightTriangleBufferXs;
	}

	public int[] highlightTriangleYs() {
		int dY = playerH() / 2;
		int iActive = modelPerspective == ModelPerspective.WHITE_IS_MY_COLOR ? 0 : 1;
		int centreY = playerY(iActive) + dY;
		highlightTriangleBufferYs[0] = centreY - dY;
		highlightTriangleBufferYs[1] = centreY;
		highlightTriangleBufferYs[2] = centreY + dY;
		return highlightTriangleBufferYs;
	}

	public int dieY() {
		return (int) (rowH) - dieDiameter() / 2;
	}

	public int dieX(int i, int n) {
		int margin = dieDiameter() / 2;
		int allDiceW = n * dieDiameter() + (n - 1) * margin;
		int dX = dieDiameter() + margin;
		return (int) (3 * columnW) + i * dX - allDiceW / 2;
	}

	public int dieDiameter() {
		return checkerDiameter();
	}

	public int dieDotX(int i, int n, int innerX) {
		return (int) (dieX(i, n) + +0.25 * dieDiameter() * innerX - 0.5 * dieDotDiameter());
	}

	public int dieDotY(int innerY) {
		return (int) (dieY() + 0.25 * dieDiameter() * innerY - 0.5 * dieDotDiameter());
	}

	public int dieDotDiameter() {
		return Math.max(dieDiameter() / 5, 4);
	}

	public float pointSelectionStrokeD() {
		return moveStrokeD();
	}

	public int pointSelectionX(int pointSelection) {
		boolean forMe = true;
		return checkerX(pointSelection, forMe);
	}

	public int pointSelectionY(int pointSelection) {
		boolean forMe = true;
		return checkerY(pointSelection, forMe, 0, 1);
	}

	public int pointSelectionD() {
		return checkerDiameter();
	}
}
