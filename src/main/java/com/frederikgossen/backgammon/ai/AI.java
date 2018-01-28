package com.frederikgossen.backgammon.ai;

import com.frederikgossen.backgammon.model.Board;
import com.frederikgossen.backgammon.model.DiceValues;
import com.frederikgossen.backgammon.model.Move;

public interface AI {

	public Move think(Board board, DiceValues diceValues, int ithMoveInTurn);
}
