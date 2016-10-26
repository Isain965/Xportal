package mx.izo.xportal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.StringBuilder;
import com.google.gwt.user.client.rpc.core.java.lang.Integer_CustomFieldSerializer;

/**
 * Created by isain on 10/24/2016.
 */

public class Bala {


    private Sprite sprite;
    public float velocidadX = 10;
    private float x;
    //private int contadorP=401;


    public Bala(Texture textura) {
        TextureRegion texturaBala = new TextureRegion(textura);
        sprite = new Sprite(texturaBala);
    }

    public void render(SpriteBatch batch) {

        TextureRegion region = sprite;
        x = sprite.getX() + velocidadX;
        //contadorP+=1;
        //Gdx.app.log("Contador pixeles", Integer.toString(contadorP));
        sprite.setX(x);
        batch.draw(region, sprite.getX(), sprite.getY());

    }

    public void setDireccion(float direccion){
        this.velocidadX = direccion;
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

    /*public  int getContadorP(){
        return contadorP;
    }

    public void setContadorP(int x) {
        this.contadorP= x;
    }*/

    public Sprite getSprite() {
        return sprite;
    }
}