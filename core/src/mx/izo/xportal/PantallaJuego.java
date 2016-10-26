package mx.izo.xportal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.TimerTask;


/**
 * Created by Equipo alfa buena maravilla onda dinamita escuadrón lobo on 10/11/2016.
 */

public class PantallaJuego implements Screen
{
    public static final float ANCHO_MAPA = 4000;   // Ancho del mapa en pixeles
    public static final float ALTO_MAPA = 896;

    // Referencia al objeto de tipo Game (tiene setScreen para cambiar de pantalla)
    private Plataforma plataforma;

    // La cámara y vista principal
    private OrthographicCamera camara;
    private Viewport vista;


    // Objeto para dibujar en la pantalla
    private SpriteBatch batch;

    // MAPA
    private TiledMap mapa;      // Información del mapa en memoria
    private OrthogonalTiledMapRenderer rendererMapa;    // Objeto para dibujar el mapa

    // Personaje
    private Texture texturaPersonaje;       // Aquí cargamos la imagen marioSprite.png con varios frames
    private Personaje mario;
    public static final int TAM_CELDA = 32;



    // HUD. Los componentes en la pantalla que no se mueven
    private OrthographicCamera camaraHUD;   // Cámara fija
    // Botones izquierda/derecha
    private Texture texturaBtnIzquierda;
    private Boton btnIzquierda;
    private Texture texturaBtnDerecha;
    private Boton btnDerecha;
    // Botón saltar
    private Texture texturaSalto;
    private Boton btnSalto;

    //Boton disparar
    private Texture texturaDisparo;
    private Boton btnDisparo;

    // Estrellas recolectadas
    private int estrellas;
    private int vidaf;
    private Texto texto;
    private Sound sonidoEstrella;

    // Fin del juego, Gana o Pierde
    private Texture texturaGana;
    private Boton btnGana;
    private Sound sonidoPierde;

    private  Texture texturaBala;
    private  Texture texturaEnemigo;

    // Estados del juego
    private EstadosJuego estadoJuego;

    ArrayList<Bala> balas = new ArrayList<Bala>();
    ArrayList<Enemigo> enemigos = new ArrayList<Enemigo>();



    public PantallaJuego(Plataforma plataforma) {
        this.plataforma = plataforma;
    }

    /*
    Se ejecuta al mostrar este Screen como pantalla de la app
     */
    @Override
    public void show() {
        // Crea la cámara/vista
        camara = new OrthographicCamera(Plataforma.ANCHO_CAMARA,Plataforma.ALTO_CAMARA);
        camara.position.set(Plataforma.ANCHO_CAMARA / 2, Plataforma.ALTO_CAMARA / 2, 0);
        camara.update();
        vista = new StretchViewport(Plataforma.ANCHO_CAMARA, Plataforma.ALTO_CAMARA, camara);

        batch = new SpriteBatch();

        // Cámara para HUD
        camaraHUD = new OrthographicCamera(Plataforma.ANCHO_CAMARA,Plataforma.ALTO_CAMARA);
        camaraHUD.position.set(Plataforma.ANCHO_CAMARA / 2, Plataforma.ALTO_CAMARA / 2, 0);
        camaraHUD.update();

        //cargarRecursos();
        crearObjetos();

        // Indicar el objeto que atiende los eventos de touch (entrada en general)
        Gdx.input.setInputProcessor(new ProcesadorEntrada());

        estadoJuego = EstadosJuego.JUGANDO;

        // Texto
        texto = new Texto();
    }



    // LOS RECURSOS SE CARGAN AHORA EN PantallaCargando
    /*
    // Carga los recursos a través del administrador de assets
    private void cargarRecursos() {
        // Cargar las texturas/mapas
        AssetManager assetManager = plataforma.getAssetManager();   // Referencia al assetManager
        assetManager.load("Mapa.tmx", TiledMap.class);  // Cargar info del mapa
        assetManager.load("marioSprite.png", Texture.class);    // Cargar imagen
        // Texturas de los botones
        assetManager.load("derecha.png", Texture.class);
        assetManager.load("izquierda.png", Texture.class);
        assetManager.load("salto.png", Texture.class);
        // Fin del juego
        assetManager.load("ganaste.png", Texture.class);

        // Se bloquea hasta que cargue todos los recursos
        assetManager.finishLoading();
    }
    */

