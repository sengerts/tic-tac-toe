package de.sengerts.tictactoe.ui.screens;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.sengerts.tictactoe.model.board.Territory;
import de.sengerts.tictactoe.model.board.Tile;
import de.sengerts.tictactoe.model.players.PlayerSign;
import de.sengerts.tictactoe.model.settings.Dimension;
import de.sengerts.tictactoe.ui.TicTacToeGame;

/**
 * Class representing a play screen for a tic tac toe game.
 * 
 * @author Tobias Senger
 */
public class PlayScreen implements Screen {

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
	 * Instance variable that stores the sprite cache for this screen.
	 */
	private final SpriteCache cache;
	/**
	 * Instance variable that stores the camera for this screen.
	 */
	private final OrthographicCamera camera;
	/**
	 * Instance variable that stores the player textures map for this screen.
	 */
	private final HashMap<PlayerSign, TextureRegion> playerTextureRegions;

	/**
	 * Another constructor for class PlayScreen.
	 * 
	 * Creates a new object of type PlayScreen by assigning the tic tac toe game
	 * associated with this screen to the instance variable ticTacToeGame and
	 * initializing the player texture regions map, sprite batch and camera
	 * for this screen before creating the tiles sprites and loading the player textures.
	 * 
	 * @param ticTacToeGame the tic tac toe game this screen is associated with
	 */
	public PlayScreen(final TicTacToeGame ticTacToeGame) {
		this.ticTacToeGame = ticTacToeGame;
		
		this.batch = new SpriteBatch();
		this.playerTextureRegions = new HashMap<PlayerSign, TextureRegion>();

		Territory territory = ticTacToeGame.getGameLogic().getTerritory();
		Dimension territorySize = territory.getSize();
		int columnsCount = territorySize.getColumnsCount();
		int rowsCount = territorySize.getRowsCount();

		float width = 16 * columnsCount;
		float height = 16 * rowsCount;

		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(true, width, height);
		this.camera.position.set(width / 2, height / 2, 0);
		this.cache = new SpriteCache(rowsCount * columnsCount, false);

		createTiles();
		loadPlayerTextures();
	}

	/**
	 * Creates the tiles for the sprite cache.
	 * 
	 * Creates the tiles for the sprite cache by creating sprites for all
	 * tiles of this screen's game's territory and predefining the positions
	 * for these tile sprites on the screen before adding them to this screen's
	 * sprite cache.
	 */
	private void createTiles() {
		Sprite tileSprite = ticTacToeGame.getSkin().getSprite("tile");
		Territory territory = ticTacToeGame.getGameLogic().getTerritory();

		cache.beginCache();
		for (Tile tile : territory.getTiles()) {
			int tileX = tile.getLocation().getColumn();
			int tileY = tile.getLocation().getRow();

			tileSprite.setPosition(tileX * 16, tileY * 16);
			cache.add(tileSprite);
		}
		cache.endCache();
	}

	/**
	 * Loads the player textures.
	 * 
	 * Loads the player textures by getting the values for all
	 * possible player signs of a tic tac toe game from this scren's
	 * game's skin and adding the textures to the map in the instance
	 * variable playerTextureRegions.
	 */
	private void loadPlayerTextures() {
		Skin skin = ticTacToeGame.getSkin();
		for (PlayerSign playerSign : PlayerSign.values()) {
			String textureRegionName = playerSign.name().toLowerCase();
			TextureRegion playerTextureRegion = skin.getRegion(textureRegionName);

			playerTextureRegions.put(playerSign, playerTextureRegion);
		}
	}

	/**
	 * Shows this play screen.
	 * 
	 * When this play screen is shown, a new input processor is
	 * created and assigned which makes a human player move whenever
	 * the screen is touched.
	 */
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean touchUp(int x, int y, int pointer, int button) {
				ticTacToeGame.getGameLogic().makeHumanPlayerMove();
				return true;
			}
		});
	}

	/**
	 * Renders this play screen.
	 * 
	 * Renders this play screen by clearing it, updating this screen's
	 * camera, drawing the tiles from the sprite cache and drawing the marks
	 * with the sprite batch of this screen. Finally, when the game is ended,
	 * this screen's game is set to end screen.
	 */
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		drawTiles();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		drawMarks();
		batch.end();

		if (ticTacToeGame.getGameLogic().isEnded()) {
			ticTacToeGame.setEndScreen();
		}
	}

	/**
	 * Draws the tiles.
	 * 
	 * Draws the prefined tiles by drawing all predefined
	 * tiles from the sprite cache of this screen.
	 */
	private void drawTiles() {
		Territory territory = ticTacToeGame.getGameLogic().getTerritory();

		cache.setProjectionMatrix(camera.combined);
		cache.begin();
		cache.draw(0, 0, territory.getTilesAmount());
		cache.end();
	}

	/**
	 * Draws the marks.
	 * 
	 * Draws the marks by looping over all tiles of this screen's game's
	 * territory and drawing the player sign texture of the player who marked
	 * the current tile in the corresponding tile on the screen using this screen's
	 * sprite batch.
	 */
	private void drawMarks() {
		Territory territory = ticTacToeGame.getGameLogic().getTerritory();
		for (Tile tile : territory.getTiles()) {
			if (!tile.isMarked())
				continue;

			PlayerSign markedPlayerSign = tile.getMarkedPlayer().getPlayerSign();
			TextureRegion markedPlayerTextureRegion = playerTextureRegions.get(markedPlayerSign);

			int spriteX = tile.getLocation().getColumn() * 16;
			int spriteY = tile.getLocation().getRow() * 16;

			batch.draw(markedPlayerTextureRegion, spriteX, spriteY);
		}
	}

	/**
	 * Resizes this play screen.
	 * 
	 * Resizes this play screen by calling the screen's game's
	 * adjustViewport method with the new window width and height
	 * and the viewport of this screen as parameters.
	 */
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		ticTacToeGame.adjustViewport(width, height);
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
	 * Disposes this play screen.
	 * 
	 * Disposes this play screen by disposing this screen's
	 * sprite cache and sprite batch.
	 */
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
		cache.dispose();
		batch.dispose();
	}

}