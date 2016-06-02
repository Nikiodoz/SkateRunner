package dk.kea.androidclass2016.skaterunner.skaterunner;

import android.graphics.Bitmap;
import android.graphics.Canvas;


/**
 * Created by Niki Tamjidi on 24-05-2016.
 */
public class Player extends GameObject
{
    //player will use a spritesheet
    private Bitmap spritesheet;
    private int score;
    //acceleration (extra variable)
    private double dya;
    private boolean up;
    private boolean playing;
    private Animation animation = new Animation();
    //use to increment the score
    private long startTime;

    public Player(Bitmap res, int w, int h, int numFrames)
    {
        x = 100;
        y = GamePanel.HEIGHT / 2;
        dy = 0;
        score = 0;
        height = h;
        width = w;

        //Here we create a Bitmap[], that will store all the different sprites
        Bitmap[] image = new Bitmap[numFrames];

        //set spritesheet
        spritesheet = res;

        //We want to divide the image into pieces and store them,
        //Then our animation class will scroll through them.
        for (int i = 0; i < image.length; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, i * width, 0, width, height);
        }

        //Pass array into the animation class
        animation.setFrames(image);
        animation.setDelay(10);
        startTime = System.nanoTime();
    }

    //This method is going to be called by the motionEvent,
    //when we press forward the player will go forward
    public void setUp(boolean b)
    {
        up = b;
    }

    public void update()
    {
        //So every 1/10 second the score should go up by 1
        long elapsed = (System.nanoTime() - startTime / 1000000);
        if (elapsed > 100)
        {
            score++;
            startTime = System.nanoTime();
        }

        animation.update();

        //If we press up, we should accelerate
        if (up)
        {
            dy = (int) (dya -= 1.1);
        }
        //else we slow down
        else
        {

            //Here we cap the speed of the player
            if (dy > 8) dy = 8;
            if (dy < -8) dy = -8;

            y += dy * 2;
            dy = 0;
        }
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(animation.getImage(), x, y, null);
    }

    public int getScore()
    {
        return score;
    }

    public boolean getPlaying()
    {
        return playing;
    }

    public void setPlaying(boolean b)
    {
        playing = b;
    }

    public void resetDYA()
    {
        dya = 0;
    }

    public void resetScore()
    {
        score = 0;
    }

}
