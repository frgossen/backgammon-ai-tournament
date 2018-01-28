package com.frederikgossen.backgammon.gamelogic;

import java.util.Random;

import com.frederikgossen.backgammon.model.DiceValues;

public class GameRandomness {

	private Random random;

	public GameRandomness(long seed) {
		random = new Random(seed);
	}

	public <T> T randomChoice(T a, T b) {
		return random.nextBoolean() ? a : b;
	}

	public int roll() {
		return random.nextInt(6) + 1;
	}

	public void nextDiceValues(DiceValues diceValues) {
		diceValues.setValues(roll(), roll());
	}
}
