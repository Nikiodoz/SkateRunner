package dk.kea.androidclass2016.skaterunner.skaterunner;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Game extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set screen to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content on screen
        setContentView(new GamePanel(this));
    }
}