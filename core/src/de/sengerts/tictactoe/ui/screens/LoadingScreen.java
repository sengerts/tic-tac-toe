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

/**
 * Class representing a loading screen for a tic tac toe game.
 * 
 * @author Tobias Senger
 */
public class LoadingScreen implements Screen {

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
	 * Instance variable that stores the viewport for this screen.
	 */
	private final Viewport viewport;
	/**
	 * Instance variable that stores the camera for this screen.
	 */
	private final OrthographicCamera camera;
	/**
	 * Instance variable that stores whether the asset manager has already
	 * finished loading.
	 */
	private boolean alreadyFinished;

	/**
	 * Another constructor for class LoadingScreen.
	 * 
	 * Creates a new object of type LoadingScreen by assigning the tic tac toe game
	 * associated with this screen to the instance variable ticTacToeGame and
	 * initializing the sprite batch, camera and viewport for this screen and setting
	 * the value of the instance variable alreadyFinished to false.
	 * 
	 * @param ticTacToeGame the tic tac toe game this screen is associated with
	 */
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

	/**
	 * Shows this loading screen.
	 * 
	 * Loads the assets for this screen's game's UI when
	 * this screen is shown.
	 */
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		loadAssets();
	}

	/**
	 * Renders this loading screen.
	 * 
	 * Renders this loading screen by clearing this screen with the
	 * "dark" game color and updating the asset manager loading process.
	 * When the asset manager has finished loading and didn't finish
	 * yet, the method {@see finishLoading()} is called.
	 */
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
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

	/**
	 * Loads the screen's game's asset.
	 * 
	 * Loads the screen's game's asset by initiliazing the free type
	 * font loader, loading the needed fonts and after these have been
	 * loaded successfully loading the skin.
	 */
	private void loadAssets() {
		initFreeTypeLoader();
		loadFonts();
		ticTacToeGame.getAssetManager().finishLoading();
		loadSkin();
		ticTacToeGame.getAssetManager().finishLoading();
	}

	/**
	 * Finishes the loading process.
	 * 
	 * Finishes the loading process by setting the value of the instance
	 * variable alreadyFinished to true, getting the loaded skin and adding
	 * the game's own colors to it before setting it as the skin for this screen's
	 * tic tac toe game and setting this screen's game to title screen.
	 */
	private void finishLoading() {
		this.alreadyFinished = true;
		Skin skin = ticTacToeGame.getAssetManager().get("uiskin/uiskin.json", Skin.class);
		addColors(skin);
		ticTacToeGame.setSkin(skin);
		ticTacToeGame.setTitleScreen();
	}

	/**
	 * Initializes the free type fontl loader.
	 * 
	 * Initializes the free type fontl loader with the internal file
	 * handle resolver and ttf BitmapFont as the loaded file format.
	 */
	private void initFreeTypeLoader() {
		InternalFileHandleResolver fileHandler = new InternalFileHandleResolver();
		FreeTypeFontGeneratorLoader fontGeneratorLoader = new FreeTypeFontGeneratorLoader(fileHandler);
		FreetypeFontLoader fontLoader = new FreetypeFontLoader(fileHandler);
		ticTacToeGame.getAssetManager().setLoader(FreeTypeFontGenerator.class, fontGeneratorLoader);
		ticTacToeGame.getAssetManager().setLoader(BitmapFont.class, ".ttf", fontLoader);
	}

	/**
	 * Loads the skin for this screen's game.
	 * 
	 * Loads the skin for this screen's game by getting the generated fonts
	 * and loading the skin again with the generated fonts and the json skin
	 * file for the skin.
	 */
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

	/**
	 * Loads the needed fonts.
	 * 
	 * Loads the needed fonts by generating the game's font in different sizes and
	 * styles by the free type font loader of the asset manager.
	 */
	private void loadFonts() {
		for (int fontSize : Arrays.asList(24, 32, 64, 128)) {
			FreeTypeFontLoaderParameter parameter = getFontParameter(fontSize);
			ticTacToeGame.getAssetManager().load("font-" + fontSize + ".ttf", BitmapFont.class, parameter);
		
			parameter = getFontParameter(fontSize);
			addFontDecoration(parameter);
			ticTacToeGame.getAssetManager().load("dec-font-" + fontSize + ".ttf", BitmapFont.class, parameter);
		}
	}
	
	/**
	 * Gets a default font parameter for the fonts.
	 * 
	 * Gets a default font parameter for the fonts by creating
	 * a new free type font loader parameter with the path of the
	 * font file and the given font size.
	 * 
	 * @param fontSize the size of the font to generate
	 * @return the default font parameter for the font to generate in
	 * the given size
	 */
	private FreeTypeFontLoaderParameter getFontParameter(int fontSize) {
		FreeTypeFontLoaderParameter parameter = new FreeTypeFontLoaderParameter();
		parameter.fontFileName = "fonts/font.ttf";
		parameter.fontParameters.size = fontSize;
		return parameter;
	}
	
	/**
	 * Adds font decoration to the given font parameter.
	 * 
	 * Adds font decoration to the given font parameter by adding a dark border
	 * and shadow to the given free type font loader parameter.
	 * 
	 * @param parameter the font parameter to manipulate
	 */
	private void addFontDecoration(FreeTypeFontLoaderParameter parameter) {
		parameter.fontParameters.borderColor = new Color(0, 0, 0, 0.5f);
		parameter.fontParameters.borderWidth = 2;
		parameter.fontParameters.shadowColor = new Color(0, 0, 0, 0.5f);
		parameter.fontParameters.shadowOffsetX = 3;
		parameter.fontParameters.shadowOffsetY = 5;
	}

	/**
	 * Adds the game colors to the global colors map.
	 * 
	 * Adds the game's colors to the given skin by adding the
	 * green, red, blue, dark and light skin colors to the static
	 * colors map in class Colors.
	 * 
	 * @param skin the skin to get the game colors from
	 */
	private void addColors(Skin skin) {
		Colors.put("TTT_GREEN", skin.getColor("green"));
		Colors.put("TTT_RED", skin.getColor("red"));
		Colors.put("TTT_BLUE", skin.getColor("blue"));
		Colors.put("TTT_DARK", skin.getColor("dark"));
		Colors.put("TTT_LIGHT", skin.getColor("light"));
	}

	/**
	 * Resizes this loading screen.
	 * 
	 * Resizes this loading screen by calling the screen's game's
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
	 * Disposes this loading screen.
	 * 
	 * Disposes this loading screen by disposing this scren's
	 * sprite batch.
	 */
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
		batch.dispose();
	}

}
