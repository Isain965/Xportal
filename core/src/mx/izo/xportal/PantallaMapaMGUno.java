package mx.izo.xportal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.GL20;

public class PantallaMapaMGUno implements Screen {

    private Plataforma plataforma;

    //private final Juego juego;
    public int width = 1280;
    public int height = 800;

    private OrthographicCamera camara;
    private Viewport vista;
    private SpriteBatch batch;

    //************************************************
    //mapa
    private TiledMap mapa; //info del mapa en memoria
    private OrthogonalTiledMapRenderer rendererMapa; //obj para dibujar mapa
    //************************************************

    public PantallaMapaMGUno(Plataforma plataforma) {
        this.plataforma = plataforma;
    }

    /*public PantallaMapaMGUno(Juego juego){
        this.juego = juego;
    }*/

    @Override
    public void show() {
        inicializarCamara();
        crearEscena();
        cargarMapa();
        //quien procesa los eventos
    }

    private void cargarMapa() {
        AssetManager manager = new AssetManager();
        //leer mapas Tmx
        manager.setLoader(TiledMap.class,new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load("inv.tmx",TiledMap.class);
        manager.finishLoading();//Bloquea hasta que carga el mapa
        mapa = manager.get("inv.tmx");

        rendererMapa = new OrthogonalTiledMapRenderer(mapa,batch);
        rendererMapa.setView(camara);
    }

    private void crearEscena() {
        batch = new SpriteBatch();
    }

    private void inicializarCamara() {
        camara = new OrthographicCamera(width,height);
        camara.position.set(width/2,height/2,0);
        camara.update();
        vista = new StretchViewport(width,height,camara);//crear vista
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camara.combined);
        //batch.begin();
        //rendererMapa.setView(camara);
        rendererMapa.render();
        //batch.end();
    }

    @Override
    public void resize(int width, int height) {
        vista.update(width, height);
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

    }
}
