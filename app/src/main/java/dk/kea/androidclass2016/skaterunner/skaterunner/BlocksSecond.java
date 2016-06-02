package dk.kea.androidclass2016.skaterunner.skaterunner;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Ahmed on 25-05-2016.
 *
 * this is bottom border.
 */
public class BlocksSecond extends GameObject
{
    private Bitmap image;

    //every block will have a height of 200
    public BlocksSecond(Bitmap res, int x, int y)
    {
        height = 200;
        width = 20;

        this.x = x;
        this.y = y;
        dx = GamePanel.MOVESPEED;

        image = Bitmap.createBitmap(res, 0, 0, width, height);
    }

    public void update ()
    {
        x +=dx;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image, x, y, null);
    }
}
