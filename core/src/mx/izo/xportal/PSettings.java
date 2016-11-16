package mx.izo.xportal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by isain on 10/11/2016.
 */
public class PSettings implements Screen {
    // Referencia al objeto de tipo Game (tiene setScreen para cambiar de pantalla)
    private Plataforma plataforma;

    // La cámara y vista principal
    private OrthographicCamera camara;
    private Viewport vista;

    // Objeto para dibujar en la pantalla
    private SpriteBatch batch;

    // Fondo
    private Texture texturaAcercaDe;

    // Opciones
    private Texture texturaRegresar;
    private Boton btnRegresar;

    //Resetear niveles
    private Texture texturaBtnReset;
    private Boton btnReset;

    //Botones generales de audio
    private Texture texturaSonidoT;
    private Texture texturaMusicaT;
    private Texture texturaSonidoF;
    private Texture texturaMusicaF;
    private Boton btnSonidoT;
    private Boton btnMusicaT;
    private Boton btnSonidoF;
    private Boton btnMusicaF;


    //PREFERENCIAS
    public Preferences niveles = Gdx.app.getPreferences("Niveles");
    public Preferences sonidos = Gdx.app.getPreferences("Sonidos");
    public Preferences musica = Gdx.app.getPreferences("Musica");

    private boolean estadoMusica = musica.getBoolean("estadoMusica");
    private boolean estadoSonidos = sonidos.getBoolean("estadoSonidos");

    //Musica de fondo
    private Music musicFondo;


    public PSettings(Plataforma plataforma) {
        this.plataforma = plataforma;
    }

    /*
    Se ejecuta al mostrar este Screen como pantalla de la app
     */
    @Override
    public void show() {
        // Crea la cámara/vista
        camara = new OrthographicCamera(Plataforma.ANCHO_CAMARA, Plataforma.ALTO_CAMARA);
        camara.position.set(Plataforma.ANCHO_CAMARA / 2, Plataforma.ALTO_CAMARA / 2, 0);
        camara.update();
        vista = new StretchViewport(Plataforma.ANCHO_CAMARA, Plataforma.ALTO_CAMARA, camara);

        batch = new SpriteBatch();

        cargarRecursos();
        crearObjetos();

        Gdx.input.setInputProcessor(new ProcesadorEntrada());

    }

    // Carga los recursos a través del administrador de assets
    private void cargarRecursos() {
        // Cargar las texturas/mapas
        AssetManager assetManager = plataforma.getAssetManager();   // Referencia al assetManager

        assetManager.load("SettingsDef.png", Texture.class);    // Cargar imagen
        assetManager.load("back.png", Texture.class);


        // Texturas de los botones de sonido
        assetManager.load("BtmSonido.png", Texture.class);
        assetManager.load("BtmMusic.png", Texture.class);
        assetManager.load("BtmSonidoF.png", Texture.class);
        assetManager.load("BtmMusicF.png", Texture.class);

        //Textura boton reset
        assetManager.load("reset.png",Texture.class);

        // Se bloquea hasta que cargue todos los recursos
        assetManager.finishLoading();
    }

