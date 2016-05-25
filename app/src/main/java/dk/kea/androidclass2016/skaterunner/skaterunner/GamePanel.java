package dk.kea.androidclass2016.skaterunner.skaterunner;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Niki Tamjidi on 22-05-2016.
 * This class is where most of the game is going on (GameScreen)
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{

    //Dimenensions of our game
    public static final int WIDTH = 600;
    public static final int HEIGHT = 300;
    public static final int MOVESPEED = -5;

    //reference
    private MainThread thread;
    private Background bg;
    private Player pl;

    //constructor (automatically called when you create/construct the object)
    public GamePanel(Context context)
    {
        super(context);

        //add the callback to the SurfaceHolder to intercepts events
        getHolder().addCallback(this);

        //instantiate Thread.
        //we are passing getHolder() to it and "THIS" gamePanel.
        thread = new MainThread(getHolder(), this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        //instatiate, get the image and pass it into the Background class constructor
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.skatebg));
        pl = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player), 65, 20, 4);

        //will make the image slowly move off the screen


        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        //stop the thread when the surface is "destroyed".
        boolean retry = true;

        //sometimes it can take multiple attempts to stop the thread.
        //so we put it in a loop and a try/catch.
        while (retry)
        {
            //try to stop the thread. set it running to false.
            //if it doesn't succeed, it will go to the catch block and loop will retry.
            //if it does succeed it will skip the catch block and set retry = false.
            try
            {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    //This is going to handle touch events.(listen for touch events on the screen).
    public boolean onTouchEvent(MotionEvent event)
    {
        //If you press down on the screen
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            //This is the first time pressing down, which means the player is not playing yet.
            if (!pl.getPlaying())
            {
                pl.setPlaying(true);
            }
            //If the player is already playing.
            else
            {
                pl.setForward(true);
            }
            return true;
        }

        //Listen for the second event, when you release your finger off the phone
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            //you are no longer going up, so set it to false
            pl.setForward(false);
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void update()
    {
        //We are only going to update the background, if the player is playing
        if (pl.getPlaying())
        {
            //update the background
            bg.update();
            //only update the player, if the player is playing.
            pl.update();
        }
    }

    //we want to draw the background
    //but we have to @Override the draw() from the surfaceView class
    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);

        //scale the image for all devices
        //getWidth() and getHeight(), gives us the width/height
        //of the entire phone screen(surfaceView)
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);
        if (canvas != null)
        {
            //Before we scale, we create a savedState for our canvas
            final int savedState = canvas.save();
            //now we scale
            canvas.scale(scaleFactorX, scaleFactorY);
            //then we draw the background
            bg.draw(canvas);
            //draw the player
            pl.draw(canvas);
            //now we return to the savedState(the unscaled state), because if we don't
            //it will just keep scaling, everytime we call the draw method.
            canvas.restoreToCount(savedState);
        }
    }
}