package de.sengerts.tictactoe.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.sengerts.tictactoe.ui.TicTacToeGame;

/**
 * Class representing a title screen for a tic tac toe game.
 * 
 * @author Tobias Senger
 */
public class TitleScreen implements Screen {

	/**
	 * Instance variable that stores the tic tac toe game this screen
	 * is associated with.
	 */
	private final TicTacToeGame ticTacToeGame;
	/**
	 * Instance variable that stores the sprite batch for this screen.
	 */
	private final SpriteBatch batch;
	/**
	 * Instance variable that stores the stage for this screen.
	 */
	protected final Stage stage;
	/**
	 * Instance variable that stores the viewport for this screen.
	 */
	private final Viewport viewport;
	/**
	 * Instance variable that stores the camera for this screen.
	 */
	private final OrthographicCamera camera;

	/**
	 * Another constructor for class TitleScreen.
	 * 
	 * Creates a new object of type TitleScreen by assigning the tic tac toe game
	 * associated with this screen to the instance variable ticTacToeGame and
	 * initializing the sprite batch, camera, viewport and stage for this screen.
	 * 
	 * @param ticTacToeGame the tic tac toe game this screen is associated with
	 */
	public TitleScreen(final TicTacToeGame ticTacToeGame) {
		this.ticTacToeGame = ticTacToeGame;

		this.batch = new SpriteBatch();
		this.camera = new OrthographicCamera();

		this.viewport = new FitViewport(TicTacToeGame.MENU_WIDTH, TicTacToeGame.MENU_HEIGHT, camera);
		viewport.apply();

		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();

		this.stage = new Stage(viewport, batch);
	}

	/**
	 * Shows this title screen.
	 * 
	 * When this title screen is shown, this screen's stage is set
	 * as the input processor and the main table is initialized and
	 * added with content before adding the table as an actor
	 * to this screen's stage.
	 */
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		// Stage should control input:
		Gdx.input.setInputProcessor(stage);

		// Create Table
		Table mainTable = new Table();
		// Set table to fill stage
		mainTable.setFillParent(true);
		// Set alignment of contents in the table.
		mainTable.top();

		// Create Logo
		Label logoLabel = new Label("[TTT_BLUE]Tic [TTT_GREEN]Tac [TTT_RED]Toe", ticTacToeGame.getSkin());

		// Create buttons
		TextButton playButton = new TextButton("Play", ticTacToeGame.getSkin());
		TextButton optionsButton = new TextButton("Options", ticTacToeGame.getSkin());
		TextButton exitButton = new TextButton("Exit", ticTacToeGame.getSkin());

		// Add listeners to buttons
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ticTacToeGame.setPlayScreen();
			}
		});
		optionsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ticTacToeGame.setOptionsScreen();
			}
		});
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ticTacToeGame.exitGame();
			}
		});

		Label credits = new Label("By Tobias Senger", ticTacToeGame.getSkin(), "dec-font-32", "light");

		// Add actors to table
		mainTable.add(logoLabel).padTop(100f).padBottom(50f);
		mainTable.row();
		mainTable.add(playButton).padBottom(10f);
		mainTable.row();
		mainTable.add(optionsButton).padBottom(10f);
		mainTable.row();
		mainTable.add(exitButton);
		mainTable.row();
		mainTable.add(credits).padTop(230f);

		// Add table to stage
		stage.addActor(mainTable);
	}

	/**
	 * Renders this title screen.
	 * 
	 * Renders this title screen by clearing it with the "dark"
	 * game color and letting this screen's stage act before
	 * drawing the stage.
	 */
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(45f / 255f, 49f / 255f, 66f / 255f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
	}

	/**
	 * Resizes this title screen.
	 * 
	 * Resizes this title screen by calling the screen's game's
	 * adjustViewport method with the new window width and height
	 * and the viewport of this screen as parameters.
	 */
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		ticTacToeGame.adjustViewport(width, height, viewport);
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

	/**
	 * Disposes this title screen.
	 * 
	 * Disposes this title screen by disposing this screen's
	 * sprite batch and stage.
	 */
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
	}

}