    private void crearObjetos() {
        AssetManager assetManager = plataforma.getAssetManager();   // Referencia al assetManager
        // Carga el mapa en memoria
        mapa = assetManager.get("Mapa.tmx");
        //mapa.getLayers().get(0).setVisible(false);    // Pueden ocultar una capa así
        // Crear el objeto que dibujará el mapa

        rendererMapa = new OrthogonalTiledMapRenderer(mapa,batch);
        rendererMapa.setView(camara);
        // Cargar frames
        texturaPersonaje = assetManager.get("marioSprite.png");
        texturaSalto = assetManager.get("salto.png");
        // Crear el personaje
        mario = new Personaje(texturaPersonaje,texturaSalto);
        // Posición inicial del personaje
        mario.getSprite().setPosition(Plataforma.ANCHO_CAMARA / 10, Plataforma.ALTO_CAMARA * 0.90f);

        // Crear los botones
        texturaBtnIzquierda = assetManager.get("BtmIzquierdo.png");
        btnIzquierda = new Boton(texturaBtnIzquierda);
        btnIzquierda.setPosicion(TAM_CELDA,TAM_CELDA );
        btnIzquierda.setAlfa(0.7f); // Un poco de transparencia
        texturaBtnDerecha = assetManager.get("BtmDerecho.png");
        btnDerecha = new Boton(texturaBtnDerecha);
        btnDerecha.setPosicion(6 * TAM_CELDA,  TAM_CELDA);
        btnDerecha.setAlfa(0.7f); // Un poco de transparencia
        texturaSalto = assetManager.get("BtmSaltar.png");
        btnSalto = new Boton(texturaSalto);
        btnSalto.setPosicion(Plataforma.ANCHO_CAMARA - 4.5f * TAM_CELDA,  TAM_CELDA);
        btnSalto.setAlfa(0.7f);

        texturaDisparo = assetManager.get("shoot.jpg");
        btnDisparo = new Boton(texturaDisparo);
        btnDisparo.setPosicion(Plataforma.ANCHO_CAMARA - 4.5f * TAM_CELDA,  TAM_CELDA+110);
        btnDisparo.setAlfa(0.7f);
        // Gana
        texturaGana = assetManager.get("ganaste.png");
        btnGana = new Boton(texturaGana);
        btnGana.setPosicion(Plataforma.ANCHO_CAMARA/2-btnGana.getRectColision().width/2,Plataforma.ALTO_CAMARA/2-btnGana.getRectColision().height/2);
        btnGana.setAlfa(0.7f);


        texturaBala = assetManager.get("bullet.png");
        texturaEnemigo = assetManager.get("Planta.png");

        Enemigo enemigo1 = new Enemigo(texturaEnemigo);
        enemigo1.setPosicion(2000,74);
        Enemigo enemigo2 = new Enemigo(texturaEnemigo);
        enemigo2.setPosicion(500,20);
        Enemigo enemigo3 = new Enemigo(texturaEnemigo);
        enemigo3.setPosicion(1000,94);
        enemigos.add(enemigo1);
        enemigos.add(enemigo2);
        enemigos.add(enemigo3);

        // Efecto moneda
        sonidoEstrella = assetManager.get("coin.wav");
        sonidoPierde = assetManager.get("mariodie.wav");
    }

    /*
    Dibuja TODOS los elementos del juego en la pantalla.
    Este método se está ejecutando muchas veces por segundo.
     */


    @Override
    public void render(float delta) { // delta es el tiempo entre frames (Gdx.graphics.getDeltaTime())

        if (estadoJuego!=EstadosJuego.PERDIO) {
            // Actualizar objetos en la pantalla
            moverPersonaje();
            actualizarCamara(); // Mover la cámara para que siga al personaje
        }

        // Dibujar
        borrarPantalla();

        batch.setProjectionMatrix(camara.combined);

        rendererMapa.setView(camara);
        rendererMapa.render();  // Dibuja el mapa

        // Entre begin-end dibujamos nuestros objetos en pantalla
        batch.begin();

        mario.render(batch);    // Dibuja el personaje


        for(Enemigo enemigo:enemigos){
            if (enemigo.getVidas()>0){
                enemigo.render(batch,texturaBala);
                for(Bala bala : balas){
                    bala.render(batch);
                    if((bala.getX() >= enemigo.getX() && bala.getX()<= (enemigo.getX()+enemigo.getSprite().getWidth()))&&
                        (bala.getY() >= enemigo.getY() && bala.getY()<= (enemigo.getY()+enemigo.getSprite().getHeight()))) {
                        int vidas = enemigo.getVidas();
                        enemigo.setVidas(vidas-1);
                        bala.velocidadX = 10;
                        bala.setPosicion(0, 3000);
                    }
                }

            }
            else{
                enemigos.remove(enemigo);
            }
        }


        //Dibuja las balas del personaje
        for(Bala bala : balas){
            bala.render(batch);
        }

        batch.end();

        // Dibuja el HUD
        batch.setProjectionMatrix(camaraHUD.combined);
        batch.begin();

        // ¿Ya ganó?
        if (estadoJuego==EstadosJuego.GANO) {
            btnGana.render(batch);
        } else {
            btnIzquierda.render(batch);
            btnDerecha.render(batch);
            btnSalto.render(batch);
            btnDisparo.render(batch);
            // Estrellas recolectadas
            texto.mostrarMensaje(batch,"Score: "+estrellas,Plataforma.ANCHO_CAMARA-1000,Plataforma.ALTO_CAMARA*0.95f);
            texto.mostrarMensaje(batch,"Vidas: "+vidaf,Plataforma.ANCHO_CAMARA-500,Plataforma.ALTO_CAMARA*0.95f);
        }
        batch.end();
    }

