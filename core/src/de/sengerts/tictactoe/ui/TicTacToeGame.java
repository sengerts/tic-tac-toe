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
import de.sengerts.tictactoe.model.Dimension;
import de.sengerts.tictactoe.ui.screens.EndScreen;
import de.sengerts.tictactoe.ui.screens.LoadingScreen;
import de.sengerts.tictactoe.ui.screens.OptionsScreen;
import de.sengerts.tictactoe.ui.screens.PlayScreen;
import de.sengerts.tictactoe.ui.screens.TitleScreen;

public class TicTacToeGame extends Game {
	
	public static final int MENU_WIDTH = 900;
	public static final int MENU_HEIGHT = 900;

	private Dimension territorySize;
	private boolean aiOpponent;
	private GameLogic gameLogic;

	private AssetManager assetManager;
	private Skin skin;

	private ShapeRenderer shapeRenderer;

	@Override
	public void create() {
		this.territorySize = new Dimension(3, 3);
		this.aiOpponent = true;
		this.assetManager = new AssetManager();
		this.shapeRenderer = new ShapeRenderer();
		
		setLoadingScreen();
	}

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

	public void adjustViewport(int width, int height) {
		adjustViewport(width, height, null);
	}
	
	public void setLoadingScreen() {
		setScreen(new LoadingScreen(this));
	}

	public void setTitleScreen() {
		TitleScreen titleScreen = new TitleScreen(this);
		// new TransitionScreen(this, getScreen(), titleScreen)
		setScreen(titleScreen);
	}
	
	public void setOptionsScreen() {
		OptionsScreen optionsScreen = new OptionsScreen(this);
		setScreen(optionsScreen);
	}

	public void setPlayScreen() {
		this.gameLogic = new GameLogic(getTerritorySize(), isAiOpponent());
		
		PlayScreen playScreen = new PlayScreen(this);
		// new TransitionScreen(this, getScreen(), playScreen)
		setScreen(playScreen);
	}

	public void setEndScreen() {
		takeEndMapScreenshot();
		EndScreen endScreen = new EndScreen(this);
		// new TransitionScreen(this, getScreen(), endScreen)
		setScreen(endScreen);
	}

	public void exitGame() {
		Gdx.app.exit();
	}

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

	@Override
	public void render() {
		super.render();
	}

	public GameLogic getGameLogic() {
		return gameLogic;
	}

	public Skin getSkin() {
		return skin;
	}
	
	public void setSkin(Skin skin) {
		this.skin = skin;
	}

	public ShapeRenderer getShapeRenderer() {
		return shapeRenderer;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public Dimension getTerritorySize() {
		return territorySize;
	}

	public void setTerritorySize(Dimension territorySize) {
		this.territorySize = territorySize;
	}

	public boolean isAiOpponent() {
		return aiOpponent;
	}

	public void setAiOpponent(boolean aiOpponent) {
		this.aiOpponent = aiOpponent;
	}

}
