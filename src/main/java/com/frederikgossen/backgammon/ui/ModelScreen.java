package com.frederikgossen.backgammon.ui;

import static com.frederikgossen.backgammon.ui.ModelScreenColors.BAR_COLOR;
import static com.frederikgossen.backgammon.ui.ModelScreenColors.BLACK_PLAYER_COLOR;
import static com.frederikgossen.backgammon.ui.ModelScreenColors.BLACK_PLAYER_INVERTED_COLOR;
import static com.frederikgossen.backgammon.ui.ModelScreenColors.BORDER_COLOR;
import static com.frederikgossen.backgammon.ui.ModelScreenColors.DICE_COLOR;
import static com.frederikgossen.backgammon.ui.ModelScreenColors.DICE_DOT_COLOR;
import static com.frederikgossen.backgammon.ui.ModelScreenColors.FINISH_COLOR;
import static com.frederikgossen.backgammon.ui.ModelScreenColors.TRIANGLE_COLOR;
import static com.frederikgossen.backgammon.ui.ModelScreenColors.WHITE_PLAYER_COLOR;
import static com.frederikgossen.backgammon.ui.ModelScreenColors.WHITE_PLAYER_INVERTED_COLOR;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.frederikgossen.backgammon.model.Board;
import com.frederikgossen.backgammon.model.DiceValues;
import com.frederikgossen.backgammon.model.ModelPerspective;
import com.frederikgossen.backgammon.model.Move;
import com.frederikgossen.backgammon.model.Player;
import com.frederikgossen.backgammon.model.Point;

public class ModelScreen extends JFrame {

	private static final long serialVersionUID = -3998049035781480477L;

	private JPanel jPanel;
	private ModelScreenCoords coords;
	private ModelScreenColors colors;

	private Board board = null;
	private Player whitePlayer = null;
	private Player blackPlayer = null;
	private DiceValues diceValues = null;
	private Move move = null;
	private int pressedPoint = -1;

	private Move promptMoveResult = null;
	private final Lock promptMoveLock = new ReentrantLock();
	private final Condition promptMoveAvailable = promptMoveLock.newCondition();

	public ModelScreen(String title) {
		super(title);
		setSize(800, 600);
		jPanel = new JPanel() {
			private static final long serialVersionUID = -2695414010816472109L;

			@Override
			public void paintComponent(Graphics g0) {
				synchronized (ModelScreen.this) {
					Graphics2D g = (Graphics2D) g0;
					coords.setViewport(getWidth(), getHeight());
					clear(g);
					drawBoard(g);
					drawMove(g);
					drawPointSelection(g);
					drawBothPlayer(g);
					drawBothDiceValues(g);
				}
			}
		};
		jPanel.addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent e) {
				pressedPoint = coords.anchor(e.getX(), e.getY());
				jPanel.repaint();
			}

