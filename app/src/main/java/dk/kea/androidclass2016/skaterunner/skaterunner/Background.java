package dk.kea.androidclass2016.skaterunner.skaterunner;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Niki Tamjidi on 23-05-2016.
 */
public class Background
{

    private Bitmap image;
    //coordinates for the background
    private int x, y, dx;

    public Background(Bitmap res)
    {
        image = res;
    }

    public void update()
    {
        //Change the x position, everytime it updates, which update is constantly called
        //(in the update() in GamePanel)
        x += dx;
        //what happens when it is off the screen? We want to reset it
        //If x<0 the image is completly off the screen
        if (x < -GamePanel.WIDTH)
        {
            //When x=0 we will start moving off the screen again
            x = 0;
        }
    }

    public void draw(Canvas canvas)
    {
        //Draw the canvas
        canvas.drawBitmap(image, x, y, null);
        //We have to draw second image in front of the first, so the scrolling looks continous.
        //So if part of the image is off the screen, we are gonna compensate for that, by drawing
        //a second image after the first image.
        if (x < 0)
        {
            canvas.drawBitmap(image, x + GamePanel.WIDTH, y, null);
        }
    }

    public void setVector(int dx)
    {
        this.dx = dx;
    }
}
