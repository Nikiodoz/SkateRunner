package dk.kea.androidclass2016.skaterunner.skaterunner;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Davidsen on 27/05/2016.
 */
public class Explosion
{
    private int x;
    private int y;
    private int width;
    private int height;
    private int row;
    private Animation animation = new Animation();
    private Bitmap spritesheet;


    public Explosion(Bitmap res, int x, int y, int w, int h, int numFrames){


        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;

        Bitmap[] image = new Bitmap[numFrames]; // Assign this bitmap to all explosions

        spritesheet = res;

        for(int i = 0; i< image.length; i++){

            if(i%5 == 0&&i>0)row++;
            image[i] = Bitmap.createBitmap(spritesheet, (i-(5*row))*width, row*height, width, height);


        }

        animation.setFrames(image);
        animation.setDelay(10);


    }

    public void draw(Canvas canvas){

        if (!animation.isPlayedOnce()){

            canvas.drawBitmap(animation.getImage(),x,y,null);
        }


    }

    public void update(){


        if (!animation.isPlayedOnce()){

        animation.update();

        }
    }

    public int getHeight(){return height;}


}
