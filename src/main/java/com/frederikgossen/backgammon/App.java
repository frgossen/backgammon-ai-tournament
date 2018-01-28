package com.frederikgossen.backgammon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.frederikgossen.backgammon.gamelogic.PlayerAndAI;
import com.frederikgossen.backgammon.gamelogic.Tournament;
import com.frederikgossen.backgammon.gamelogic.Tournament.TournamentListener;
import com.frederikgossen.backgammon.model.Board;
import com.frederikgossen.backgammon.model.DiceValues;
import com.frederikgossen.backgammon.model.ModelPerspective;
import com.frederikgossen.backgammon.model.Move;
import com.frederikgossen.backgammon.model.Player;
import com.frederikgossen.backgammon.model.TournamentResult;
import com.frederikgossen.backgammon.ui.MenuScreen;
import com.frederikgossen.backgammon.ui.ModelScreen;
import com.frederikgossen.backgammon.ui.exceptions.InvalidInputException;

public class App {

	private MenuScreen menuScreen;
	private ModelScreen modelScreen;
	private Tournament tournament;

	private SynchronizedVariable<Boolean> cancelled;
	private SynchronizedVariable<Boolean> showMoves;
	private SynchronizedVariable<Integer> durationPerMoveInMs;
	private Thread tournamentThread;

	public static void main(String[] args) {
		new App();
	}

	public App() {
		initMenuScreen();
		initTournament();
	}

	private void initMenuScreen() {
		menuScreen = new MenuScreen("Backgammon | Menu");
		menuScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menuScreen.addOnStartListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runTournamentAsync();
			}
		});
		menuScreen.addOnCancelListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelled.set(true);
			}
		});
		menuScreen.setVisible(true);
		modelScreen = new ModelScreen("Backgammon | Game in Progress");
		modelScreen.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {
			}

			public void windowIconified(WindowEvent e) {
			}

			public void windowDeiconified(WindowEvent e) {
			}

			public void windowDeactivated(WindowEvent e) {
			}

			public void windowClosing(WindowEvent e) {
				cancelled.set(true);
			}

			public void windowClosed(WindowEvent e) {
			}

			public void windowActivated(WindowEvent e) {
			}
		});
	}

	private void initTournament() {
		cancelled = new SynchronizedVariable<Boolean>(false);
		showMoves = new SynchronizedVariable<Boolean>(false);
		durationPerMoveInMs = new SynchronizedVariable<Integer>(0);
		long randomSeed = System.currentTimeMillis();
		tournament = new Tournament(randomSeed);
		tournament.setTournamentListener(new TournamentListener() {

			public void onBeforeMatch(int i, int n, Player whitePlayer, Player blackPlayer) {
				modelScreen.setPlayer(whitePlayer, blackPlayer);
			}

			public void onAfterMatch(int i, int n) {
			}

			public void onBeforeGame(int i, int n, Player whitePlayer, Player blackPlayer) {
			}

			public void onAfterGame(int i, int n) {
			}

			public void onProgress(double p) {
				menuScreen.setProgress(p);
			}

			public void onBeforeMove(ModelPerspective persp, Board b, DiceValues diceVs, int ithMoveInTurn,
					PlayerAndAI activePlayer, PlayerAndAI otherPlayer, Move m) {

				if (showMoves.get()) {
					Board boardDeepCopy = new Board(b);
					DiceValues diceValuesDeepCopy = new DiceValues(diceVs);
					Move moveDeepCopy = new Move(m);
					modelScreen.setModelPerspective(persp);
					modelScreen.setBoard(boardDeepCopy);
					modelScreen.setDiceValues(diceValuesDeepCopy);
					modelScreen.clearMove();
					modelScreen.repaint();
					blockMatchExecution(durationPerMoveInMs.get() / 3);
					modelScreen.setMove(moveDeepCopy);
					modelScreen.repaint();
					blockMatchExecution(durationPerMoveInMs.get() / 3);
				}
			}

			public void onAfterMove(ModelPerspective persp, Board b, DiceValues diceVs, int ithMoveInTurn,
					PlayerAndAI activePlayer, PlayerAndAI otherPlayer, Move m) {

				if (showMoves.get()) {
					Board boardDeepCopy = new Board(b);
					DiceValues diceValuesDeepCopy = new DiceValues(diceVs);
					Move moveDeepCopy = new Move(m);
					modelScreen.setModelPerspective(persp);
					modelScreen.setBoard(boardDeepCopy);
					modelScreen.setDiceValues(diceValuesDeepCopy);
					modelScreen.setMove(moveDeepCopy);
					modelScreen.repaint();
					blockMatchExecution(durationPerMoveInMs.get() / 3);
				}
			}

			public boolean isCancelled() {
				return cancelled.get();
			}
		});
	}

	private void runTournamentAsync() {
		tournamentThread = new Thread(new Runnable() {
			public void run() {
				runTournament();
			}
		});
		tournamentThread.start();
	}

	private void runTournament() {
		try {
			/* Collect parameters */
			List<PlayerAndAI> selectedPlayerAndAIs = menuScreen.getSelectedPlayerAndAIs();
			int numberOfGames = menuScreen.getNumberOfGames();
			showMoves.set(menuScreen.isShowMovesSelected());
			durationPerMoveInMs.set(menuScreen.getDurationPerMoveInMs());

			/* Start tournament */
			cancelled.set(false);
			tournament.setPlayer(selectedPlayerAndAIs);
			menuScreen.setInProgress(true);
			modelScreen.setVisible(showMoves.get());
			TournamentResult tournamentResult = tournament.play(numberOfGames);
			menuScreen.setInProgress(false);
			modelScreen.setVisible(false);

			/* Show result */
			JOptionPane.showMessageDialog(menuScreen, tournamentResult, "Tournament Result",
					JOptionPane.INFORMATION_MESSAGE);

		} catch (InvalidInputException e) {
			JOptionPane.showMessageDialog(menuScreen, e.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void blockMatchExecution(int ms) {
		if (!cancelled.get()) {
			try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				/* It's not too important to wait anyways */
			}
		}
	}
}
