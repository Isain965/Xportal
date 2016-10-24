package mx.izo.xportal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by isain on 10/24/2016.
 */
public class Bala {

    private Sprite sprite;  // Sprite cuando no se mueve

    // Animación
    private Animation animacion;    // Caminando
    private float timerAnimacion;   // tiempo para calcular el frame

    // Estados de la bala
    private EstadoMovimiento estadoMatando;

    public Bala(Texture textura) {
        // Lee la textura como región
        TextureRegion texturaCompleta = new TextureRegion(textura);
        // La divide en frames de 16x32 (ver marioSprite.png)
        //TextureRegion[][] texturaPersonaje = texturaCompleta.split(16,32);
        TextureRegion[][] texturaPersonaje = texturaCompleta.split(115,115);
        // Crea la animación con tiempo de 0.25 segundos entre frames.
        animacion = new Animation(0.25f,texturaPersonaje[0][0],
                texturaPersonaje[0][1], texturaPersonaje[0][1] );
        // Animación infinita
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        // Inicia el timer que contará tiempo para saber qué frame se dibuja
        timerAnimacion = 0;
        // Crea el sprite cuando para el personaje quieto (idle)
        sprite = new Sprite(texturaPersonaje[0][0]);    // quieto
        //estadoMovimiento = EstadoMovimiento.INICIANDO;
        //estadoSalto = EstadoSalto.EN_PISO;
    }
    public void disparar(float x, float y){//recibir x y del personaje
        //Obtener coordenadas actuales
        float limite = x+300;
        float Cx = x;
        while (x<=limite){
            sprite.setX(Cx++);
        }

    }

    public enum EstadoMovimiento {
        INICIANDO
    }
}