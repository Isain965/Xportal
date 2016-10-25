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
public class Enemigo {
    private Sprite sprite;
    private Bala bala;

    //animacion
    private Animation animacion;    // Caminando
    private float timerAnimacion;   // tiempo para calcular el frame

    public Enemigo(Texture textura) {
        TextureRegion texturaEnemigo = new TextureRegion(textura);
        sprite = new Sprite(texturaEnemigo);
        // La divide en frames de 16x32 (ver marioSprite.png)
        //TextureRegion[][] texturaPersonaje = texturaCompleta.split(16,32);
        TextureRegion[][] texturaPersonaje = texturaEnemigo.split(269,134);
        // Crea la animación con tiempo de 0.25 segundos entre frames.
        animacion = new Animation(0.25f,texturaPersonaje[0][5],
                texturaPersonaje[0][2], texturaPersonaje[0][1] );
        // Animación infinita
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        // Inicia el timer que contará tiempo para saber qué frame se dibuja
        timerAnimacion = 0;
        // Crea el sprite cuando para el personaje quieto (idle)
        sprite = new Sprite(texturaPersonaje[0][0]);    // quieto
    }

    public void render(SpriteBatch batch,Texture texturaBala) {
        Bala bala = new Bala(texturaBala);
        bala.render(batch);

        timerAnimacion += Gdx.graphics.getDeltaTime();
        // Obtiene el frame que se debe mostrar (de acuerdo al timer)
        TextureRegion region = animacion.getKeyFrame(timerAnimacion);
        // Dirección correcta
        if (!region.isFlipX()) {
            region.flip(true,false);
            if (region.isFlipX()) {
                region.flip(true,false);
            }
        }
        // Dibuja el frame en las coordenadas del sprite
        batch.draw(region, sprite.getX(), sprite.getY());
    }

    public void setPosicion(float x, float y) {
        sprite.setPosition(x, y);
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
