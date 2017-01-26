package a00957172.comp3717.bcit.ca.a2dgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.test.suitebuilder.annotation.Smoke;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by couga on 2017-01-14.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    
    public static final int WIDTH = 736;
    public static final int HEIGHT = 460;
    public static final int MOVESPEED = -5;
    private long smokeStartTime;
    private long missileStartTime;
    private MainThread thread;
    private Background bg;
    private Player player;
    private ArrayList<Smokepuff> smoke;
    private ArrayList<Missile> missiles;
    private Random rand = new Random();

    public GamePanel(Context context)
    {
        super(context);

        //add the callback to the surfaceholder to intercept finger presses
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        int counter = 0;
        while (retry && counter<1000) {

            counter++;
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
            } catch (InterruptedException e) {e.printStackTrace();}

        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.backup));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.helicopter), 65, 25, 3);
        smoke = new ArrayList<Smokepuff>();
        missiles = new ArrayList<Missile>();
        smokeStartTime = System.nanoTime();
        missileStartTime = System.nanoTime();

        //when surface is created we can safely start game loop
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            if(!player.getPlaying()){
                player.setPlaying(true);
            }
            else{
                player.setUp(true);
            }
            return true;
        }
        if(event.getAction()==MotionEvent.ACTION_UP){
            player.setUp(false);
            return true;
        }
        return super.onTouchEvent(event);
    }
    public void update()
    {
        if(player.getPlaying()){
            bg.update();
            player.update();

            //add missiles on timer
            long missilesElapsed = (System.nanoTime() - missileStartTime)/1000000;
            if(missilesElapsed > (2000 - player.getScore()/2))
            {
                //first missile goes down the center
                if (missiles.size() == 0){
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.missile),
                            WIDTH + 10, HEIGHT/2, 45, 15, player.getScore(), 13));
                }
                else{
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.missile),
                            WIDTH + 10, (int)(rand.nextDouble()* (HEIGHT)), 45, 15, player.getScore(), 13));
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.missile),
                            WIDTH + 10, (int)(rand.nextDouble()* (HEIGHT)), 45, 15, player.getScore(), 13));
                }
                if(player.getScore() > 1000){
                    int allOutWar = 5;
                    for(int i = 0; i < allOutWar; i++){
                        missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.missile),
                                WIDTH + 10, (int)(rand.nextDouble()* (HEIGHT)), 45, 15, player.getScore(), 13));
                    }
                }
                System.out.println(player.getScore());
                //reset the timer after creating a new missile
                missileStartTime = System.nanoTime();

            }
            //loop through every missile and check for collision
            //and if its still on the screen
            for(int i = 0; i < missiles.size(); i++){
                missiles.get(i).update();
                if(collision(missiles.get(i), player))
                {
                    missiles.remove(i);
                    player.setPlaying(false);
                    break;
                }
                //remove missiles off the screen
                if(missiles.get(i).getx() < -100)
                {
                    missiles.remove(i);
                    break;
                }
            }


            //smoke
            long elapsed = (System.nanoTime() - smokeStartTime)/1000000;
            if(elapsed > 120){
                smoke.add(new Smokepuff(player.getx(), player.gety() + 10));
                smokeStartTime = System.nanoTime();
            }

            for(int i = 0; i < smoke.size(); i++){
                smoke.get(i).update();
                if(smoke.get(i).getx() <-10){
                    smoke.remove(i);
                }

            }

        }

    }

    public boolean collision(GameObject a, GameObject b)
    {
        if (Rect.intersects(a.getRectangle(), b.getRectangle()))
        {
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas)
    {
        final float scaleFactorX = getWidth()/(WIDTH * 1f);
        final float scaleFactorY = getHeight()/(HEIGHT * 1f);
        if(canvas!=null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);

            //drawe background
            bg.draw(canvas);

            //draw player
            player.draw(canvas);

            //draw smoke
            for(Smokepuff sp: smoke){
                sp.draw(canvas);
            }
            //draw missiles
            for(Missile m: missiles){
                m.draw(canvas);
            }
            canvas.restoreToCount(savedState);
        }
    }
}
