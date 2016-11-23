package mx.izo.proyecto;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by isain on 10/24/2016.
 */

public class Bala {


    private Sprite sprite;
    public float velocidadX = 10;
    private float x,y;
    private int dir = 0;//cero es para movimiento en x si es uno movimiento en y

    public Bala(Texture textura) {
        TextureRegion texturaBala = new TextureRegion(textura);
        sprite = new Sprite(texturaBala);
    }

    //el boolean bandera es para definir la direccion
    public void render(SpriteBatch batch, boolean bandera) {//De que es esta bandera? comenten su código

        TextureRegion region = sprite;
        if (bandera) {
            if (!region.isFlipX()) {
                region.flip(true, false);
            }
        } else {
            if (region.isFlipX()) {
                region.flip(true, false);
            }
        }
        if(dir == 0) {//por defecto dispara solo en el eje de las x
            x = sprite.getX() + velocidadX;
            sprite.setX(x);
        } else if(dir == 1) {//se cambia con el setter de dir para disparar a las Y
            y = sprite.getY() + velocidadX;
            sprite.setY(y);
        }
        batch.draw(region, sprite.getX(), sprite.getY());

    }

    public void setDireccion(float direccion){
        this.velocidadX = direccion;
    }

    public void setPosicion(float x, float y) {
        sprite.setPosition(x, y);
    }

    public void setVelocidadX(float vel){
        this.velocidadX = vel;
    }

    public void setDir(int dir){
        this.dir = dir;
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