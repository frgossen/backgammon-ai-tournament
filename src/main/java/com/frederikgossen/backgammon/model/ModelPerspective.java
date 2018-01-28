package com.frederikgossen.backgammon.model;

public enum ModelPerspective {

	WHITE_IS_MY_COLOR, BLACK_IS_MY_COLOR;

	public ModelPerspective other() {
		return this == WHITE_IS_MY_COLOR ? BLACK_IS_MY_COLOR : WHITE_IS_MY_COLOR;
	}
}
