package de.sengerts.tictactoe.ui.screens;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.sengerts.tictactoe.ui.TicTacToeGame;

public class LoadingScreen implements Screen {

	//private static final float PROGRESS_BAR_WIDTH = 400f;
	//private static final float PROGRESS_BAR_HEIGHT = 50f;
	
	private final TicTacToeGame ticTacToeGame;
	//private final ShapeRenderer shapeRenderer;
	private final SpriteBatch batch;
	private final Viewport viewport;
	private final OrthographicCamera camera;
	
	private boolean alreadyFinished;

	public LoadingScreen(final TicTacToeGame ticTacToeGame) {
		this.ticTacToeGame = ticTacToeGame;
		//this.shapeRenderer = ticTacToeGame.getShapeRenderer();

		this.batch = new SpriteBatch();
		this.camera = new OrthographicCamera();
		
		this.viewport = new FitViewport(TicTacToeGame.MENU_WIDTH, TicTacToeGame.MENU_HEIGHT, camera);
		viewport.apply();
		
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();

		this.alreadyFinished = false;
	}

	@Override
	public void show() {
		loadAssets();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(45f/255f, 49f/255f, 66f/255f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//renderProgressBar();
		
		if (!alreadyFinished && ticTacToeGame.getAssetManager().update()) {
			finishLoading();
			return;
		}
	}
	
	/*private void renderProgressBar() {
		float progress = ticTacToeGame.getAssetManager().getProgress();
		
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		
		float barX = (TicTacToeGame.SCREEN_WIDTH - PROGRESS_BAR_WIDTH) / 2f;
		float barY = (TicTacToeGame.SCREEN_HEIGHT - PROGRESS_BAR_HEIGHT) / 2f;
		float filledWidth = PROGRESS_BAR_WIDTH * progress;
		float filledHeight = PROGRESS_BAR_HEIGHT;
		
		shapeRenderer.rect(barX, barY, filledWidth, filledHeight);
		shapeRenderer.end();
	}*/

	private void loadAssets() {
		initFreeTypeLoader();
		loadFonts();
		ticTacToeGame.getAssetManager().finishLoading();
		loadSkin();
		ticTacToeGame.getAssetManager().finishLoading();
	}

	private void finishLoading() {
		this.alreadyFinished = true;
		Skin skin = ticTacToeGame.getAssetManager().get("uiskin/uiskin.json", Skin.class);
		addColors(skin);
		ticTacToeGame.setSkin(skin);
		ticTacToeGame.setTitleScreen();
	}

	private void initFreeTypeLoader() {
		InternalFileHandleResolver fileHandler = new InternalFileHandleResolver();
		FreeTypeFontGeneratorLoader fontGeneratorLoader = new FreeTypeFontGeneratorLoader(fileHandler);
		FreetypeFontLoader fontLoader = new FreetypeFontLoader(fileHandler);
		ticTacToeGame.getAssetManager().setLoader(FreeTypeFontGenerator.class, fontGeneratorLoader);
		ticTacToeGame.getAssetManager().setLoader(BitmapFont.class, ".ttf", fontLoader);
	}

	private void loadSkin() {
		ObjectMap<String, Object> resources = new ObjectMap<String, Object>();
		for (int fontSize : Arrays.asList(24, 32, 64, 128)) {
			BitmapFont font = ticTacToeGame.getAssetManager().get("font-" + fontSize + ".ttf", BitmapFont.class);
			font.getData().markupEnabled = true;
			font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			resources.put("font-" + fontSize, font);
			
			font = ticTacToeGame.getAssetManager().get("dec-font-" + fontSize + ".ttf", BitmapFont.class);
			font.getData().markupEnabled = true;
			font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			resources.put("dec-font-" + fontSize, font);
		}
		ticTacToeGame.getAssetManager().load("uiskin/uiskin.atlas", TextureAtlas.class);
		SkinParameter parameter = new SkinLoader.SkinParameter("uiskin/uiskin.atlas", resources);
		ticTacToeGame.getAssetManager().load("uiskin/uiskin.json", Skin.class, parameter);
	}

	private void loadFonts() {
		for (int fontSize : Arrays.asList(24, 32, 64, 128)) {
			FreeTypeFontLoaderParameter parameter = getFontParameter(fontSize);
			ticTacToeGame.getAssetManager().load("font-" + fontSize + ".ttf", BitmapFont.class, parameter);
		
			parameter = getFontParameter(fontSize);
			addFontDecoration(parameter);
			ticTacToeGame.getAssetManager().load("dec-font-" + fontSize + ".ttf", BitmapFont.class, parameter);
		}
	}
	
	private FreeTypeFontLoaderParameter getFontParameter(int fontSize) {
		FreeTypeFontLoaderParameter parameter = new FreeTypeFontLoaderParameter();
		parameter.fontFileName = "fonts/font.ttf";
		parameter.fontParameters.size = fontSize;
		return parameter;
	}
	
	private void addFontDecoration(FreeTypeFontLoaderParameter parameter) {
		parameter.fontParameters.borderColor = new Color(0, 0, 0, 0.5f);
		parameter.fontParameters.borderWidth = 2;
		parameter.fontParameters.shadowColor = new Color(0, 0, 0, 0.5f);
		parameter.fontParameters.shadowOffsetX = 3;
		parameter.fontParameters.shadowOffsetY = 5;
	}

	private void addColors(Skin skin) {
		Colors.put("TTT_GREEN", skin.getColor("green"));
		Colors.put("TTT_RED", skin.getColor("red"));
		Colors.put("TTT_BLUE", skin.getColor("blue"));
		Colors.put("TTT_DARK", skin.getColor("dark"));
		Colors.put("TTT_LIGHT", skin.getColor("light"));
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
	}

}
