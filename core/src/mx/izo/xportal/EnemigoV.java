package mx.izo.xportal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by isain on 10/25/2016.
 */
public class EnemigoV {
    private Sprite sprite;
    private int vidas = 5;

    //animacion
    private Animation animacion;    // Caminando
    private float timerAnimacion;   // tiempo para calcular el frame

    int mov=0;

    public EnemigoV(Texture textura, int mg){
        if(mg==2) {
            mov=2;
            TextureRegion texturaEnemigo = new TextureRegion(textura);
            sprite = new Sprite(texturaEnemigo);
            TextureRegion[][] texturaPersonaje;
            texturaPersonaje = texturaEnemigo.split(64, 64);
            animacion = new Animation(0.25f, texturaPersonaje[0][0], texturaPersonaje[1][0]);
            // Animación infinita
            animacion.setPlayMode(Animation.PlayMode.LOOP);
            // Inicia el timer que contará tiempo para saber qué frame se dibuja
            timerAnimacion = 0;
            // Crea el sprite cuando para el personaje quieto (idle)
            sprite = new Sprite(texturaPersonaje[0][0]);    // quieto
        }
    }


    public EnemigoV(Texture textura) {
        TextureRegion texturaEnemigo = new TextureRegion(textura);

        sprite = new Sprite(texturaEnemigo);

        TextureRegion[][] texturaPersonaje;
        texturaPersonaje = texturaEnemigo.split(124, 134);
        animacion = new Animation(0.25f, texturaPersonaje[0][5], texturaPersonaje[0][4], texturaPersonaje[0][3],texturaPersonaje[0][2], texturaPersonaje[0][1],texturaPersonaje[0][0],
                texturaPersonaje[0][1], texturaPersonaje[0][2],texturaPersonaje[0][3], texturaPersonaje[0][4]);
        // Animación infinita
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        // Inicia el timer que contará tiempo para saber qué frame se dibuja
        timerAnimacion = 0;
        // Crea el sprite cuando para el personaje quieto (idle)
        sprite = new Sprite(texturaPersonaje[0][0]);    // quieto

    }

    int i=0;
    boolean bandDer;

    public void render(SpriteBatch batch) {
        /*if(sprite.getX()>=0 && sprite.getX()<=sprite.getWidth()*2){
            bandDer=true;
        } else if(sprite.getX()>PantallaMGDos.ANCHO_MAPA-sprite.getWidth()*2 && sprite.getX()<PantallaMGDos.ANCHO_MAPA){
            bandDer=false;
        }

        if(bandDer){
            if(sprite.getX()<PantallaMGDos.ANCHO_MAPA){// - sprite.getWidth()) {
                sprite.setX(sprite.getX() + i++);
            }
        }else if(sprite.getX()>0){
            sprite.setX(sprite.getX()+i--);
        }*/



        timerAnimacion += Gdx.graphics.getDeltaTime();
        // Obtiene el frame que se debe mostrar (de acuerdo al timer)
        TextureRegion region = animacion.getKeyFrame(timerAnimacion);
        batch.draw(region, sprite.getX(), sprite.getY());
    }

    public void setPosicion(float x, float y) {
        sprite.setPosition(x, y);
    }

    public int getVidas(){
        return vidas;
    }

    public void setVidas(int vidas){
        this.vidas = vidas;
    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    public Sprite getSprite() {
        return sprite;
    }
}
