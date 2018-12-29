package de.sengerts.tictactoe.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.sengerts.tictactoe.model.Player;
import de.sengerts.tictactoe.model.PlayerSign;
import de.sengerts.tictactoe.ui.TicTacToeGame;

public class EndScreen implements Screen {

	private final TicTacToeGame ticTacToeGame;
	private final Stage stage;
	private final Viewport viewport;
	private final OrthographicCamera camera;
	private final Table mainTable;

	public EndScreen(final TicTacToeGame ticTacToeGame) {
		this.ticTacToeGame = ticTacToeGame;

		this.mainTable = new Table();
		this.camera = new OrthographicCamera();

		this.viewport = new FitViewport(TicTacToeGame.MENU_WIDTH, TicTacToeGame.MENU_HEIGHT, camera);
		viewport.apply();

		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();

		this.stage = new Stage(viewport, new SpriteBatch());
	}

	@Override
	public void show() {
		// Let stage control input
		Gdx.input.setInputProcessor(stage);

		addEndMap();
		addMainTable();
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
		stage.dispose();
	}
	
	private void addEndMap() {
		Texture endMapTexture = new Texture(Gdx.files.local("endMaps/lastEndMap.png"));
		Image endMapImage = new Image(endMapTexture);
		/*mainTable.add(endMapImage).pad(30f);
		mainTable.row();*/
		
		float width = TicTacToeGame.MENU_WIDTH * 0.4f;
		float x = (TicTacToeGame.MENU_WIDTH - width) / 2f;
		float y = 90f;
		
		endMapImage.setSize(width, width);
		endMapImage.setPosition(x, y);
		
		stage.addActor(endMapImage);
	}
	
	private void addMainTable() {
		initMainTable();

		// Add Table Content
		addResultLabel();
		addHomeButton();
		addExitButton();

		stage.addActor(mainTable);
	}

	private void initMainTable() {
		// Set table to fill stage
		mainTable.setFillParent(true);
		// Set alignment of contents in the table.
		mainTable.top();
	}

	private void addResultLabel() {
		Label resultLabel = new Label(getResultText(), ticTacToeGame.getSkin());
		mainTable.add(resultLabel).pad(40f, 30f, 10f, 30f);
		mainTable.row();
	}

	private String getResultText() {
		String resultText = "";
		if (ticTacToeGame.getGameLogic().isDraw()) {
			resultText = getDrawText();
		} else if (ticTacToeGame.getGameLogic().isPlayingAgainstAI()) {
			resultText = getPlayingAgainstAIText();
		} else {
			resultText = getOnlyHumansPlayingText();
		}
		return resultText;
	}

	private String getDrawText() {
		return "[TTT_BLUE]DRAW!";
	}

	private String getPlayingAgainstAIText() {
		boolean humanPlayerLost = ticTacToeGame.getGameLogic().getWinner().isAIPlayer();
		return humanPlayerLost ? "[TTT_RED]YOU LOST!" : "[TTT_GREEN]YOU WON!";
	}

	private String getOnlyHumansPlayingText() {
		Player winner = ticTacToeGame.getGameLogic().getWinner();
		PlayerSign winnerSign = winner.getPlayerSign();
		return "[" + winnerSign.getColorName() + "] PLAYER " + winner.getName() + " WON!";
	}

	private void addHomeButton() {
		TextButton homeButton = new TextButton("Home", ticTacToeGame.getSkin());
		homeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ticTacToeGame.setLoadingScreen();
			}
		});
		mainTable.add(homeButton).pad(10f, 0f, 0f, 0f);
		mainTable.row();
	}

	private void addExitButton() {
		TextButton exitButton = new TextButton("Exit", ticTacToeGame.getSkin());
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ticTacToeGame.exitGame();
			}
		});
		mainTable.add(exitButton).pad(10f, 0f, 40f, 0f);
	}

}