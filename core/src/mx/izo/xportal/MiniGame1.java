package mx.izo.xportal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
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

import java.util.Random;

import java.util.ArrayList;


/**
 * Created by Isain 10/31/2016.
 */

public class MiniGame1 implements Screen
{
    public static final float ANCHO_MAPA = 1280;   // Ancho del mapa en pixeles
    public static final float ALTO_MAPA = 736;

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
    private Texture texturaPersonaje2;
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


    //Boton Pausa
    private Texture texturaBtnPausa;
    private Boton btnPausa;



    // Estrellas recolectadas
    private int estrellas;
    private int vidaf = 0;
    private Texto texto;
    private Sound sonidoEstrella, sonidoLlave;

    // Fin del juego, Gana o Pierde
    private Texture texturaGana;
    private Boton btnGana;
    private Sound sonidoPierde, sonidoVida;


    private float tiempoJuego=0;

    // Estados del juego
    private EstadosJuego estadoJuego;

    private Texture texturaManzanas;
    ArrayList<BalaV> balasL = new ArrayList<BalaV>();

    ArrayList<EnemigoV> enemigosV = new ArrayList<EnemigoV>();
    private boolean banderaDisparo=true;

    private Texture texturaEnemigo2;

    private boolean banderaDireccion=false;

    //TEXURAS PARA LA PAUSA
    private Texture texturaPausa;
    private Texture texturaPlay;
    private Texture texturaMenu;
    private Texture texturaSonidoT;
    private Texture texturaMusicaT;
    private Texture texturaSonidoF;
    private Texture texturaMusicaF;
    private Boton btnPantallaPausa;
    private Boton btnPlay;
    private Boton btnMenu;
    private Boton btnSonidoT;
    private Boton btnMusicaT;
    private Boton btnSonidoF;
    private Boton btnMusicaF;
    private boolean banderaPausa = false;
    //Musica de fondo
    private Music musicFondo;




