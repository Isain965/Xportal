package mx.izo.xportal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

/**
 * Created by isain on 10/11/2016.
 */
public class Plataforma extends Game
{
	// Constantes públicas
	public static final float ANCHO_CAMARA = 1280;
	public static final float ALTO_CAMARA = 720;

	// Administra la carga de los assets del juego
	private final AssetManager assetManager = new AssetManager();

	@Override
	public void create() {

		// Agregamos un loader para los mapas
		assetManager.setLoader(TiledMap.class,
				new TmxMapLoader(new InternalFileHandleResolver()));
		// Pantalla inicial
		setScreen(new PrincipalPantalla(this));
	}

	// Método accesor de assetManager
	public AssetManager getAssetManager() {
		return assetManager;
	}

	@Override
	public void dispose() {
		super.dispose();
		assetManager.clear();
	}

}
