package com.frederikgossen.backgammon.ui;

import java.awt.Color;

import com.frederikgossen.backgammon.model.ModelPerspective;

public class ModelScreenColors {

	private ModelPerspective modelPerspective;

	public static final Color WHITE_PLAYER_COLOR = Color.WHITE;
	public static final Color BLACK_PLAYER_COLOR = Color.BLACK;
	public static final Color WHITE_PLAYER_INVERTED_COLOR = Color.BLACK;
	public static final Color BLACK_PLAYER_INVERTED_COLOR = Color.WHITE;
	public static final Color WHITE_PLAYER_HIGHLIGHT_COLOR = Color.BLUE;
	public static final Color BLACK_PLAYER_HIGHLIGHT_COLOR = Color.RED;
	public static final Color BACKGROUND_COLOR = Color.WHITE;
	public static final Color BAR_COLOR = Color.GRAY;
	public static final Color FINISH_COLOR = Color.GRAY;
	public static final Color TRIANGLE_COLOR = Color.LIGHT_GRAY;
	public static final Color DICE_COLOR = Color.WHITE;
	public static final Color DICE_DOT_COLOR = Color.BLACK;
	public static final Color BORDER_COLOR = Color.BLACK;

	public ModelScreenColors() {
		modelPerspective = ModelPerspective.WHITE_IS_MY_COLOR;
	}

	public void setModelPerspective(ModelPerspective modelPerspective) {
		this.modelPerspective = modelPerspective;
	}

	public Color myColor() {
		return modelPerspective == ModelPerspective.WHITE_IS_MY_COLOR ? WHITE_PLAYER_COLOR : BLACK_PLAYER_COLOR;
	}

	public Color yourColor() {
		return modelPerspective == ModelPerspective.WHITE_IS_MY_COLOR ? BLACK_PLAYER_COLOR : WHITE_PLAYER_COLOR;
	}

	public Color myInvertedColor() {
		return modelPerspective == ModelPerspective.WHITE_IS_MY_COLOR ? WHITE_PLAYER_INVERTED_COLOR
				: BLACK_PLAYER_INVERTED_COLOR;
	}

	public Color yourInvertedColor() {
		return modelPerspective == ModelPerspective.WHITE_IS_MY_COLOR ? BLACK_PLAYER_INVERTED_COLOR
				: WHITE_PLAYER_INVERTED_COLOR;
	}

	public Color myHighlightColor() {
		return modelPerspective == ModelPerspective.WHITE_IS_MY_COLOR ? WHITE_PLAYER_HIGHLIGHT_COLOR
				: BLACK_PLAYER_HIGHLIGHT_COLOR;
	}

	public Color yourHighlightColor() {
		return modelPerspective == ModelPerspective.WHITE_IS_MY_COLOR ? BLACK_PLAYER_HIGHLIGHT_COLOR
				: WHITE_PLAYER_HIGHLIGHT_COLOR;
	}
}
