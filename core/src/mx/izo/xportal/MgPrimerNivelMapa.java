package mx.izo.xportal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
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

/**
 * Created by k3ll3x on 13/10/2016.
 *
 * Mapa del minigame del primer nivel tipo space invaders
 */
public class MgPrimerNivelMapa {

    //change ancho mapa
    public static final int ANCHO_MAPA = 1280;

    public static final int ANCHO_CAMARA = 1280;
    public static final int ALTO_CAMARA = 800;
    // Cámara
    private OrthographicCamera camara;
    private Viewport vista;

    // HUD. Los componentes en la pantalla que no se mueven
    private OrthographicCamera camaraHUD; // Cámara fija
    private StretchViewport vistaHUD;

    // Escena para HUD
    private Stage escena;

    // SpriteBatch sirve para administrar los trazos
    private SpriteBatch batch;
    private final Plataforma juego;  // Para regresar al menú

    //*** Para el mapa
    private TiledMap mapa;  // Información del mapa en memoria
    private OrthogonalTiledMapRenderer rendererMapa;    // Dibuja el mapa
    //***

    /* Mario animado
    private Texture texturaMario;
    private Personaje mario;
    */

    // Pad
    private Touchpad pad;

    /* Musica
    private Music musicaFondo;
    private Sound sonidoMuere;
    private boolean haMuertoMario=false;
    private Sound sonidoMoneda;
    */


    public MgPrimerNivelMapa(Plataforma juego) {  // Constructor
        this.juego = juego;
    }

    //@Override
    public void show() {
        inicializarCamara();
        crearEscena();
        cargarMapa();   // Nuevo
        crearPad();

        Gdx.gl.glClearColor(1,1,1,1);
    }

    private void crearPad() {

        // Para cargar las texturas y convertirlas en Drawable
        Skin skin = new Skin();
        skin.add("touchBackground", new Texture("touchBackground.png"));
        skin.add("touchKnob", new Texture("touchKnob.png"));

        // Carcaterísticas del pad
        Touchpad.TouchpadStyle tpEstilo = new Touchpad.TouchpadStyle();
        tpEstilo.background = skin.getDrawable("touchBackground");
        tpEstilo.knob = skin.getDrawable("touchKnob");

        // Crea el pad, revisa la clase Touchpad para entender los parámetros
        pad = new Touchpad(20,tpEstilo);
        pad.setBounds(0,0,200,200); // Posición y tamaño
        pad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (mario.getEstadoMovimiento()!= Personaje.EstadoMovimiento.INICIANDO) {
                    Touchpad p = (Touchpad) actor;
                    if (p.getKnobPercentX() > 0) {    //Derecha
                        mario.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
                    } else if (p.getKnobPercentX() < 0) { // Izquierda
                        mario.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_IZQUIERDA);
                    } else {    // Nada
                        mario.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
                    }
                }
            }
        });

        escena.addActor(pad);
        pad.setColor(1,1,1,0.4f);

        // Botón de salto (lado derecho)
        Texture texturaBtnSalto = new Texture("btnSalto.png");
        TextureRegionDrawable trdBtnSalto = new TextureRegionDrawable(new TextureRegion(texturaBtnSalto));
        ImageButton btnSalto = new ImageButton(trdBtnSalto);

        btnSalto.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mario.saltar(); // Iniciar el salto
                return true;    // Ya consumió el evento
            }
        });
        btnSalto.setPosition(ANCHO_CAMARA-128-32,32); // ancho mundo - ancho de boton - margen
        escena.addActor(btnSalto);

        Gdx.input.setInputProcessor(escena);
    }

    private void crearEscena() {
        batch = new SpriteBatch();

        escena = new Stage();
        escena.setViewport(vistaHUD);
        crearPad();
    }

    private void cargarMapa() {
        AssetManager manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load("MarioCompleto.tmx", TiledMap.class);
        manager.load("marioSprite.png", Texture.class);
        // Carga música
        manager.load("audio/marioBros.mp3", Music.class);
        manager.load("audio/muereMario.mp3", Sound.class);
        manager.load("audio/moneda.mp3", Sound.class);
        manager.finishLoading();
        mapa = manager.get("MarioCompleto.tmx");
        texturaMario = manager.get("marioSprite.png");

        // Crea el objeto que dibujará el mapa
        rendererMapa = new OrthogonalTiledMapRenderer(mapa,batch);
        rendererMapa.setView(camara);

        // Audio
        musicaFondo = manager.get("audio/marioBros.mp3");
        sonidoMuere = manager.get("audio/muereMario.mp3");
        sonidoMoneda = manager.get("audio/moneda.mp3");

        musicaFondo.setLooping(true);
        musicaFondo.play();

        // Mario
        mario = new MgPrimerNivelNave(texturaMario, sonidoMoneda);
    }

    private void inicializarCamara() {
        camara = new OrthographicCamera(ANCHO_CAMARA, ALTO_CAMARA);
        camara.position.set(ANCHO_CAMARA/2, ALTO_CAMARA /2,0);
        camara.update();
        vista = new StretchViewport(ANCHO_CAMARA, MgPrimerNivelMapa.ALTO_CAMARA,camara);

        // Cámara para HUD
        camaraHUD = new OrthographicCamera(ANCHO_CAMARA, MgPrimerNivelMapa.ALTO_CAMARA);
        camaraHUD.position.set(ANCHO_CAMARA/2, MgPrimerNivelMapa.ALTO_CAMARA /2, 0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(ANCHO_CAMARA, MgPrimerNivelMapa.ALTO_CAMARA,camaraHUD);
    }

    @Override
    public void render(float delta) {
        // actualizar cámara (para recorrer el mundo completo)
        actualizarCamara();
        // Actualización del personaje en el mapa
        mario.actualizar(mapa);
        // Borra el frame actual
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // escala la pantalla de acuerdo a la cámara y vista
        batch.setProjectionMatrix(camara.combined);
        rendererMapa.setView(camara);
        rendererMapa.render();  // Dibuja el mapa
        batch.begin();
        mario.render(batch);    // Dibuja el personaje
        batch.end();

        // Dibuja el HUD
        batch.setProjectionMatrix(camaraHUD.combined);
        escena.draw();

        // PRUEBA si mario se cae al vacío
        if (mario.getY()<0 && !haMuertoMario) {
            haMuertoMario = true;
            musicaFondo.stop();
            sonidoMuere.play();
        }
    }

    // Actualiza la posición de la cámara para que el personaje esté en el centro,
    // excepto cuando está en la primera y última parte del mundo
    private void actualizarCamara() {
        float posX = mario.getX();
        // Si está en la parte 'media'
        if (posX>=ANCHO_CAMARA/2 && posX<=ANCHO_MAPA-ANCHO_CAMARA/2) {
            // El personaje define el centro de la cámara
            camara.position.set((int)posX, camara.position.y, 0);
        } else if (posX>ANCHO_MAPA-ANCHO_CAMARA/2) {    // Si está en la última mitad
            // La cámara se queda a media pantalla antes del fin del mundo  :)
            camara.position.set(ANCHO_MAPA-ANCHO_CAMARA/2, camara.position.y, 0);
        } else if ( posX<ANCHO_CAMARA/2 ) { // La primera mitad
            camara.position.set(ANCHO_CAMARA/2, MgPrimerNivelMapa.ALTO_CAMARA /2,0);
        }
        camara.update();
    }

    @Override
    public void resize(int width, int height) {
        vista.update(width, height);
        vistaHUD.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        texturaMario.dispose();
        mapa.dispose();
    }
}