			public void mouseReleased(MouseEvent e) {
				promptMoveLock.lock();
				int from = pressedPoint;
				int to = coords.anchor(e.getX(), e.getY());
				promptMoveResult = new Move(from, to);
				promptMoveAvailable.signalAll();
				pressedPoint = -1;
				jPanel.repaint();
				promptMoveLock.unlock();
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
			}
		});
		setContentPane(jPanel);
		coords = new ModelScreenCoords();
		colors = new ModelScreenColors();
	}

	public synchronized void setBoard(Board board) {
		this.board = board;
	}

	public synchronized void setPlayer(Player whitePlayer, Player blackPlayer) {
		this.whitePlayer = whitePlayer;
		this.blackPlayer = blackPlayer;
	}

	public synchronized void setDiceValues(DiceValues diceValues) {
		this.diceValues = diceValues;
	}

	public synchronized void clearDiceValues() {
		this.diceValues = null;
	}

	public synchronized void setMove(Move move) {
		this.move = move;
	}

	public synchronized void clearMove() {
		this.move = null;
	}

	public synchronized void setPointSelection(int pointSelection) {
		this.pressedPoint = pointSelection;
	}

	public synchronized void clearPointSelection() {
		this.pressedPoint = -1;
	}

	public synchronized void setModelPerspective(ModelPerspective modelPerspective) {
		this.colors.setModelPerspective(modelPerspective);
		this.coords.setModelPerspective(modelPerspective);
	}

	public synchronized Move promptMove() {
		promptMoveLock.lock();
		try {
			while (promptMoveResult == null)
				promptMoveAvailable.await();
			Move result = promptMoveResult;
			promptMoveResult = null;
			return result;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			promptMoveLock.unlock();
		}
		return null;
	}

	private void clear(Graphics2D g) {
		g.setColor(ModelScreenColors.BACKGROUND_COLOR);
		g.fillRect(0, 0, coords.viewportW(), coords.viewportH());
	}

	private void drawBoard(Graphics2D g) {
		/* Draw triangles */
		g.setColor(TRIANGLE_COLOR);
		int[] xs = coords.trianglesXs();
		int[] ys = coords.trianglesYs(false);
		g.fillPolygon(xs, ys, xs.length);
		ys = coords.trianglesYs(true);
		g.fillPolygon(xs, ys, xs.length);

		/* Draw bar */
		g.setColor(BAR_COLOR);
		g.fillRect(coords.barX(), coords.barY(), coords.barW(), coords.barH());

		/* Draw finish */
		g.setColor(FINISH_COLOR);
		g.fillRect(coords.finishX(), coords.finishY(), coords.finishW(), coords.finishH());

		/* Draw checkers */
		if (board != null) {
			for (int i = 0; i < Board.N_POINTS; i++) {
				Point p = board.getPoint(i);
				drawCheckers(g, true, i, p.getMyCheckers());
				drawCheckers(g, false, i, p.getYourCheckers());
			}
		}
	}

	private void drawCheckers(Graphics2D g, boolean myCheckers, int iPoint, int nChecker) {
		int checkerD = coords.checkerDiameter();
		Color color = myCheckers ? colors.myColor() : colors.yourColor();
		Color invertedColor = myCheckers ? colors.myInvertedColor() : colors.yourInvertedColor();
		for (int iChecker = 0; iChecker < nChecker; iChecker++) {
			int checkerX = coords.checkerX(iPoint, myCheckers);
			int checkerY = coords.checkerY(iPoint, myCheckers, iChecker, nChecker);
			g.setColor(color);
			g.fillOval(checkerX, checkerY, checkerD, checkerD);
			g.setColor(invertedColor);
			g.drawOval(checkerX, checkerY, checkerD, checkerD);
		}
	}

	private void drawMove(Graphics2D g) {
		if (move != null) {
			g.setColor(colors.myHighlightColor());
			Stroke originalStroke = g.getStroke();
			g.setStroke(new BasicStroke(coords.moveStrokeD(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			int iFrom = move.getFrom();
			int iTo = move.getTo();
			if (coords.isMoveArcApplicable(iFrom, iTo)) {
				int arcX = coords.moveArcX(iFrom, iTo);
				int arcY = coords.moveArcY(iFrom, iTo);
				int arcW = coords.moveArcW(iFrom, iTo);
				int arcH = coords.moveArcH(iFrom, iTo);
				int arcAngle0 = coords.moveArcAngle0(iFrom, iTo);
				int arcAngle1 = coords.moveArcAngle1();
				g.drawArc(arcX, arcY, arcW, arcH, arcAngle0, arcAngle1);
			} else {
				int fromX = coords.anchorX(iFrom);
				int fromY = coords.anchorY(iFrom);
				int toX = coords.anchorX(iTo);
				int toY = coords.anchorY(iTo);
				g.drawLine(fromX, fromY, toX, toY);
			}
			g.setStroke(originalStroke);
		}
	}

	private void drawPointSelection(Graphics2D g) {
		if (pressedPoint >= 0) {
			g.setColor(colors.myHighlightColor());
			Stroke originalStroke = g.getStroke();
			g.setStroke(new BasicStroke(coords.pointSelectionStrokeD(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.drawOval(coords.pointSelectionX(pressedPoint), coords.pointSelectionY(pressedPoint),
					coords.pointSelectionD(), coords.pointSelectionD());
			g.setStroke(originalStroke);
		}
	}

	private void drawBothPlayer(Graphics2D g) {
		if (whitePlayer != null && blackPlayer != null) {
			Font font = new Font("Verdana", Font.BOLD, coords.fontH());
			g.setFont(font);
			FontMetrics fm = g.getFontMetrics();
			int playerW = coords.playerW(fm, whitePlayer.getName(), blackPlayer.getName());

			/* Draw names */
			drawPlayer(g, 0, playerW, whitePlayer.getName(), WHITE_PLAYER_COLOR, WHITE_PLAYER_INVERTED_COLOR);
			drawPlayer(g, 1, playerW, blackPlayer.getName(), BLACK_PLAYER_COLOR, BLACK_PLAYER_INVERTED_COLOR);

			/* Draw highlighting if active */
			g.setColor(colors.myHighlightColor());
			int[] xs = coords.highlightTriangleXs(playerW);
			int[] ys = coords.highlightTriangleYs();
			g.fillPolygon(xs, ys, xs.length);
		}
	}

	private void drawPlayer(Graphics2D g, int i, int playerW, String name, Color color, Color invertedColor) {
		/* Draw background */
		g.setColor(color);
		g.fillRect(coords.playerX(playerW), coords.playerY(i), playerW, coords.playerH());

		/* Draw name */
		FontMetrics fm = g.getFontMetrics();
		g.setColor(invertedColor);
		g.drawString(name, coords.playerNameX(fm, name), coords.playerNameY(fm, i));

		/* Draw border */
		g.setColor(BORDER_COLOR);
		g.drawRect(coords.playerX(playerW), coords.playerY(i), playerW, coords.playerH());
	}

	private void drawBothDiceValues(Graphics2D g) {
		if (diceValues != null) {
			int n = diceValues.countValues();
			int i = 0;
			Iterator<Integer> it = diceValues.iterator();
			while (it.hasNext())
				drawDieValue(g, i++, n, it.next());
		}
	}

	private void drawDieValue(Graphics2D g, int i, int n, int value) {
		/* Draw box */
		g.setColor(DICE_COLOR);
		g.fillRect(coords.dieX(i, n), coords.dieY(), coords.dieDiameter(), coords.dieDiameter());
		g.setColor(BORDER_COLOR);
		g.drawRect(coords.dieX(i, n), coords.dieY(), coords.dieDiameter(), coords.dieDiameter());

		/* Draw value */
		if (value == 1 || value == 3 || value == 5) {
			drawDieDot(g, i, n, 2, 2);
		}
		if (value == 2 || value == 3 || value == 4 || value == 5 || value == 6) {
			drawDieDot(g, i, n, 1, 3);
			drawDieDot(g, i, n, 3, 1);
		}
		if (value == 4 || value == 5 || value == 6) {
			drawDieDot(g, i, n, 1, 1);
			drawDieDot(g, i, n, 3, 3);
		}
		if (value == 6) {
			drawDieDot(g, i, n, 1, 2);
			drawDieDot(g, i, n, 3, 2);
		}
	}

	private void drawDieDot(Graphics2D g, int i, int n, int innerX, int innerY) {
		g.setColor(DICE_DOT_COLOR);
		g.fillOval(coords.dieDotX(i, n, innerX), coords.dieDotY(innerY), coords.dieDotDiameter(),
				coords.dieDotDiameter());
	}
}
