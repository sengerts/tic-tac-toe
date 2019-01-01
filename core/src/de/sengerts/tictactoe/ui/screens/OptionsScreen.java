package de.sengerts.tictactoe.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.sengerts.tictactoe.model.settings.AIDifficulty;
import de.sengerts.tictactoe.model.settings.Dimension;
import de.sengerts.tictactoe.ui.TicTacToeGame;

/**
 * Class representing an options screen for a tic tac toe game.
 * 
 * @author Tobias Senger
 */
public class OptionsScreen implements Screen {

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
	 * Instance variable that stores the territory size slider for this screen.
	 */
	private Slider slider;
	/**
	 * Instance variable that stores the territory size label for this screen.
	 */
	private Label sliderLabel;
	/**
	 * Instance variable that stores the AI mode checkbox for this screen.
	 */
	private CheckBox checkbox;
	/**
	 * Instance variable that stores the AI difficulty select box for this screen.
	 */
	private SelectBox<AIDifficulty> selectBox; 

	/**
	 * Instance variable that stores the territory size of this screen's tic tac toe game.
	 */
	private int territorySize;
	/**
	 * Instance variable that stores whether this screen's tic tac toe game is against AI.
	 */
	private boolean aiOpponent;
	/**
	 * Instance variable that stores the AI difficulty of this screen's tic tac toe game.
	 */
	private AIDifficulty aiDifficulty;

	/**
	 * Another constructor for class OptionsScreen.
	 * 
	 * Creates a new object of type OptionsScreen by assigning the tic tac toe game
	 * associated with this screen to the instance variable ticTacToeGame and the
	 * games territory size, AI difficulty and whether the game is against AI to
	 * its corresponding instance variables territorySize, aiOpponent and aiDifficulty.
	 * Then the sprite batch, camera, viewport and stage for this screen are 
	 * initialized the main table, camera, viewport and stage of this screen.
	 * 
	 * @param ticTacToeGame the tic tac toe game this screen is associated with
	 */
	public OptionsScreen(final TicTacToeGame ticTacToeGame) {
		this.ticTacToeGame = ticTacToeGame;
		this.territorySize = ticTacToeGame.getTerritorySize().getRowsCount();
		this.aiOpponent = ticTacToeGame.isAiOpponent();
		this.aiDifficulty = ticTacToeGame.getAiDifficulty();

		this.batch = new SpriteBatch();
		this.camera = new OrthographicCamera();

		this.viewport = new FitViewport(TicTacToeGame.MENU_WIDTH, TicTacToeGame.MENU_HEIGHT, camera);
		viewport.apply();

		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();

		this.stage = new Stage(viewport, batch);
	}

	/**
	 * Shows this options screen.
	 * 
	 * When this options screen is shown, this screen's stage is
	 * set as the input processor, the main table is initialized and
	 * content added to the table before adding the main table
	 * as an actor to this screen's stage.
	 */
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);

		Table mainTable = new Table();
		// Set table to fill stage
		mainTable.setFillParent(true);
		// Set alignment of contents in the table.
		mainTable.top();

		// Create Logo
		Label titleLabel = new Label("[TTT_BLUE]Settings", ticTacToeGame.getSkin());

		// Create Slider
		this.sliderLabel = new Label("[TTT_LIGHT]Amount of rows/ columns: " + territorySize, ticTacToeGame.getSkin(),
				"dec-font-32", "light");
		this.slider = new Slider(3, 21, 1, false, ticTacToeGame.getSkin());
		slider.setValue(territorySize);

		// Create Opponent Toggle
		this.checkbox = new CheckBox("AI opponent", ticTacToeGame.getSkin());
		checkbox.setChecked(aiOpponent);
		
		// Create AI difficulty select box
		Label aiDifficultyLabel = new Label("AI Difficulty:", ticTacToeGame.getSkin(), "dec-font-32", "light");
		this.selectBox = new SelectBox<AIDifficulty>(ticTacToeGame.getSkin());
		selectBox.setItems(AIDifficulty.values());
		selectBox.setSelected(aiDifficulty);

		// Create Save Button
		TextButton saveButton = new TextButton("Save", ticTacToeGame.getSkin());

		// Add listeners to button
		saveButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ticTacToeGame.setTerritorySize(new Dimension(territorySize, territorySize));
				ticTacToeGame.setAiOpponent(aiOpponent);
				ticTacToeGame.setAiDifficulty(aiDifficulty);
				ticTacToeGame.setTitleScreen();
			}
		});

		// Add actors to table
		mainTable.add(titleLabel).padTop(100f).padBottom(70f);
		mainTable.row();
		mainTable.add(sliderLabel).padBottom(10f);
		mainTable.row();
		mainTable.add(slider).padBottom(30f);
		mainTable.row();
		mainTable.add(checkbox).padBottom(30f);
		mainTable.row();
		mainTable.add(aiDifficultyLabel);
		mainTable.row();
		mainTable.add(selectBox);
		mainTable.row();
		mainTable.add(saveButton).padTop(70f);

		// Add table to stage
		stage.addActor(mainTable);
	}

	/**
	 * Renders this options screen.
	 * 
	 * Renders this options screen by clearing it with the "dark" game
	 * color, updating the slider label, AI opponent mode and AI difficulty
	 * values and letting this screen's stage act before drawing the stage.
	 */
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(45f / 255f, 49f / 255f, 66f / 255f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		updateSliderLabel();
		updateAIOpponent();
		updateAIDifficulty();

		stage.act();
		stage.draw();
	}

	/**
	 * Updates the slider label.
	 * 
	 * Updates the slider label by setting the right text for the
	 * sliders label with the currently set territory size.
	 */
	private void updateSliderLabel() {
		this.territorySize = (int) slider.getValue();
		sliderLabel.setText("[TTT_LIGHT]Amount of rows / columns: " + territorySize);
	}

	/**
	 * Updates whether this screen's game is against AI.
	 * 
	 * Updates whether this screen's game is against AI by assigning
	 * the checked value of the checkbox the instance variable aiOpponent.
	 */
	private void updateAIOpponent() {
		this.aiOpponent = checkbox.isChecked();
	}
	
	/**
	 * Updates the AI difficulty.
	 * 
	 * Updates the AI difficulty by assigning the selected
	 * value of the select box to the instance variable aiDifficulty.
	 */
	private void updateAIDifficulty() {
		this.aiDifficulty = selectBox.getSelected();
	}

	/**
	 * Resizes this options screen.
	 * 
	 * Resizes this options screen by calling the screen's game's
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
	 * Disposes this options screen.
	 * 
	 * Disposes this options screen by disposing this screen's
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