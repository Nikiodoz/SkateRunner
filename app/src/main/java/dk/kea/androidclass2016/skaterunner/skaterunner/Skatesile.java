package dk.kea.androidclass2016.skaterunner.skaterunner;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by Ahmed on 27-05-2016.
 */
public class Skatesile extends GameObject
{
    private int score;
    private int speed;
    private Random rand = new Random(); //to generate our skatesile we'll use Random
    private Animation animation = new Animation(); //for animation for our skatesile
    private Bitmap spritesheet;

    public Skatesile(Bitmap res, int x, int y, int w, int h, int s, int numFrames)
    {
        //super.x calls the superclass in game object
        super.x = x;
        super.y = y;

        width = w;
        height = h;
        score = s;

        speed = 7 + (int) (rand.nextDouble() * score / 30); //range of skatesile will be speed of 7 + random number * score / 30

        //cap skatesile speed
        if(speed >= 40) speed = 40;

        //bitmap image array
        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;

        //loop through the images
        for(int i = 0; i<image.length; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, 0, i * height, width, height);
        }

        //send array to the animation class
        animation.setFrames(image);
        animation.setDelay(100-speed); //if skatesile is faster, the delay will be less and skatesile will go faster
    }
    public void update()
    {
        x = speed;
        animation.update();
    }
    public void draw(Canvas canvas)
    {
        try
        {
            canvas.drawBitmap(animation.getImage(), x, y , null);
        }
        catch

            (Exception e){
        }
    }

    public int getWidth()
    {
        //offset slightly for more realistic collision detect
        return width - 10;
    }

}