    public MiniGame1(Plataforma plataforma) {
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



    private void crearObjetos() {
        AssetManager assetManager = plataforma.getAssetManager();   // Referencia al assetManager
        // Carga el mapa en memoria
        mapa = assetManager.get("MiniGameMapa.tmx");
        //mapa.getLayers().get(0).setVisible(false);    // Pueden ocultar una capa así
        // Crear el objeto que dibujará el mapa

        rendererMapa = new OrthogonalTiledMapRenderer(mapa,batch);
        rendererMapa.setView(camara);
        // Cargar frames
        texturaPersonaje = assetManager.get("marioSprite.png");
        texturaPersonaje2 = assetManager.get("marioSpriteIzq.png");

        texturaSalto = assetManager.get("salto.png");
        // Crear el personaje
        mario = new Personaje(texturaPersonaje,texturaSalto,texturaPersonaje2);
        // Posición inicial del personaje
        mario.getSprite().setPosition(Plataforma.ANCHO_CAMARA / 10+50, Plataforma.ALTO_CAMARA * 0.90f);
        mario.setVelocidadX(3);

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


        texturaBtnPausa = assetManager.get("BtmPausa.png");
        btnPausa = new Boton(texturaBtnPausa);
        btnPausa.setPosicion(Plataforma.ANCHO_CAMARA - 4.5f * TAM_CELDA,  TAM_CELDA+570);
        btnPausa.setAlfa(0.7f);

        // Gana
        texturaGana = assetManager.get("ganaste.png");
        btnGana = new Boton(texturaGana);
        btnGana.setPosicion(Plataforma.ANCHO_CAMARA/2-btnGana.getRectColision().width/2,Plataforma.ALTO_CAMARA/2-btnGana.getRectColision().height/2);
        btnGana.setAlfa(0.7f);

        texturaManzanas = assetManager.get("Apple.png");

        // Efecto moneda
        sonidoEstrella = assetManager.get("monedas.mp3");
        sonidoPierde = assetManager.get("opendoor.mp3");
        sonidoVida= assetManager.get("vidawi.mp3");
        sonidoLlave=assetManager.get("llave.mp3");

        texturaEnemigo2 = assetManager.get("embudo.png");

        EnemigoV enemigoV1 = new EnemigoV(texturaEnemigo2);
        enemigoV1.setPosicion(100,700);
        enemigosV.add(enemigoV1);

        //Crenado la musica de fondo
        musicFondo = Gdx.audio.newMusic(Gdx.files.internal("little-forest.mp3"));
        musicFondo.setLooping(true);
        musicFondo.play();


        //IMPLEMENTANDO LA PAUSA
        texturaPausa = assetManager.get("Pausa.png");
        texturaPlay = assetManager.get("BtmPlay.png");
        texturaMenu = assetManager.get("back.png");
        texturaSonidoT = assetManager.get("BtmSonido.png");
        texturaMusicaT = assetManager.get("BtmMusic.png");
        texturaSonidoF = assetManager.get("BtmSonidoF.png");
        texturaMusicaF = assetManager.get("BtmMusicF.png");

        btnPantallaPausa = new Boton(texturaPausa);
        //btnPantallaPausa.setPosicion(Plataforma.ANCHO_CAMARA/2, Plataforma.ALTO_CAMARA/2);
        btnPantallaPausa.setAlfa(0.7f);

        btnPlay = new Boton (texturaPlay);
        btnPlay.setPosicion(Plataforma.ANCHO_CAMARA/2+150, Plataforma.ALTO_CAMARA/2);
        btnPlay.setAlfa(0.7f);

        btnMenu = new Boton (texturaMenu);
        btnMenu.setPosicion(Plataforma.ANCHO_CAMARA/2-250, Plataforma.ALTO_CAMARA/2);
        btnMenu.setAlfa(0.7f);

        btnMusicaT = new Boton(texturaMusicaT);
        btnMusicaT.setPosicion(Plataforma.ANCHO_CAMARA/2+150, Plataforma.ALTO_CAMARA/2-180);
        btnMusicaT.setAlfa(0.7f);

        btnMusicaF = new Boton(texturaMusicaF);
        btnMusicaF.setPosicion(Plataforma.ANCHO_CAMARA/2+150, Plataforma.ALTO_CAMARA/2-1000);
        btnMusicaF.setAlfa(0.7f);

        btnSonidoT = new Boton(texturaSonidoT);
        btnSonidoT.setPosicion(Plataforma.ANCHO_CAMARA/2-250, Plataforma.ALTO_CAMARA/2-180);
        btnSonidoT.setAlfa(0.7f);

        btnSonidoF = new Boton(texturaSonidoF);
        btnSonidoF.setPosicion(Plataforma.ANCHO_CAMARA/2-250, Plataforma.ALTO_CAMARA/2-1000);
        btnSonidoF.setAlfa(0.7f);

    }

    /*
    Dibuja TODOS los elementos del juego en la pantalla.
    Este método se está ejecutando muchas veces por segundo.
     */


    @Override
    public void render(float delta) { // delta es el tiempo entre frames (Gdx.graphics.getDeltaTime())
        if (!banderaPausa) {

            if (estadoJuego != EstadosJuego.PERDIO) {
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

            tiempoJuego += Gdx.graphics.getDeltaTime();
            //Gdx.app.log("Tiempo juego", Float.toString(tiempoJuego));

            for (EnemigoV enemigoV : enemigosV) {
                if (enemigoV.getVidas() > 0) {
                    enemigoV.render(batch);

                    if ((int) tiempoJuego == 1 && banderaDisparo) {
                        BalaV balaEnJuego = new BalaV(texturaManzanas);
                        balaEnJuego.setDireccion(-3);
                        balaEnJuego.setPosicion(enemigoV.getX() + 50, enemigoV.getY() + 50);
                        balasL.add(balaEnJuego);
                        banderaDisparo = false;
                        tiempoJuego = 0;
                        enemigoV.setPosicion((int) (1220 * Math.random()) + 1, 750);
                    }

                    for (BalaV bala : balasL) {
                        bala.render(batch);
                        banderaDisparo = true;
                        if ((bala.getX() >= mario.getX() && bala.getX() <= (mario.getX() + mario.getSprite().getWidth())) &&
                                (bala.getY() >= mario.getY() && bala.getY() <= (mario.getY() + enemigoV.getSprite().getHeight()))) {
                            int vidas = enemigoV.getVidas();
                            if (vidaf < 20) {
                                vidaf += 1;
                                sonidoLlave.play();
                            }
                            bala.velocidadX = 10;
                            //Borrar de memoria
                            bala.setPosicion(0, -50);
                        }
                    }
                    if (tiempoJuego > 6) {
                        //ISAIN EL HACKER :)
                        tiempoJuego = 0;
                    }
                } else {
                    //Borrar de memoria
                    enemigoV.setPosicion(0, 2000);
                }

            }

            for (int i = 0; i < balasL.size(); i++) {
                if (balasL.get(i).getY() < 0) {
                    balasL.remove(i);
                }
            }

            if (vidaf == 20) {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        plataforma.setScreen(new CargandoMGDos(plataforma));
                    }
                }, 3);  // 3 segundos
            }


            batch.end();

            // Dibuja el HUD
            batch.setProjectionMatrix(camaraHUD.combined);
            batch.begin();

            // ¿Ya ganó?
            if (estadoJuego == EstadosJuego.GANO) {
                btnGana.render(batch);
            } else {
                btnIzquierda.render(batch);
                btnDerecha.render(batch);
                btnSalto.render(batch);
                btnPausa.render(batch);
                // Estrellas recolectadas
                texto.mostrarMensaje(batch, "Score: " + estrellas, Plataforma.ANCHO_CAMARA / 2 - 200, Plataforma.ALTO_CAMARA * 0.95f);
                texto.mostrarMensaje(batch, "Points: " + vidaf, Plataforma.ANCHO_CAMARA / 2 + 200, Plataforma.ALTO_CAMARA * 0.95f);
            }
            batch.end();
        }
        else{
            // Dibuja La Pausa
            batch.setProjectionMatrix(camaraHUD.combined);

            batch.begin();
            // ¿Ya ganó?
            if (estadoJuego == EstadosJuego.GANO) {
                btnGana.render(batch);
            } else {
                btnPantallaPausa.render(batch);
                btnPlay.render(batch);
                btnMenu.render(batch);
                btnMusicaT.render(batch);
                btnMusicaF.render(batch);
                btnSonidoT.render(batch);
                btnSonidoF.render(batch);
            }
            batch.end();
        }
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
                else if( !esVida(celda) ) {  // Las estrellas no lo detienen :)
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
            }
            else if( (celdaAbajo==null && celdaDerecha==null) || esVida(celdaAbajo) || esVida(celdaDerecha) ) {
                // Celda vacía, entonces el personaje puede avanzar
                mario.caer();
                mario.setEstadoSalto(Personaje.EstadoSalto.CAIDA_LIBRE);
            }
            else {
                // Dejarlo sobre la celda que lo detiene
                mario.setPosicion(mario.getX(), (celdaY + 1) * TAM_CELDA);
                mario.setEstadoSalto(Personaje.EstadoSalto.EN_PISO);

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
            } else if ( esVida(capaPlataforma.getCell(celdaX,celdaY)) ) {
                // Borrar esta estrella y contabilizar
                capaPlataforma.setCell(celdaX,celdaY,null);
                if(vidaf<=4){
                    vidaf++;
                }
                sonidoVida.play();
            }
            else if (esVida(capaPlataforma.getCell(celdaX,celdaY+1)) ) {
                // Borrar esta estrella y contabilizar
                capaPlataforma.setCell(celdaX,celdaY+1,null);
                vidaf++;
                sonidoVida.play();
            }

