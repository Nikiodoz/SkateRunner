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
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 602;

    //reference
    private MainThread thread;
    private Background bg;

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
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.sktbg));

        //will make the image slowly move off the screen
        bg.setVector(-5);

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
    public boolean onTouchEvent(MotionEvent event)
    {
        return super.onTouchEvent(event);
    }

    public void update()
    {
        //update the background
        bg.update();
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
        final float scaleFactorX = getWidth() / WIDTH;
        final float scaleFactorY = getHeight() / HEIGHT;
        if (canvas != null)
        {
            //Before we scale, we create a savedState for our canvas
            final int savedState = canvas.save();
            //now we scale
            canvas.scale(scaleFactorX, scaleFactorY);
            //then we draw the background
            bg.draw(canvas);
            //now we return to the savedState(the unscaled state), because if we don't
            //it will just keep scaling, everytime we call the draw method.
            canvas.restoreToCount(savedState);
        }
    }
}