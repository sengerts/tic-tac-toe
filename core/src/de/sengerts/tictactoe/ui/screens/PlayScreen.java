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

public class PlayScreen implements Screen {

	private final TicTacToeGame ticTacToeGame;
	private final SpriteBatch batch;
	private final OrthographicCamera camera;
	private final SpriteCache cache;
	private final HashMap<PlayerSign, TextureRegion> playerTextureRegions;

	public PlayScreen(final TicTacToeGame ticTacToeGame) {
		this.ticTacToeGame = ticTacToeGame;
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
		this.batch = new SpriteBatch();
		this.cache = new SpriteCache(rowsCount * columnsCount, false);

		createTiles();
		loadPlayerTextures();
	}

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

	private void loadPlayerTextures() {
		Skin skin = ticTacToeGame.getSkin();
		for (PlayerSign playerSign : PlayerSign.values()) {
			String textureRegionName = playerSign.name().toLowerCase();
			TextureRegion playerTextureRegion = skin.getRegion(textureRegionName);

			playerTextureRegions.put(playerSign, playerTextureRegion);
		}
	}

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

	private void drawTiles() {
		Territory territory = ticTacToeGame.getGameLogic().getTerritory();

		cache.setProjectionMatrix(camera.combined);
		cache.begin();
		cache.draw(0, 0, territory.getTilesAmount());
		cache.end();
	}

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

	@Override
	public void resize(int width, int height) {
		ticTacToeGame.adjustViewport(width, height);
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
		cache.dispose();
		batch.dispose();
	}

}