    private void crearObjetos() {
        AssetManager assetManager = plataforma.getAssetManager();   // Referencia al assetManager
        // Carga el mapa en memoria
        texturaAcercaDe = assetManager.get("SettingsDef.png");
        texturaRegresar = assetManager.get("back.png");

        texturaSonidoT = assetManager.get("BtmSonido.png");
        texturaMusicaT = assetManager.get("BtmMusic.png");
        texturaSonidoF = assetManager.get("BtmSonidoF.png");
        texturaMusicaF = assetManager.get("BtmMusicF.png");

        texturaBtnReset = assetManager.get("reset.png");

        //musica de fondo
        musicFondo = Gdx.audio.newMusic(Gdx.files.internal("little-forest.mp3"));
        musicFondo.setLooping(true);
        if(estadoMusica) {
            musicFondo.play();
        }
        else{
            musicFondo.stop();
        }


        //crear botones
        btnRegresar = new Boton(texturaRegresar);
        btnRegresar.setPosicion(20,10);

        if(estadoMusica) {
            btnMusicaT = new Boton(texturaMusicaT);
            btnMusicaT.setPosicion(Plataforma.ANCHO_CAMARA / 2 + 150, Plataforma.ALTO_CAMARA / 2 - 70);
            btnMusicaT.setAlfa(0.7f);

            btnMusicaF = new Boton(texturaMusicaF);
            btnMusicaF.setPosicion(Plataforma.ANCHO_CAMARA / 2 + 250, Plataforma.ALTO_CAMARA / 2 - 70);//250
            btnMusicaF.setAlfa(0.7f);
        }
        else {
            btnMusicaT = new Boton(texturaMusicaT);
            btnMusicaT.setPosicion(Plataforma.ANCHO_CAMARA / 2 + 250, Plataforma.ALTO_CAMARA / 2 - 70);
            btnMusicaT.setAlfa(0.7f);

            btnMusicaF = new Boton(texturaMusicaF);
            btnMusicaF.setPosicion(Plataforma.ANCHO_CAMARA / 2 + 150, Plataforma.ALTO_CAMARA / 2 - 70);//250
            btnMusicaF.setAlfa(0.7f);
        }
        if (estadoSonidos) {
            btnSonidoT = new Boton(texturaSonidoT);
            btnSonidoT.setPosicion(Plataforma.ANCHO_CAMARA / 2 - 250, Plataforma.ALTO_CAMARA / 2 - 70);
            btnSonidoT.setAlfa(0.7f);

            btnSonidoF = new Boton(texturaSonidoF);
            btnSonidoF.setPosicion(Plataforma.ANCHO_CAMARA / 2 - 350, Plataforma.ALTO_CAMARA / 2 - 70);
            btnSonidoF.setAlfa(0.7f);
        } else{
            btnSonidoT = new Boton(texturaSonidoT);
            btnSonidoT.setPosicion(Plataforma.ANCHO_CAMARA / 2 - 350, Plataforma.ALTO_CAMARA / 2 - 70);
            btnSonidoT.setAlfa(0.7f);

            btnSonidoF = new Boton(texturaSonidoF);
            btnSonidoF.setPosicion(Plataforma.ANCHO_CAMARA / 2 - 250, Plataforma.ALTO_CAMARA / 2 - 70);
            btnSonidoF.setAlfa(0.7f);
        }
        btnReset = new Boton(texturaBtnReset);
        btnReset.setPosicion(Plataforma.ANCHO_CAMARA-145,10);
        btnReset.setAlfa(0.7f);
    }

    /*
    Dibuja TODOS los elementos del juego en la pantalla.
    Este método se está ejecutando muchas veces por segundo.
     */
    @Override
    public void render(float delta) { // delta es el tiempo entre frames (Gdx.graphics.getDeltaTime())

        // Dibujar
        borrarPantalla();

        batch.setProjectionMatrix(camara.combined);

        // Entre begin-end dibujamos nuestros objetos en pantalla
        batch.begin();


        batch.draw(texturaAcercaDe, 0, 0);

        //DibujarBotones
        btnRegresar.render(batch);
        btnReset.render(batch);
        if(estadoMusica) {
            btnMusicaT.setPosicion(Plataforma.ANCHO_CAMARA / 2 + 150, Plataforma.ALTO_CAMARA / 2 - 70);
            btnMusicaT.setAlfa(0.9f);
            btnMusicaT.render(batch);
            btnMusicaF.setPosicion(Plataforma.ANCHO_CAMARA / 2 + 250, Plataforma.ALTO_CAMARA / 2 - 70);
            btnMusicaF.setAlfa(0.9f);

        }else {
            btnMusicaT.setPosicion(Plataforma.ANCHO_CAMARA / 2 + 250, Plataforma.ALTO_CAMARA / 2 - 70);
            btnMusicaT.setAlfa(0.9f);
            btnMusicaF.setPosicion(Plataforma.ANCHO_CAMARA / 2 + 150, Plataforma.ALTO_CAMARA / 2 - 70);
            btnMusicaF.render(batch);
            btnMusicaF.setAlfa(0.9f);
        }
        if(estadoSonidos) {
            btnSonidoT.setPosicion(Plataforma.ANCHO_CAMARA / 2 - 250, Plataforma.ALTO_CAMARA / 2 - 70);
            btnSonidoT.setAlfa(0.9f);
            btnSonidoT.render(batch);
            btnSonidoF.setPosicion(Plataforma.ANCHO_CAMARA / 2 - 350, Plataforma.ALTO_CAMARA / 2 - 70);
            btnSonidoF.setAlfa(0.9f);
        }else{
            btnSonidoT.setPosicion(Plataforma.ANCHO_CAMARA / 2 - 350, Plataforma.ALTO_CAMARA / 2 - 70);
            btnSonidoT.setAlfa(0.9f);
            btnSonidoF.setPosicion(Plataforma.ANCHO_CAMARA / 2 - 250, Plataforma.ALTO_CAMARA / 2 - 70);
            btnSonidoF.render(batch);
            btnSonidoF.setAlfa(0.9f);
        }


        batch.end();
    }

