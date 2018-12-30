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

public class OptionsScreen implements Screen {

	private final TicTacToeGame ticTacToeGame;
	private final SpriteBatch batch;
	protected final Stage stage;
	private final Viewport viewport;
	private final OrthographicCamera camera;

	private Slider slider;
	private Label sliderLabel;
	private CheckBox checkbox;
	private SelectBox<AIDifficulty> selectBox; 

	private int territorySize;
	private boolean aiOpponent;
	private AIDifficulty aiDifficulty;

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
		Label titleLabel = new Label("[TTT_BLUE]Settings", ticTacToeGame.getSkin());

		// Create Slider
		this.sliderLabel = new Label("[TTT_LIGHT]Amount of rows/ columns: " + territorySize, ticTacToeGame.getSkin(),
				"dec-font-32", "light");
		this.slider = new Slider(3, 63, 2, false, ticTacToeGame.getSkin());
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

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(45f / 255f, 49f / 255f, 66f / 255f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		updateSlider();
		updateCheckbox();
		updateAIDifficulty();

		stage.act();
		stage.draw();
	}

	private void updateSlider() {
		this.territorySize = (int) slider.getValue();
		sliderLabel.setText("[TTT_LIGHT]Amount of rows / columns: " + territorySize);
	}

	private void updateCheckbox() {
		this.aiOpponent = checkbox.isChecked();
	}
	
	private void updateAIDifficulty() {
		this.aiDifficulty = selectBox.getSelected();
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