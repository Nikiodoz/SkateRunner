package dk.kea.androidclass2016.skaterunner.skaterunner;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Niki Tamjidi on 22-05-2016.
 * This class is where the game loop is gonna run (game class)
 */
public class MainThread extends Thread
{
    private int FPS = 30;
    private double avgFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel)
    {
        //call the constructor of the superclass
        super();

        //set surfaceHolder reference here, to the surfaceHolder that is passed in
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run()
    {
        //here (in our game loop) we are trying to cap the FPS at 30.

        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        //each time you run through the game loop, you want it to take 1000/30 milliseconds
        long targetTime = 1000 / FPS;

        while (running)
        {
            startTime = System.nanoTime();
            canvas = null;

            //try locking the canvas for pixel editing
            try
            {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder)
                {
                    //this is gonna be the "meat" of our program.
                    //everytime we go through the game loop,
                    //you're gonna update the game once and draw the game once.
                    //It's called so fast (hopefully 30x a second), that it will flow naturally.
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            } catch (Exception e)
            {
            }

            //always executed regardless the catch.
            finally
            {
                if (canvas != null)
                {
                    try
                    {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            //been through one circle of updating and drawing.
            //here is how many milliseconds it took, to update and draw the game(canvas) once.
            //our target is to get, each time update and draw goes through, is 1/30 of a second.
            timeMillis = (System.nanoTime() - startTime) / 1000000;

            //how long we are gonna wait, to go through the loop again.
            waitTime = targetTime - timeMillis;

            //make thread wait, that waitTime!
            try
            {
                this.sleep(waitTime);
            } catch (Exception e)
            {
            }

            //by now we should have going through it all once (should take 1/30 of a seconds).
            //frameCount goes up by 1.
            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == FPS)
            {
                avgFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(avgFPS);
            }
        }
    }

    public void setRunning(boolean b)
    {
        running = b;
    }
}