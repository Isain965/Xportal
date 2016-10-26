package mx.izo.xportal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.GL20;

public class PantallaMapaMGUno implements Screen {
    public static final int TAM_CELDA = 64;
    private Plataforma plataforma;

    //private final Juego juego;
    public int width = 1280;
    public static final float ANCHO_MAPA = 1280;
    public int height = 800;

    private OrthographicCamera camara;
    private Viewport vista;
    private SpriteBatch batch;

    private Texture texturaNave;
    private Personaje nave;

    private Texture texturaBtnIzquierda;
    private Boton btnIzquierda;
    private Texture texturaBtnDerecha;
    private Boton btnDerecha;
    private Texture texturaDisparo;
    private Boton btnDisparo;

    //************************************************
    //mapa
    private TiledMap mapa; //info del mapa en memoria
    private OrthogonalTiledMapRenderer rendererMapa; //obj para dibujar mapa
    //************************************************

    // HUD. Los componentes en la pantalla que no se mueven
    private OrthographicCamera camaraHUD; // Cámara fija
    private StretchViewport vistaHUD;

    // Escena para HUD
    private Stage escena;

    public PantallaMapaMGUno(Plataforma plataforma) {
        this.plataforma = plataforma;
    }

    /*public PantallaMapaMGUno(Juego juego){
        this.juego = juego;
    }*/

    @Override
    public void show() {
        cargarTexturas();
        inicializarCamara();
        crearEscena();
        crearObjetos();
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

    public void crearObjetos(){
        // Crear el personaje
        nave = new Personaje(texturaNave);
        //nave.getSprite().setPosition(Plataforma.ANCHO_CAMARA / 10, Plataforma.ALTO_CAMARA * 0.90f);
        nave.getSprite().setPosition(width/2,nave.getSprite().getHeight());

        // Crear los botones
        btnIzquierda = new Boton(texturaBtnIzquierda);
        btnIzquierda.setPosicion(TAM_CELDA, 5 * TAM_CELDA);
        btnIzquierda.setAlfa(0.7f); // Un poco de transparencia
        btnDerecha = new Boton(texturaBtnDerecha);
        btnDerecha.setPosicion(6 * TAM_CELDA, 5 * TAM_CELDA);
        btnDerecha.setAlfa(0.7f); // Un poco de transparencia
        btnDisparo = new Boton(texturaDisparo);
        btnDisparo.setPosicion(Plataforma.ANCHO_CAMARA - 5 * TAM_CELDA, 5 * TAM_CELDA);
        btnDisparo.setAlfa(0.7f);
    }

    private void cargarTexturas() {
        AssetManager assetManager = plataforma.getAssetManager();
        assetManager.load("nave.png", Texture.class);
        assetManager.load("BtmIzquierdo.png", Texture.class);
        assetManager.load("BtmDerecho.png", Texture.class);
        assetManager.load("BtmSaltar.png", Texture.class);
        assetManager.finishLoading();
        texturaNave = assetManager.get("nave.png");
        texturaDisparo = assetManager.get("BtmSaltar.png");
        texturaBtnDerecha = assetManager.get("BtmDerecho.png");
        texturaBtnIzquierda = assetManager.get("BtmIzquierdo.png");
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
        /*//batch.begin();
        //rendererMapa.setView(camara);
        rendererMapa.render();
        //batch.end();*/
        rendererMapa.render();
        batch.begin();

        nave.render(batch);
        btnDerecha.render(batch);
        btnIzquierda.render(batch);
        btnDerecha.render(batch);
        batch.end();
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
