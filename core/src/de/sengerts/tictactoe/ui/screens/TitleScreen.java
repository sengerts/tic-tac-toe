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

public class TitleScreen implements Screen {

	private final TicTacToeGame ticTacToeGame;
	private final SpriteBatch batch;
	protected final Stage stage;
	private final Viewport viewport;
	private final OrthographicCamera camera;

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

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(45f / 255f, 49f / 255f, 66f / 255f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		ticTacToeGame.adjustViewport(width, height, viewport);
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
		batch.dispose();
		stage.dispose();
	}

}