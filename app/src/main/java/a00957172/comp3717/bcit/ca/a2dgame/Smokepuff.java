package a00957172.comp3717.bcit.ca.a2dgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by couga on 2017-01-18.
 */

public class Smokepuff extends GameObject{
    public int radius;
    public Smokepuff(int x, int y)
    {
        radius = 5;
        super.x = x;
        super.y = y;
    }
    public void update()
    {
        x -= 10;
    }
    public void draw(Canvas canvas)
    {
        Paint paint = new Paint();
        int colorIdentifier = (int)Math.floor((Math.random() * 3) + 1);
        paint.setColor(colorIdentifier <= 2 ? Color.GRAY : Color.BLACK);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(x-radius, y-radius, radius, paint);
        canvas.drawCircle(x-radius+2, y-radius-2, radius, paint);
        canvas.drawCircle(x-radius+4, y-radius+1, radius, paint);
    }
}
