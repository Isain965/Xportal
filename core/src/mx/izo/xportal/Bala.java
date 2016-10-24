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

    private Sprite sprite;
    private float angulo,vel;
    public float velocidadX= 2;
    private SpriteBatch batch;
    private float x,y;

    public Bala (Texture textura){
        TextureRegion texturaDardo = new TextureRegion(textura);
        sprite = new Sprite(texturaDardo);


    }

    public void render(SpriteBatch batch){//,Personaje pinguino,float vidas){
        //vel = 10f;
        //sprite.translate(vel,vel*angulo);
        //sprite.draw(batch);
        //System.out.println(angulo);
        //  velocidadX = 2;

        //timerAnimacion += Gdx.graphics.getDeltaTime();
        TextureRegion region = sprite;
        //if(region.isFlipX()){
        //    region.flip(true,false);
        //}

        x=sprite.getX()+velocidadX;
        sprite.setX(x);
        batch.draw(region,sprite.getX(),sprite.getY());
        /*if(pinguino.getX() == sprite.getX()){
            velocidadX = 0;
            vidas--;
        }*/

    }

    /*public void addBala(float y){
        dardos.add(sprite);
    }
    public void draw(){
        for(int i=0; i< dardos.size(); i++){
            dardos.get(i).draw(batch);
        }
    }
    public void actualizar(){
        for(int i=0; i<dardos.size(); i++){
            dardos.get(i).translate(vel,angulo);
        }
    }
    public void disparar(float vel,Personaje pinguino, Personaje enemigo){
    }*/

    public void setPosicion(float x, float y){
        sprite.setPosition(x,y);
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return sprite.getY();
    }

    public Sprite getSprite(){
        return sprite;
    }
}