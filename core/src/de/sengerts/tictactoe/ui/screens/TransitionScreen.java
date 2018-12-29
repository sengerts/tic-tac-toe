package de.sengerts.tictactoe.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.sengerts.tictactoe.ui.TicTacToeGame;

public class TransitionScreen implements Screen {

	private final TicTacToeGame tictacToeGame;
	private final Screen currentScreen;
	private final Screen nextScreen;

	// Once this reaches 1.0f, the next scene is shown
	private float alpha = 0;
	// true if fade in, false if fade out
	private boolean fadeDirection = true;

	public TransitionScreen(final TicTacToeGame tictacToeGame, final Screen currentScreen, final Screen nextScreen) {
		this.tictacToeGame = tictacToeGame;
		this.currentScreen = currentScreen;
		this.nextScreen = nextScreen;

		// Set screens temporarily so that the create() method is called
		// and no null pointer exceptions occur
		tictacToeGame.setScreen(nextScreen);
		tictacToeGame.setScreen(currentScreen);
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (fadeDirection) {
			currentScreen.render(Gdx.graphics.getDeltaTime());
		} else {
			nextScreen.render(Gdx.graphics.getDeltaTime());
		}

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		Color darkColor = Colors.get("TTT_DARK");
		tictacToeGame.getShapeRenderer().setColor(darkColor.r, darkColor.g, darkColor.b, alpha);
		
		tictacToeGame.getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
		tictacToeGame.getShapeRenderer().rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		tictacToeGame.getShapeRenderer().end();
		
		Gdx.gl.glDisable(GL20.GL_BLEND);

		if (alpha >= 1) {
			fadeDirection = false;
		} else if (alpha <= 0 && fadeDirection == false) {
			tictacToeGame.setScreen(nextScreen);
		}
		alpha += fadeDirection == true ? 0.04 : -0.04;
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}