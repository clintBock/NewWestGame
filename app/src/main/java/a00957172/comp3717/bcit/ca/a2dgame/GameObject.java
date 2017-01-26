package a00957172.comp3717.bcit.ca.a2dgame;

import android.graphics.Rect;

/**
 * Created by couga on 2017-01-14.
 */

public abstract class GameObject {
    protected int x;
    protected int y;
    protected int dy;
    protected int dx;
    protected int width;
    protected int height;

    public void setx(int x){
        this.x = x;
    }

    public int getx(){
        return x;
    }

    public void sety(int y){
        this.y = y;
    }

    public int gety(){
        return y;
    }
    public void setWidth(int width){
        this.width = width;
    }

    public int getWidth(){
        return width;
    }
    public void setHeight(int height){
        this.height = height;
    }

    public int getHeight(){
        return height;
    }
    public Rect getRectangle(){
        return new Rect(x, y, x+width, y+height);
    }

}
