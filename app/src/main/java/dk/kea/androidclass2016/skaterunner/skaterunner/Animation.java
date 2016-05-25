package dk.kea.androidclass2016.skaterunner.skaterunner;

import android.graphics.Bitmap;
import android.provider.Settings;

/**
 * Created by Niki Tamjidi on 24-05-2016.
 */
public class Animation
{
    private Bitmap[] frames;
    private int currentFrame;
    private long startTime;
    private long delay;
    private boolean playedOnce;

    public void setFrames(Bitmap[] frames)
    {
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
    }

    public void setDelay(long d)
    {
        delay = d;

    }

    //In case we want to manually set the frame
    public void setFrame(int i)
    {
        currentFrame = i;
    }

    public void update()
    {
        long elapsed = (System.nanoTime() - startTime) / 1000000;

        if (elapsed > delay)
        {
            currentFrame++;
            startTime = System.nanoTime();

        }

        if (currentFrame == frames.length)
        {
            currentFrame = 0;
            playedOnce = true;
        }
    }

    //What the player class is going to draw is determined by this method
    public Bitmap getImage()
    {
        return frames[currentFrame];
    }

    public int getFrame()
    {
        return currentFrame;
    }

    public boolean isPlayedOnce()
    {
        return playedOnce;
    }
}