    // Actualiza la posición de la cámara para que el personaje esté en el centro,
    // excepto cuando está en la primera y última parte del mundo
    private void actualizarCamara() {
        float posX = mario.getX();
        float posY = mario.getY();
        // Si está en la parte 'media'
        if (posX>=Plataforma.ANCHO_CAMARA/2 && posX<=ANCHO_MAPA-Plataforma.ANCHO_CAMARA/2) {
            // El personaje define el centro de la cámara
            camara.position.set((int)posX, camara.position.y, 0);
        } else if (posX>ANCHO_MAPA-Plataforma.ANCHO_CAMARA/2) {    // Si está en la última mitad
            // La cámara se queda a media pantalla antes del fin del mundo  :)
            camara.position.set(ANCHO_MAPA-Plataforma.ANCHO_CAMARA/2, camara.position.y, 0);
        }//Si el personaje se coloca en el centro de la camara

         if((posY>=Plataforma.ALTO_CAMARA/2 && posY<=ALTO_MAPA-Plataforma.ALTO_CAMARA/2)) {
            // El personaje define el centro de la cámara
            camara.position.set(camara.position.x,(int)posY, 0);
        } else if ((posY>ALTO_MAPA-Plataforma.ALTO_CAMARA/2)) {    // Si está en la última mitad
            // La cámara se queda a media pantalla antes del fin del mundo  :)
            camara.position.set(camara.position.x, ALTO_MAPA - Plataforma.ALTO_CAMARA / 2, 0);
        }
        camara.update();
    }

    /*
    Movimiento del personaje. SIMPLIFICAR LOGICA :(
     */
    private void moverPersonaje() {
        // Prueba caída libre inicial o movimiento horizontal
        switch (mario.getEstadoMovimiento()) {
            case INICIANDO:     // Mueve el personaje en Y hasta que se encuentre sobre un bloque
                // Los bloques en el mapa son de 16x16
                // Calcula la celda donde estaría después de moverlo
                int celdaX = (int)(mario.getX()/ TAM_CELDA);
                int celdaY = (int)((mario.getY()+mario.VELOCIDAD_Y)/ TAM_CELDA);
                // Recuperamos la celda en esta posición
                // La capa 0 es el fondo
                TiledMapTileLayer capa = (TiledMapTileLayer)mapa.getLayers().get(1);
                TiledMapTileLayer.Cell celda = capa.getCell(celdaX, celdaY);
                // probar si la celda está ocupada
                if (celda==null) {
                    // Celda vacía, entonces el personaje puede avanzar
                    mario.caer();
                }  else if ( !esCoin(celda) ) {  // Las estrellas no lo detienen :)
                    // Dejarlo sobre la celda que lo detiene
                    mario.setPosicion(mario.getX(), (celdaY + 1) * TAM_CELDA);
                    mario.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
                }
                break;
            case MOV_DERECHA:       // Se mueve horizontal
            case MOV_IZQUIERDA:
                probarChoqueParedes();      // Prueba si debe moverse
                break;
        }

        // Prueba si debe caer por llegar a un espacio vacío
        if ( mario.getEstadoMovimiento()!= Personaje.EstadoMovimiento.INICIANDO
                && (mario.getEstadoSalto() != Personaje.EstadoSalto.SUBIENDO) ) {
            // Calcula la celda donde estaría después de moverlo
            int celdaX = (int) (mario.getX() / TAM_CELDA);
            int celdaY = (int) ((mario.getY() + mario.VELOCIDAD_Y) / TAM_CELDA);
            // Recuperamos la celda en esta posición
            // La capa 0 es el fondo
            TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(1);
            TiledMapTileLayer.Cell celdaAbajo = capa.getCell(celdaX, celdaY);
            TiledMapTileLayer.Cell celdaDerecha = capa.getCell(celdaX+1, celdaY);
            // probar si la celda está ocupada
            if ( (celdaAbajo==null && celdaDerecha==null) || esCoin(celdaAbajo) || esCoin(celdaDerecha) ) {
                // Celda vacía, entonces el personaje puede avanzar
                mario.caer();
                mario.setEstadoSalto(Personaje.EstadoSalto.CAIDA_LIBRE);
            } else {
                // Dejarlo sobre la celda que lo detiene
                mario.setPosicion(mario.getX(), (celdaY + 1) * TAM_CELDA);
                mario.setEstadoSalto(Personaje.EstadoSalto.EN_PISO);

                /*if ( esMoneda(celdaAbajo) || esMoneda(celdaDerecha)) {
                    // La encontró!!!!
                    estadoJuego = EstadosJuego.GANO;
                    btnIzquierda.setAlfa(0.2f);
                    btnDerecha.setAlfa(0.2f);
                    btnSalto.setAlfa(0.2f);
                }*/
            }
        }

        // Saltar
        switch (mario.getEstadoSalto()) {
            case SUBIENDO:
            case BAJANDO:
                mario.actualizarSalto(mapa);    // Actualizar posición en 'y'
                break;
        }
    }

