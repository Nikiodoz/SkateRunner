package dk.kea.androidclass2016.skaterunner.skaterunner;

/**
 * Created by Ahmed on 25-05-2016.
 */
public class Blocks
{
    public static final float Width = 40;
    public static final float HEIGHT = 18;
    int type;
    float x;
    float y;

    public Blocks(float x, float y, int type)
    {
        this.x = x;
        this.y = y;
        this.type = type;
    }
}
