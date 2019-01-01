package de.sengerts.tictactoe.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.sengerts.tictactoe.logic.GameLogic;
import de.sengerts.tictactoe.model.settings.AIDifficulty;
import de.sengerts.tictactoe.model.settings.Dimension;
import de.sengerts.tictactoe.ui.screens.EndScreen;
import de.sengerts.tictactoe.ui.screens.LoadingScreen;
import de.sengerts.tictactoe.ui.screens.OptionsScreen;
import de.sengerts.tictactoe.ui.screens.PlayScreen;
import de.sengerts.tictactoe.ui.screens.TitleScreen;

/**
 * Class representing a tic tac toe game.
 * 
 * @author Tobias Senger
 */
public class TicTacToeGame extends Game {
	
	/**
	 * Class variable that stores this game's UI's menu width in pixels.
	 */
	public static final int MENU_WIDTH = 900;
	/**
	 * Class variable that stores this game's UI's menu height in pixels.
	 */
	public static final int MENU_HEIGHT = 900;

	/**
	 * Instance variable that stores this game's territory size.
	 */
	private Dimension territorySize;
	/**
	 * Instance variable that stores whether this is game against AI.
	 */
	private boolean aiOpponent;
	/**
	 * Instance variable that stores this game's AI difficulty.
	 */
	private AIDifficulty aiDifficulty;
	
	// Asset management and rendering
	/**
	 * Instance variable that stores the asset manager this game's UI.
	 */
	private AssetManager assetManager;
	/**
	 * Instance variable that stores the skin of this game's UI.
	 */
	private Skin skin;
	/**
	 * Instance variable that stores the shape renderer of this game's UI.
	 */
	private ShapeRenderer shapeRenderer;
	
	/**
	 * Instance variable that stores the game logic of this game.
	 */
	private GameLogic gameLogic;

	/**
	 * Creates this game.
	 * 
	 * Creates this game by first initiliazing the territory size,
	 * the AI difficulty, asset manager and shape renderer and whether
	 * this is a game against an AI player before setting the loading
	 * screen.
	 */
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	@Override
	public void create() {
		this.territorySize = new Dimension(3, 3);
		this.aiOpponent = true;
		this.aiDifficulty = AIDifficulty.MEDIUM;
		this.assetManager = new AssetManager();
		this.shapeRenderer = new ShapeRenderer();
		
		setLoadingScreen();
	}

	/**
	 * Adjusts this game's viewport.
	 * 
	 * Adjusts this game's viewport by calculating a fitting new viewport
	 * that keeps the aspect ratio, which is 1:1, of the games content and
	 * surrounds it by "black" bars.
	 * 
	 * @param width the new width of the game window
	 * @param height the new height of the game window
	 * @param viewport a specific viewport to update
	 */
	public void adjustViewport(int width, int height, Viewport viewport) {
		Vector2 size = Scaling.fit.apply(1, 1, width, height);
		int viewportX = (int) (width - size.x) / 2;
		int viewportY = (int) (height - size.y) / 2;
		int viewportWidth = (int) size.x;
		int viewportHeight = (int) size.y;
		Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
		
		if (viewport != null) {
			viewport.update(viewportWidth, viewportHeight);
			viewport.setScreenPosition(viewportX, viewportY);
			viewport.apply();
		}
	}

	/**
	 * Adjusts this game's viewport.
	 * 
	 * Adjusts this game's viewport by calling the overloaded
	 * method adjustViewport with null as the viewport object.
	 * 
	 * @param width the new width of the game window
	 * @param height the new height of the game window
	 */
	public void adjustViewport(int width, int height) {
		adjustViewport(width, height, null);
	}
	
	/**
	 * Sets the loading screen.
	 * 
	 * Sets the loading screen by setting the current screen of this
	 * game to a new loading screen.
	 */
	public void setLoadingScreen() {
		setScreen(new LoadingScreen(this));
	}

	/**
	 * Sets the title screen.
	 * 
	 * Sets the title screen by setting the current screen of this
	 * game to a new title screen.
	 */
	public void setTitleScreen() {
		TitleScreen titleScreen = new TitleScreen(this);
		// new TransitionScreen(this, getScreen(), titleScreen)
		setScreen(titleScreen);
	}
	
	/**
	 * Sets the options screen.
	 * 
	 * Sets the options screen by setting the current screen of this
	 * game to a new options screen.
	 */
	public void setOptionsScreen() {
		OptionsScreen optionsScreen = new OptionsScreen(this);
		setScreen(optionsScreen);
	}

	/**
	 * Sets the play screen.
	 * 
	 * Sets the play screen by setting the current screen of this
	 * game to a new play screen.
	 */
	public void setPlayScreen() {
		this.gameLogic = new GameLogic(getTerritorySize(), isAiOpponent(), getAiDifficulty());
		
		PlayScreen playScreen = new PlayScreen(this);
		// new TransitionScreen(this, getScreen(), playScreen)
		setScreen(playScreen);
	}