    // Prueba si puede moverse a la izquierda o derecha
    private void probarChoqueParedes() {
        Personaje.EstadoMovimiento estado = mario.getEstadoMovimiento();
        // Quitar porque este método sólo se llama cuando se está moviendo
        if ( estado!= Personaje.EstadoMovimiento.MOV_DERECHA && estado!=Personaje.EstadoMovimiento.MOV_IZQUIERDA){
            return;
        }
        float px = mario.getX();    // Posición actual
        // Posición después de actualizar
        px = mario.getEstadoMovimiento()==Personaje.EstadoMovimiento.MOV_DERECHA? px+Personaje.VELOCIDAD_X:
                px-Personaje.VELOCIDAD_X;
        int celdaX = (int)(px/TAM_CELDA);   // Casilla del personaje en X
        if (mario.getEstadoMovimiento()== Personaje.EstadoMovimiento.MOV_DERECHA) {
            celdaX++;   // Casilla del lado derecho
        }
        int celdaY = (int)(mario.getY()/TAM_CELDA); // Casilla del personaje en Y
        TiledMapTileLayer capaPlataforma = (TiledMapTileLayer) mapa.getLayers().get(1);
        TiledMapTileLayer capaPlataforma1 = (TiledMapTileLayer) mapa.getLayers().get(4);
        TiledMapTileLayer capaPlataforma2 = (TiledMapTileLayer) mapa.getLayers().get(6);
        //******************************
        if ( capaPlataforma.getCell(celdaX,celdaY) != null || capaPlataforma.getCell(celdaX,celdaY+1) != null ) {
            // Colisionará, dejamos de moverlo
            if ( esCoin(capaPlataforma.getCell(celdaX,celdaY)) ) {
                // Borrar esta estrella y contabilizar
                capaPlataforma.setCell(celdaX,celdaY,null);
                estrellas++;
                sonidoEstrella.play();
            } else if (esCoin(capaPlataforma.getCell(celdaX,celdaY+1)) ) {
                // Borrar esta estrella y contabilizar
                capaPlataforma.setCell(celdaX,celdaY+1,null);
                estrellas++;
                sonidoEstrella.play();
            } else if ( esPuertaA( capaPlataforma1.getCell(celdaX,celdaY) ) ) {
                sonidoPierde.play();
                estadoJuego = EstadosJuego.PERDIO;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        plataforma.setScreen(new Menu(plataforma));
                    }
                }, 3);  // 3 segundos
            } else if ( esPuertaA2( capaPlataforma2.getCell(celdaX,celdaY) ) ) {
                sonidoPierde.play();
                estadoJuego = EstadosJuego.PERDIO;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        plataforma.setScreen(new Menu(plataforma));
                    }
                }, 3);
            } else if (esLlave1(capaPlataforma.getCell(celdaX,celdaY))){
                eliminarLlave1();
                estrellas++;
                abrirPuerta1();
                sonidoEstrella.play();

            }else if (esLlave2(capaPlataforma.getCell(celdaX,celdaY))){
                eliminarLlave2();
                estrellas++;
                abrirPuerta2();
                sonidoEstrella.play();

            }else if(esVida(capaPlataforma.getCell(celdaX,celdaY))){
                capaPlataforma.setCell(celdaX,celdaY+1,null);
                vidaf++;
                sonidoEstrella.play();
            }else {
                mario.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
            }
        }

        if ( capaPlataforma1.getCell(celdaX,celdaY) != null || capaPlataforma1.getCell(celdaX,celdaY+1) != null ) {
            if ( esPuertaA( capaPlataforma1.getCell(celdaX,celdaY) ) ) {
                sonidoPierde.play();
                estadoJuego = EstadosJuego.PERDIO;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        plataforma.setScreen(new Menu(plataforma));
                    }
                }, 3);  // 3 segundos
            }
        }
        if ( capaPlataforma2.getCell(celdaX,celdaY) != null || capaPlataforma2.getCell(celdaX,celdaY+1) != null ) {
            if ( esPuertaA2( capaPlataforma2.getCell(celdaX,celdaY) ) ) {
                sonidoPierde.play();
                estadoJuego = EstadosJuego.PERDIO;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        plataforma.setScreen(new Menu(plataforma));
                    }
                }, 3);  // 3 segundos
            }
        }
        else {
            mario.actualizar();
        }
    }

    private void abrirPuerta1() {
        TiledMapTileLayer capaPlataforma = (TiledMapTileLayer) mapa.getLayers().get(3);
        capaPlataforma.setVisible(false);
        capaPlataforma = (TiledMapTileLayer) mapa.getLayers().get(4);
        capaPlataforma.setVisible(true);
    }

    private void abrirPuerta2() {
        TiledMapTileLayer capaPlataforma = (TiledMapTileLayer) mapa.getLayers().get(5);
        capaPlataforma.setVisible(false);
        capaPlataforma = (TiledMapTileLayer) mapa.getLayers().get(6);
        capaPlataforma.setVisible(true);
    }

    private void eliminarLlave2() {
        TiledMapTileLayer capaPlataforma = (TiledMapTileLayer) mapa.getLayers().get(1);
        capaPlataforma.setCell(68,23,null);
        capaPlataforma.setCell(68,24,null);
        capaPlataforma.setCell(68,25,null);
        capaPlataforma.setCell(68,26,null);
        capaPlataforma.setCell(68,27,null);
        capaPlataforma.setCell(69,23,null);
        capaPlataforma.setCell(69,24,null);
        capaPlataforma.setCell(69,25,null);
        capaPlataforma.setCell(69,26,null);
        capaPlataforma.setCell(69,27,null);
        capaPlataforma.setCell(70,23,null);
        capaPlataforma.setCell(70,24,null);
        capaPlataforma.setCell(70,25,null);
        capaPlataforma.setCell(70,26,null);
        capaPlataforma.setCell(70,27,null);

    }

    private void eliminarLlave1() {
        TiledMapTileLayer capaPlataforma = (TiledMapTileLayer) mapa.getLayers().get(1);
        capaPlataforma.setCell(38,22,null);
        capaPlataforma.setCell(38,21,null);
        capaPlataforma.setCell(38,20,null);
        capaPlataforma.setCell(38,19,null);
        capaPlataforma.setCell(38,18,null);
        capaPlataforma.setCell(39,22,null);
        capaPlataforma.setCell(39,21,null);
        capaPlataforma.setCell(39,20,null);
        capaPlataforma.setCell(39,19,null);
        capaPlataforma.setCell(39,18,null);
        capaPlataforma.setCell(40,22,null);
        capaPlataforma.setCell(40,21,null);
        capaPlataforma.setCell(40,20,null);
        capaPlataforma.setCell(40,19,null);
        capaPlataforma.setCell(40,18,null);
    }



    // Verifica si esta casilla tiene una estrella (simplificar con la anterior)
    private boolean esCoin(TiledMapTileLayer.Cell celda) {
        if (celda==null) {
            return false;
        }
        Object propiedad = celda.getTile().getProperties().get("tipo");
        return "coin".equals(propiedad);
    }

    private boolean esVida(TiledMapTileLayer.Cell celda){
        if(celda==null)
            return false;
        Object propiedad =celda.getTile().getProperties().get("tipo");
        return "pildora".equals(propiedad);
    }
    // Verifica si esta casilla tiene una llave (simplificar con la anterior)
    private boolean esLlave1(TiledMapTileLayer.Cell celda) {
        if (celda==null) {
            return false;
        }
        Object propiedad = celda.getTile().getProperties().get("tipo");
        return "llave1".equals(propiedad);
    }

    private boolean esLlave2(TiledMapTileLayer.Cell celda) {
        if (celda==null) {
            return false;
        }
        Object propiedad = celda.getTile().getProperties().get("tipo");
        return "llave2".equals(propiedad);
    }

    // Verifica si esta casilla tiene un hongo (simplificar con las anteriores)
    private boolean esPuertaA(TiledMapTileLayer.Cell celda) {
        if (celda==null) {
            return false;
        }
        Object propiedad = celda.getTile().getProperties().get("tipo");
        Gdx.app.log("es PuertaA",propiedad.toString());
        return "puertaA".equals(propiedad);
    }

    private boolean esPuertaA2(TiledMapTileLayer.Cell celda){
        if (celda==null) {
            return false;
        }
        Object propiedad =celda.getTile().getProperties().get("tipo");
        Gdx. app.log("es Puerta A2",propiedad.toString());
        return "puertaA2".equals(propiedad);

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
        assetManager.unload("marioSprite.png");
        assetManager.unload("BtmDerecho.png");
        assetManager.unload("BtmIzquierdo.png");
        assetManager.unload("BtmSaltar.png");
        assetManager.unload("ganaste.png");
        assetManager.unload("Mapa.tmx");
        assetManager.unload("shoot.jpg");
        assetManager.unload("pil.png");
        assetManager.unload("bullet.png");
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
            if (estadoJuego==EstadosJuego.JUGANDO) {
                // Preguntar si las coordenadas están sobre el botón derecho
                if (btnDerecha.contiene(x, y) && mario.getEstadoMovimiento() != Personaje.EstadoMovimiento.INICIANDO) {
                    // Tocó el botón derecha, hacer que el personaje se mueva a la derecha
                    mario.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
                } else if (btnIzquierda.contiene(x, y) && mario.getEstadoMovimiento() != Personaje.EstadoMovimiento.INICIANDO) {
                    // Tocó el botón izquierda, hacer que el personaje se mueva a la izquierda
                    mario.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_IZQUIERDA);
                } else if (btnSalto.contiene(x, y)) {
                    // Tocó el botón saltar
                    mario.saltar();
                }else if (btnDisparo.contiene(x, y)) {
                    // Tocó el botón disparar
                    Bala bala = new Bala(texturaBala);
                    bala.setPosicion(mario.getX(),mario.getY()+30);
                    //bala.setDireccion(-10);
                    balas.add(bala);
                }
            } else if (estadoJuego==EstadosJuego.GANO) {
                if (btnGana.contiene(x,y)) {
                    Gdx.app.exit();//Buuu
                }
            }
            return true;    // Indica que ya procesó el evento
        }

        /*
        Se ejecuta cuando el usuario QUITA el dedo de la pantalla.
         */
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);
            // Preguntar si las coordenadas son de algún botón para DETENER el movimiento
            if ( mario.getEstadoMovimiento()!= Personaje.EstadoMovimiento.INICIANDO && (btnDerecha.contiene(x, y) || btnIzquierda.contiene(x,y)) ) {
                // Tocó el botón derecha, hacer que el personaje se mueva a la derecha
                mario.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
            }
            return true;    // Indica que ya procesó el evento
        }


        // Se ejecuta cuando el usuario MUEVE el dedo sobre la pantalla
        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            transformarCoordenadas(screenX, screenY);
            // Acaba de salir de las fechas (y no es el botón de salto)
            if (x<Plataforma.ANCHO_CAMARA/2 && mario.getEstadoMovimiento()!= Personaje.EstadoMovimiento.QUIETO) {
                if (!btnIzquierda.contiene(x, y) && !btnDerecha.contiene(x, y) ) {
                    mario.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
                }
            }
            return true;
        }


        private void transformarCoordenadas(int screenX, int screenY) {
            // Transformar las coordenadas de la pantalla física a la cámara HUD
            coordenadas.set(screenX, screenY, 0);
            camaraHUD.unproject(coordenadas);
            // Obtiene las coordenadas relativas a la pantalla virtual
            x = coordenadas.x;
            y = coordenadas.y;
        }
    }


    public enum EstadosJuego {
        GANO,
        JUGANDO,
        PAUSADO,
        PERDIO
    }

}
