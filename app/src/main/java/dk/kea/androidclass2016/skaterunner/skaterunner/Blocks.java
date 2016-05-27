package dk.kea.androidclass2016.skaterunner.skaterunner;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Ahmed on 25-05-2016.
 */
public class Blocks extends GameObject
{
    private Bitmap image;

    //different height depending what we want to set it to
    //set the width of the block to 20 (temp width)
    public Blocks(Bitmap res, int x, int y, int h)
    {
        height = h;
        width = 20;

        this.x = x;
        this.y = y;
        dx = GamePanel.MOVESPEED;
        image = Bitmap.createBitmap(res, 0, 0, width, height);
    }

    public void update()
    {
        x+=dx;
    }

    public void draw(Canvas canvas)
    {
        //try(canvas.drawBitmap(image, x, y, null);} catch(Exception e) {};
        try {canvas.drawBitmap(image, x, y, null);} catch(Exception e) {};
    }
}