	/**
	 * Sets the end screen.
	 * 
	 * Sets the end screen by setting the current screen of this
	 * game to a new end screen.
	 */
	public void setEndScreen() {
		takeEndMapScreenshot();
		EndScreen endScreen = new EndScreen(this);
		// new TransitionScreen(this, getScreen(), endScreen)
		setScreen(endScreen);
	}

	/**
	 * Exits this game.
	 * 
	 * Exits this game by calling the exit method of static object
	 * app in class Gdx.
	 */
	public void exitGame() {
		Gdx.app.exit();
	}

	/**
	 * Takes a screenshot of the game.
	 * 
	 * Takes a screenshot of the game.
	 * Snippet taken from https://github.com/libgdx/libgdx/wiki/Taking-a-Screenshot. 
	 */
	private void takeEndMapScreenshot() {
		byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(),
				Gdx.graphics.getBackBufferHeight(), true);

		// this loop makes sure the whole screenshot is opaque and looks exactly like
		// what the user is seeing
		for (int i = 4; i < pixels.length; i += 4) {
			pixels[i - 1] = (byte) 255;
		}

		Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(),
				Pixmap.Format.RGBA8888);
		BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
		PixmapIO.writePNG(Gdx.files.local("endMaps/lastEndMap.png"), pixmap);
		pixmap.dispose();
	}

	/**
	 * Renders this game.
	 * 
	 * Renders this game by rendering this game's
	 * current screen.
	 */
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Game#render()
	 */
	@Override
	public void render() {
		super.render();
	}

	/**
	 * Gets this game's game logic.
	 * 
	 * Gets this game's game logic by retrieving the value
	 * from the instance variable gameLogic.
	 * 
	 * @return this game's game logic
	 */
	public GameLogic getGameLogic() {
		return gameLogic;
	}

	/**
	 * Gets this game's UI skin.
	 * 
	 * Gets this game's UI skin by retrieving the value of the
	 * instance variable skin.
	 * 
	 * @return this game's UI skin
	 */
	public Skin getSkin() {
		return skin;
	}
	
	/**
	 * Sets this game's UI skin.
	 * 
	 * Sets this game's UI skin by assigning the given value to
	 * the instance variable skin.
	 * 
	 * @param skin the new skin for this game's UI
	 */
	public void setSkin(Skin skin) {
		this.skin = skin;
	}

	/**
	 * Gets this game's UI shape renderer.
	 * 
	 * Gets this game's UI shape renderer by retrieving the value
	 * of the instance variable shapeRenderer.
	 * 
	 * @return this game's UI shape renderer
	 */
	public ShapeRenderer getShapeRenderer() {
		return shapeRenderer;
	}

	/**
	 * Gets this game's UI asset manager.
	 * 
	 * Gets this game's UI asset manager by retrieving the value
	 * of the instance variable assetManager.
	 * 
	 * @return this game's UI asset manager
	 */
	public AssetManager getAssetManager() {
		return assetManager;
	}

	/**
	 * Gets the territory size.
	 * 
	 * Gets and returns the size of this game's territory by retrieving
	 * the value of the instance variable territorySize.
	 * 
	 * @return this game's territory size
	 */
	public Dimension getTerritorySize() {
		return territorySize;
	}

	/**
	 * Sets this game's territory size.
	 * 
	 * Sets this game's territory size by assigning the given territory size
	 * Dimension object to the the instance variable territorySize.
	 * 
	 * @param territorySize the new territory size for this game
	 */
	public void setTerritorySize(Dimension territorySize) {
		this.territorySize = territorySize;
	}

	/**
	 * Gets whether this is a game against an AI player.
	 * 
	 * Gets whether this is a game against an AI player by
	 * retrieving and returning the boolean value in the
	 * the instance variable aiOpponent.
	 * 
	 * @return whether this is a game against AI
	 */
	public boolean isAiOpponent() {
		return aiOpponent;
	}

	/**
	 * Sets whether this is a game against an AI player.
	 * 
	 * Sets whether this is a game against an AI player by assigning
	 * the given AI opponent boolean value to the
	 * instance variable aiOpponent.
	 * 
	 * @param aiOpponent boolean value indicating whether this is a game
	 * against an AI player
	 */
	public void setAiOpponent(boolean aiOpponent) {
		this.aiOpponent = aiOpponent;
	}

	/**
	 * Gets the AI difficulty.
	 * 
	 * Gets and returns the current AI difficulty by retrieving the
	 * value of the instance variable aiDifficulty.
	 * 
	 * @return the current AI difficulty
	 */
	public AIDifficulty getAiDifficulty() {
		return aiDifficulty;
	}

	/**
	 * Sets this game's AI difficulty.
	 * 
	 * Sets the AI difficulty by assigning the given AI difficulty
	 * value to the the instance variable aiDifficulty.
	 * 
	 * @param aiDifficulty the new AI difficulty to set
	 */
	public void setAiDifficulty(AIDifficulty aiDifficulty) {
		this.aiDifficulty = aiDifficulty;
	}

}
