package de.sengerts.tictactoe.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.sengerts.tictactoe.ui.TicTacToeGame;

/**
 * Class representing a transition screen that fades out a current
 * screen and fades in a new screen.
 * 
 * Not used at the moment.
 * 
 * @author Leowbattle - initial work
 * @author Tobias Senger - some changes and adjustments
 **/
public class TransitionScreen implements Screen {

	/**
	 * Instance variable that stores the tic tac toe game this screen
	 * is associated with.
	 */
	private final TicTacToeGame tictacToeGame;
	/**
	 * Instance variable that stores the current screen before transition.
	 */
	private final Screen currentScreen;
	/**
	 * Instance variable that stores the next screen after transition.
	 */
	private final Screen nextScreen;

	/**
	 * Instance variable that stores the current alpha value.
	 * Once this reaches 1.0f, the next scene is shown.
	 */
	private float alpha = 0;
	/**
	 * Instance variable that stores the fade direction for
	 * the transition. This value is true if
	 * fading in, false when fading out.
	 */
	private boolean fadeDirection = true;

	/**
	 * Another constructor for class TransitionScreen.
	 * 
	 * Creates a new object of type TransitionScreen by assigning the tic tac toe game
	 * associated with this screen to the instance variable ticTacToeGame as well as assigning
	 * the given current and next screen to its corresponding instance variables currentScreen
	 * and nextScreen and setting this screen's tic tac toe game once to the next screen before
	 * setting it back to the old screen so that the create method is once called for the next
	 * screen and no errors occur when transitioning the two screens.
	 * 
	 * @param ticTacToeGame the tic tac toe game this screen is associated with
	 */
	public TransitionScreen(final TicTacToeGame tictacToeGame, final Screen currentScreen, final Screen nextScreen) {
		this.tictacToeGame = tictacToeGame;
		this.currentScreen = currentScreen;
		this.nextScreen = nextScreen;
		
		tictacToeGame.setScreen(nextScreen);
		tictacToeGame.setScreen(currentScreen);
	}

	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {

	}

	/**
	 * Renders this transition screen.
	 * 
	 * Renders this transition screen by fading out the current screen
	 * and fading in the next screen over time.
	 */
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
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

	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {

	}

	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {

	}

	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {

	}

	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {

	}

	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {

	}
}