    private void borrarPantalla() {
        Gdx.gl.glClearColor(0.42f, 0.55f, 1, 1);    // r, g, b, alpha
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        vista.update(width,height);
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

    // Libera los assets
    @Override
    public void dispose() {
        // Los assets se liberan a través del assetManager
        AssetManager assetManager = plataforma.getAssetManager();
        assetManager.unload("SettingsDef.png");
        assetManager.unload("back.png");
    }

    /*
    Clase utilizada para manejar los eventos de touch en la pantalla
     */
    public class ProcesadorEntrada extends InputAdapter
    {
        private Vector3 coordenadas = new Vector3();
        private float x, y;     // Las coordenadas en la pantalla

        /*
        Se ejecuta cuando el usuario PONE un dedo sobre la pantalla, los dos primeros parámetros
        son las coordenadas relativas a la pantalla física (0,0) en la esquina superior izquierda
        pointer - es el número de dedo que se pone en la pantalla, el primero es 0
        button - el botón del mouse
         */
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);

//            explosion.setPosition(x,y);
//            explosion.reset();
            return true;    // Indica que ya procesó el evento
        }

        /*
        Se ejecuta cuando el usuario QUITA el dedo de la pantalla.
         */
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);

            if (btnRegresar.contiene(x,y)){
                musicFondo.dispose();
                plataforma.setScreen(new Menu(plataforma));
            }else if(btnSonidoT.contiene(x,y)){
                estadoSonidos = false;
                sonidos.clear();
                sonidos.putBoolean("estadoSonidos",false);
                sonidos.flush();
            }
            else if(btnSonidoF.contiene(x,y)){
                estadoSonidos = true;
                sonidos.clear();
                sonidos.putBoolean("estadoSonidos",true);
                sonidos.flush();
            }else if(btnMusicaT.contiene(x,y)){
                estadoMusica=false;
                musica.clear();
                musica.putBoolean("estadoMusica",false);
                musica.flush();
                musicFondo.pause();
            } else if(btnMusicaF.contiene(x,y)){
                estadoMusica = true;
                musica.clear();
                musica.putBoolean("estadoMusica",true);
                musica.flush();
                musicFondo.play();
            }else if (btnReset.contiene(x,y)){
                niveles.clear();
                niveles.flush();
                musicFondo.dispose();
                plataforma.setScreen(new Menu(plataforma));
            }
            return true;    // Indica que ya procesó el evento
        }


        // Se ejecuta cuando el usuario MUEVE el dedo sobre la pantalla
        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            transformarCoordenadas(screenX, screenY);

            return true;
        }


        private void transformarCoordenadas(int screenX, int screenY) {
            // Transformar las coordenadas de la pantalla física a la cámara HUD
            coordenadas.set(screenX, screenY, 0);
            camara.unproject(coordenadas);
            // Obtiene las coordenadas relativas a la pantalla virtual
            x = coordenadas.x;
            y = coordenadas.y;
        }
    }
}
