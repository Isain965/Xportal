package mx.izo.xportal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by k3ll3x on 21/10/2016.
 */
public class Nave {
    private float vel = 2;
    private static float x0=1280/2; //10;
    private static float y0=800; //11;
    private static float x,y;
    private Sprite sprite;
    private EstadoMovimiento estadoMovimiento;

    public Nave(Texture textura){
        sprite = new Sprite(textura);
    }

    //dibuja a la nave
    public void render(SpriteBatch batch){
        switch(estadoMovimiento){
            case INICIANDO:
                sprite.setX(x0);
                sprite.setY(y0);
                sprite.draw(batch);
                break;
        }
    }

    public void actualizar(){
        x = sprite.getX();
        switch(estadoMovimiento){
            case MOV_DERECHA:
                x += vel;
                if(x<=PantallaMapaMGUno.ANCHO_MAPA-sprite.getWidth()){
                    sprite.setX(x);
                }
                break;
            case MOV_IZQUIERDA:
                x -= vel;
                if(x>=0){
                    sprite.setX(x);
                }
                break;
        }
    }

    public static float getX() {
        return x;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public enum EstadoMovimiento {
        INICIANDO,
        QUIETO,
        MOV_IZQUIERDA,
        MOV_DERECHA
    }
}
