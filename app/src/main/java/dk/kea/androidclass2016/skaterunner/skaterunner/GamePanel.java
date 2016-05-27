package dk.kea.androidclass2016.skaterunner.skaterunner;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Niki Tamjidi on 22-05-2016.
 * This class is where most of the game is going on (GameScreen)
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{

    //Dimenensions of our game
    public static final int WIDTH = 600;
    public static final int HEIGHT = 300;
    public static final int MOVESPEED = -5;

    //reference
    private MainThread thread;
    private Background bg;
    private Blocks bs;
    private Player pl;
    private ArrayList<Blocks> blocks;
    private ArrayList<BlocksSecond> secondBlocks;
    private Random rand = new Random();
    private int maxBlockHeight;
    private int minBlockHeight;
    private int progressDenom = 20; //increase to slow down difficulty progression, decrease to speed up difficulty progression
    private boolean topDown = true;
    private boolean botDown = true;
    private boolean newGameCreated;
    //constructor (automatically called when you create/construct the object)
    public GamePanel(Context context)
    {
        super(context);

        //add the callback to the SurfaceHolder to intercepts events
        getHolder().addCallback(this);

        //instantiate Thread.
        //we are passing getHolder() to it and "THIS" gamePanel.
        thread = new MainThread(getHolder(), this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        //instatiate, get the image and pass it into the Background class constructor
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.skatebg));
        pl = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player), 65, 20, 4);

        blocks = new ArrayList<Blocks>();
        secondBlocks = new ArrayList<BlocksSecond>();
        //will make the image slowly move off the screen


        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        //stop the thread when the surface is "destroyed".
        boolean retry = true;

        //sometimes it can take multiple attempts to stop the thread.
        //so we put it in a loop and a try/catch.
        while (retry)
        {
            //try to stop the thread. set it running to false.
            //if it doesn't succeed, it will go to the catch block and loop will retry.
            //if it does succeed it will skip the catch block and set retry = false.
            try
            {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    //This is going to handle touch events.(listen for touch events on the screen).
    public boolean onTouchEvent(MotionEvent event)
    {
        //If you press down on the screen
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            //This is the first time pressing down, which means the player is not playing yet.
            if (!pl.getPlaying())
            {
                pl.setPlaying(true);
                //pl.setUp(true);
            }
            //If the player is already playing.
            else
            {
                pl.setForward(true);
            }
            return true;
        }

        //Listen for the second event, when you release your finger off the phone
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            //you are no longer going up, so set it to false
            pl.setForward(false);
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void update()
    {
        //update the background
        bg.update();

        //We are only going to update the background, if the player is playing
        if (pl.getPlaying())
        {
            //update the background
            bg.update();
            //only update the player, if the player is playing.
            pl.update();

            //calculate the threshold of height the blocks can have based on the score
            //max and min block heart are update, and the blocks switched direction when either
            //min is met

            maxBlockHeight = 30 + pl.getScore() / progressDenom;
            //cap max block height so that blocks can only take up a total of 1/2 the screen
            if(maxBlockHeight > HEIGHT / 4)maxBlockHeight = HEIGHT/4;
            minBlockHeight = 5 + pl.getScore() / progressDenom;
            //update top blocks
            this.updateTopBlock();

            //update bottom blocks
            this.updateBottomBlock();
        }
        //if the player no longer playing cause of collide...
        else
        {
            //player collied.. newgameCreated false, beacise it's false it calls newGame, newgameCreated becomes true... repeat while player not playing
            newGameCreated = false;
            if(!newGameCreated)
            {
                newGame();
            }

        }
    }

    //we want to draw the background
    //but we have to @Override the draw() from the surfaceView class
    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);

        //scale the image for all devices
        //getWidth() and getHeight(), gives us the width/height
        //of the entire phone screen(surfaceView)
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);
        if (canvas != null)
        {
            //Before we scale, we create a savedState for our canvas
            final int savedState = canvas.save();
            //now we scale
            canvas.scale(scaleFactorX, scaleFactorY);
            //then we draw the background
            bg.draw(canvas);
            //draw the player
            pl.draw(canvas);
            //now we return to the savedState(the unscaled state), because if we don't
            //it will just keep scaling, everytime we call the draw method.
            canvas.restoreToCount(savedState);
        }

        //draw first/topblocks
        for(Blocks block: blocks)
        {
            block.draw(canvas);
        }

        //draw second/bottom blocks
        for(BlocksSecond bs: secondBlocks)
        {
            bs.draw(canvas);
        }
    }

    public void updateTopBlock()
    {
        //every 50 points, insert randomly placed top blocks that break the pattern
        if(pl.getScore() % 50 == 0)
        {
            //blocks.size -1 is the last block in the array added with + 20
            //0 is the y position
            //x is the last block in the arraylist + 20
            //height of third parameter will be a random number from the maxBlockHeight
            blocks.add(new Blocks(BitmapFactory.decodeResource(getResources(), R.drawable.skateblocksecond
            ), blocks.get(blocks.size() -1) .getX() +20, 0,(int) ((rand.nextDouble() * (maxBlockHeight
            ))+1)));
        }
        for(int i = 0; i <blocks.size(); i++)
        {
            blocks.get(i).update();
            if(blocks.get(i).getX() <- 20) //if blocks(top) looping has an x position negative 20 will be removed
            {
                blocks.remove(i);
                //remove element of arraylist, replace by adding new one

                //calculate topdown which determines the direction the border is moving (up or down)
                if(blocks.get(blocks.size() - 1).getHeight() >= maxBlockHeight)
                {
                    topDown = false;
                }
                // if the last element is greater than the max or less than block height, we'll adjust topDown so blocks will go the right direction
                if(blocks.get(blocks.size() -1).getHeight() <= minBlockHeight)
                {
                    topDown = true;
                }
                //new block added will have larger height
                if(topDown)
                {

                    blocks.add(new Blocks(BitmapFactory.decodeResource(getResources(),
                            R.drawable.skateblocksecond), blocks.get(blocks.size() -1 ).getX() + 20,
                            0, blocks.get(blocks.size() -1 ).getHeight() + 1));
                }
                //new block added will have smaller height
                else
                {
                    blocks.add(new Blocks(BitmapFactory.decodeResource(getResources(),
                            R.drawable.skateblocksecond), blocks.get(blocks.size() -1 ).getX() + 20,
                            0, blocks.get(blocks.size() -1 ).getHeight() - 1));
                }
            }
        }
    }
    public void updateBottomBlock()
    {
        //every 40 points, insert randomly placed bottom blocks
        if(pl.getScore() % 40 == 0)
        {
            secondBlocks.add(new BlocksSecond(BitmapFactory.decodeResource(getResources(), R.drawable.skateblocksecond),
                    secondBlocks.get(secondBlocks.size() -1).getX() + 20, (int) ((rand.nextDouble()
            * maxBlockHeight) + (HEIGHT - maxBlockHeight))));
        }

        //update bottom blocks
        for(int i = 0; i <secondBlocks.size(); i++)
        {
            secondBlocks.get(i).update();

            //if block is moving off screen, remove it and add a new one
            if(secondBlocks.get(i).getX() <- 20)
            {
                secondBlocks.remove(i);


                //determine if block will be moving up or down
                if (secondBlocks.get(secondBlocks.size() - 1).getHeight() >= maxBlockHeight)
                {
                    botDown = false;
                }
                // if the last element is greater than the max or less than block height, we'll adjust topDown so blocks will go the right direction
                if (secondBlocks.get(secondBlocks.size() - 1).getHeight() <= minBlockHeight)
                {
                    botDown = true;
                }

                //botDown adding blocks is 1 less from the last block from the array
                if (botDown)
                {
                    // X position is + 20 the last in the array (width is 20)
                    // Y position is the last element of the array (most recent) + 1
                    //moving downwards, upwards...
                    secondBlocks.add(new BlocksSecond(BitmapFactory.decodeResource(getResources(), R.drawable.skateblocksecond
                    ), secondBlocks.get(secondBlocks.size() - 1).getX() + 20, secondBlocks.get(secondBlocks.size() - 1
                    ).getY() + 1));
                }

                // else adding block 1 more than the last element in the array
                else
                {
                    //moving upwards
                    secondBlocks.add(new BlocksSecond(BitmapFactory.decodeResource(getResources(), R.drawable.skateblock
                    ), secondBlocks.get(secondBlocks.size() - 1).getX() + 20, secondBlocks.get(secondBlocks.size() - 1
                    ).getY() - 1));
                }
            }
        }
    }
    public void newGame()
    {
        //called everytime the player dies and then reset the game
        blocks.clear();
        secondBlocks.clear();


        minBlockHeight = 5;
        maxBlockHeight = 30;
        pl.resetDYA();
        pl.resetScore(); //reset score
        pl.setY(HEIGHT / 2); //reset player position

        //create initial blocks

        //loop create blocks till width + 40 off screen

        //initial top blocks
        for (int i = 0; i * 20 < WIDTH * 40; i++)
        {
            //first block created
            if (i == 0)
            {
                //create 1 element of the block for the first
                blocks.add(new Blocks(BitmapFactory.decodeResource(getResources(), R.drawable.skateblocksecond
                ), i * 20, 0, 10));
            } else
            {
                //get the last element of the array, get the height of the last element of the array and add +1
                blocks.add(new Blocks(BitmapFactory.decodeResource(getResources(), R.drawable.skateblocksecond
                ), i * 20, 0, blocks.get(i - 1).getHeight() + 1));
            }
        }

        //initial second blocks (bottom blocks)
        for (int i = 0; i * 20 < WIDTH + 40; i++)

        {
            //first blocks ever created
            if (i == 0)
            {
                secondBlocks.add(new BlocksSecond(BitmapFactory.decodeResource(getResources(), R.drawable.skateblocksecond)
                        , i * 20, HEIGHT - minBlockHeight));
            }
            //adding blocks until the initial screen is filed
            else
            {
                secondBlocks.add(new BlocksSecond(BitmapFactory.decodeResource(getResources(), R.drawable.skateblock),
                        i * 20, secondBlocks.get(i - 1).getY() - 1));
            }

        }
        newGameCreated = true;


    }
}