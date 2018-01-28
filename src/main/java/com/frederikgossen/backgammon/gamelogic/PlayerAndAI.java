package com.frederikgossen.backgammon.gamelogic;

import com.frederikgossen.backgammon.ai.AI;
import com.frederikgossen.backgammon.model.Board;
import com.frederikgossen.backgammon.model.DiceValues;
import com.frederikgossen.backgammon.model.Move;
import com.frederikgossen.backgammon.model.Player;

public class PlayerAndAI {

	private Player player;
	private AI ai;

	public PlayerAndAI(Player player, AI ai) {
		this.player = player;
		this.ai = ai;
	}

	public Player getPlayer() {
		return player;
	}

	public AI getAI() {
		return ai;
	}

	public Move think(Board board, DiceValues diceValues, int ithMoveInTurn) {
		return ai.think(board, diceValues, ithMoveInTurn);
	}
}