            else if ( esPuertaA( capaPlataforma1.getCell(celdaX,celdaY) ) ) {
                sonidoPierde.play();
                //estadoJuego = EstadosJuego.PERDIO;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        //plataforma.setScreen(new Menu(plataforma));
                        plataforma.setScreen(new PantallaCargando2_A(plataforma));
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
                estrellas++;
                sonidoLlave.play();

            }else if (esLlave2(capaPlataforma.getCell(celdaX,celdaY))){
                estrellas++;
                sonidoLlave.play();

            }else if(esVida(capaPlataforma.getCell(celdaX,celdaY))){
                capaPlataforma.setCell(celdaX,celdaY+1,null);
                vidaf++;
                sonidoEstrella.play();

            }else if(esPistola(capaPlataforma.getCell(celdaX,celdaY))){
                capaPlataforma.setCell(celdaX,celdaY+1,null);
                capaPlataforma.setCell(celdaX,celdaY,null);
                sonidoVida.play();
            }
            else {
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
                        plataforma.setScreen(new PantallaGanaste(plataforma));
                    }
                }, 3);  // 3 segundos
            }
        }
        if ( capaPlataforma2.getCell(celdaX,celdaY) != null || capaPlataforma2.getCell(celdaX,celdaY+1) != null ) {
            if ( esPuertaA2( capaPlataforma2.getCell(celdaX,celdaY) )) {
                sonidoPierde.play();
                estadoJuego = EstadosJuego.PERDIO;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        plataforma.setScreen(new PantallaGanaste(plataforma));
                    }
                }, 3);  // 3 segundos
            }
        }
        else {
            mario.actualizar();
        }
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
        return "puertaA".equals(propiedad);
    }

    private boolean esPuertaA2(TiledMapTileLayer.Cell celda){
        if (celda==null) {
            return false;
        }
        Object propiedad =celda.getTile().getProperties().get("tipo");
        return "puertaA2".equals(propiedad);

    }

    private boolean esPistola(TiledMapTileLayer.Cell celda){
        if (celda==null) {
            return false;
        }
        Object propiedad =celda.getTile().getProperties().get("tipo");
        return "pistolita".equals(propiedad);

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
        assetManager.unload("MiniGameMapa.tmx");  // Cargar info del mapa
        assetManager.unload("marioSprite.png");
        assetManager.unload("marioSpriteIzq.png");
        assetManager.unload("salto.png");


        // Cargar imagen
        // Texturas de los botones
        assetManager.unload("BtmDerecho.png");
        assetManager.unload("BtmIzquierdo.png");
        assetManager.unload("BtmSaltar.png");
        assetManager.unload("shoot.png");
        assetManager.unload("bullet.png");
        assetManager.unload("embudo.png");
        assetManager.unload("Planta.png");
        assetManager.unload("balaPlanta.png");
        assetManager.unload("balaEmbudo.png");
        assetManager.unload("BtmPausa.png");
        assetManager.unload("embudo.png");

        // Fin del juego
        assetManager.unload("ganaste.png");
        // Efecto al tomar la moneda
        assetManager.unload("monedas.mp3");
        assetManager.unload("llave.mp3");
        assetManager.unload("opendoor.mp3");
        assetManager.unload("vidawi.mp3");
        assetManager.unload("pistola.mp3");
        assetManager.unload("Apple.png");

        //Para la pausa
        assetManager.unload("Pausa.png");
        assetManager.unload("BtmPlay.png");
        assetManager.unload("back.png");
        assetManager.unload("BtmSonido.png");
        assetManager.unload("BtmMusic.png");
        assetManager.unload("BtmSonidoF.png");
        assetManager.unload("BtmMusicF.png");

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
                    banderaDireccion = false;
                    mario.setBanderaPosicion(banderaDireccion);
                    mario.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);

                } else if (btnIzquierda.contiene(x, y) && mario.getEstadoMovimiento() != Personaje.EstadoMovimiento.INICIANDO) {
                    // Tocó el botón izquierda, hacer que el personaje se mueva a la izquierda
                    banderaDireccion = true;
                    mario.setBanderaPosicion(banderaDireccion);
                    mario.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_IZQUIERDA);
                } else if (btnSalto.contiene(x, y)) {
                    // Tocó el botón saltar
                    mario.saltar();
                }else if(btnPausa.contiene(x,y)){
                    //plataforma.setScreen(new PantallaPausa(plataforma));
                    estadoJuego=EstadosJuego.PAUSADO;
                    banderaPausa = true;
                }
            } else if (estadoJuego==EstadosJuego.GANO) {
                if (btnGana.contiene(x,y)) {
                    Gdx.app.exit();//Buuu
                }
            }else if (estadoJuego == EstadosJuego.PAUSADO) {
                if (btnPlay.contiene(x, y)) {
                    // Tocó el botón derecha, hacer que el personaje se mueva a la derecha
                    banderaPausa = false;
                    estadoJuego = EstadosJuego.JUGANDO;
                } else if (btnMenu.contiene(x, y)) {
                    musicFondo.dispose();
                    dispose();
                    plataforma.setScreen(new Menu(plataforma));

                } else if (btnSonidoT.contiene(x, y)) {
                    AssetManager assetManager = plataforma.getAssetManager();

                    btnSonidoF.setPosicion(btnSonidoT.getX(), btnSonidoT.getY());
                    btnSonidoT.setPosicion(Plataforma.ANCHO_CAMARA / 2 - 250, Plataforma.ALTO_CAMARA / 2 - 1000);

                    sonidoEstrella = assetManager.get("Mute.mp3");
                    sonidoLlave = assetManager.get("Mute.mp3");
                    sonidoPierde = assetManager.get("Mute.mp3");
                    sonidoVida = assetManager.get("Mute.mp3");
                } else if (btnSonidoF.contiene(x, y)) {
                    AssetManager assetManager = plataforma.getAssetManager();

                    btnSonidoT.setPosicion(btnSonidoF.getX(), btnSonidoF.getY());
                    btnSonidoF.setPosicion(Plataforma.ANCHO_CAMARA / 2 - 250, Plataforma.ALTO_CAMARA / 2 - 1000);
                    sonidoEstrella = assetManager.get("monedas.mp3");
                    sonidoPierde = assetManager.get("opendoor.mp3");
                    sonidoVida = assetManager.get("vidawi.mp3");
                    sonidoLlave = assetManager.get("llave.mp3");
                } else if (btnMusicaT.contiene(x, y)) {
                    btnMusicaF.setPosicion(btnMusicaT.getX(), btnMusicaT.getY());
                    btnMusicaT.setPosicion(Plataforma.ANCHO_CAMARA / 2 - 250, Plataforma.ALTO_CAMARA / 2 - 1000);
                    musicFondo.pause();
                } else if (btnMusicaF.contiene(x, y)) {
                    btnMusicaT.setPosicion(btnMusicaF.getX(), btnMusicaF.getY());
                    btnMusicaF.setPosicion(Plataforma.ANCHO_CAMARA / 2 - 250, Plataforma.ALTO_CAMARA / 2 - 1000);
                    musicFondo.play();
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

    //if (bandera direccion)
    // a la hora de generar la bala la mandamos con direccion izquierda
}