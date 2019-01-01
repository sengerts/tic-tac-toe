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

import de.sengerts.tictactoe.model.players.Player;
import de.sengerts.tictactoe.model.players.PlayerSign;
import de.sengerts.tictactoe.ui.TicTacToeGame;

/**
 * Class representing an end screen for a tic tac toe game.
 * 
 * @author Tobias Senger
 */
public class EndScreen implements Screen {

	/**
	 * Instance variable that stores the tic tac toe game this screen
	 * is associated with.
	 */
	private final TicTacToeGame ticTacToeGame;
	/**
	 * Instance variable that stores the stage for this screen.
	 */
	private final Stage stage;
	/**
	 * Instance variable that stores the viewport for this screen.
	 */
	private final Viewport viewport;
	/**
	 * Instance variable that stores the camera for this screen.
	 */
	private final OrthographicCamera camera;
	/**
	 * Instance variable that stores the main table for this screen.
	 */
	private final Table mainTable;

	/**
	 * Another constructor for class EndScreen.
	 * 
	 * Creates a new object of type EndScreen by assigning the tic tac toe game
	 * associated with this screen to the instance variable ticTacToeGame and
	 * initializing the main table, camera, viewport and stage of this screen.
	 * 
	 * @param ticTacToeGame the tic tac toe game this screen is associated with
	 */
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

	/**
	 * Shows this end screen.
	 * 
	 * Sets the input processor to this screen's stage,
	 * adds the end map and adds the main table for the screen
	 * when this screen is shown.
	 */
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		// Let stage control input
		Gdx.input.setInputProcessor(stage);

		addEndMap();
		addMainTable();
	}
	
	/**
	 * Renders this end screen.
	 * 
	 * Renders this end screen by clearing it with the dark game
	 * color, letting the stage act and then drawing it.
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
	 * Resizes this end screen.
	 * 
	 * Resizes this end screen by calling the screen's game's
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
	 * Disposes this end screen.
	 * 
	 * Disposes this end screen by disposing this screen's
	 * stage.
	 */
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
		stage.dispose();
	}
	
	/**
	 * Adds the end map.
	 * 
	 * Adds the end map, in detail: the screenshot of this screen's
	 * tic tac toe game directly after the game has ended, as an image
	 * to the mid of this screens stage.
	 */
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
	
	/**
	 * Add the main table.
	 * 
	 * Adds the main table for this screen by initiliazing the main
	 * table and adding a result label, home button and exit button
	 * to the main table before adding this table as an actor to this
	 * screen's stage.
	 */
	private void addMainTable() {
		initMainTable();

		addResultLabel();
		addHomeButton();
		addExitButton();

		stage.addActor(mainTable);
	}

	/**
	 * Initializes this screen's main table.
	 * 
	 * Initializes this screen's main table by setting
	 * the main table to fill the parent stage and setting
	 * the alignment of the contents in the table to the top.
	 */
	private void initMainTable() {
		mainTable.setFillParent(true);
		mainTable.top();
	}

	/**
	 * Adds a result label.
	 * 
	 * Adds a result label by creating a new label with the result text
	 * ({@link #getResultText()}) and adding it to the top of this screen's
	 * main table before adding a new row to the main table.
	 */
	private void addResultLabel() {
		Label resultLabel = new Label(getResultText(), ticTacToeGame.getSkin());
		mainTable.add(resultLabel).pad(40f, 30f, 10f, 30f);
		mainTable.row();
	}

	/**
	 * Gets the result text.
	 * 
	 * Gets the result text for this screen's ended tic tac toe game.
	 * When there is a draw the text retrieved by {@link #getDrawText()} is returned.
	 * When this screen's game was against an AI player, the text retrieved by
	 * {@link #getPlayingAgainstAIText()} is returned, otherwise the text retrieved
	 * by {@link #getOnlyHumansPlayingText()} is returned.
	 * 
	 * @return the result text for this screen's tic tac toe game
	 */
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

	/**
	 * Gets the draw text.
	 * 
	 * Gets the draw text by returning the text in uppercase letters
	 * and blue color "DRAW!".
	 * 
	 * @return the text to be shown when this screen's game was a draw
	 */
	private String getDrawText() {
		return "[TTT_BLUE]DRAW!";
	}

	/**
	 * Gets the won in an AI match text.
	 * 
	 * Gets the won in an AI match text by returning the uppercased text
	 * "YOU LOST!" in red letters when the human player has lost against the AI
	 * opponent or otherwise returning the uppercased text "YOU WON!" in green
	 * letters.
	 * 
	 * @return the text to be shown when a player won in this screen's AI game
	 */
	private String getPlayingAgainstAIText() {
		boolean humanPlayerLost = ticTacToeGame.getGameLogic().getWinner().isAIPlayer();
		return humanPlayerLost ? "[TTT_RED]YOU LOST!" : "[TTT_GREEN]YOU WON!";
	}

	/**
	 * Gets the won in a human players match text.
	 * 
	 * Gets the won in a human players match text by returning the uppercased text
	 * "YOU LOST!" in red letters when the human player has lost against the AI
	 * opponent or otherwise returning the uppercased text "YOU WON!" in green
	 * letters.
	 * 
	 * @return the text to be shown when a player won in this screen's human players game
	 */
	private String getOnlyHumansPlayingText() {
		Player winner = ticTacToeGame.getGameLogic().getWinner();
		PlayerSign winnerSign = winner.getPlayerSign();
		return "[" + winnerSign.getColorName() + "] PLAYER " + winner.getName() + " WON!";
	}

	/**
	 * Adds a home button.
	 * 
	 * Adds a home button by creating a new text button with the text
	 * "Home" on it, with a click listener that sets this screen's game
	 * to loading screen as soon as the button is clicked, and adding it
	 * to the main table of this screen before adding a new row to the 
	 * main table.
	 */
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

	/**
	 * Adds an exit button.
	 * 
	 * Adds an exit button by creating a new text button with the text
	 * "Exit" on it, with a click listener that exits this screen's game
	 * window as soon as the button is clicked, and adding it
	 * to the main table of this screen.
	 */
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