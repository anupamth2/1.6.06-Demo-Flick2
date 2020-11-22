package com.udacity.gamedev.inputtestbed;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import sun.rmi.runtime.Log;


public class BouncingBall extends InputAdapter {

    private static final Color COLOR = Color.RED;
    private static final float DRAG = 1.0f;
    private static final float RADIUS_FACTOR = 1.0f / 20;
    private static final float RADIUS_GROWTH_RATE = 1.5f;
    private static final float MIN_RADIUS_MULTIPLIER = 0.1f;
    private static final float ACCELERATION = 500.0f;
    private static final float MAX_SPEED = 4000.0f;
    private static final float KICK_VELOCITY = 500.0f;
    private static final float FLICK_MULTIPLIER = 5.0f;
     int time;


    Vector2 flickStart;
    boolean flicking = false;
    float d1=0;
    float e1=0;

    float baseRadius;
    float radiusMultiplier;

    Vector2 position;
    Vector2 velocity;

    Viewport viewport;
    int[] arr=new int[25];
    Color[] cole=new Color[25];


    public BouncingBall(Viewport viewport) {
        this.viewport = viewport;
        init();

    }

    public void init() {
        position = new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2);
        velocity = new Vector2();
        baseRadius = RADIUS_FACTOR * Math.min(viewport.getWorldWidth(), viewport.getWorldHeight());
        radiusMultiplier = 1;
        time=-1;
        for(int i=0;i<25;i++)
        {
            arr[i]=-1;
        }


    }

    private void randomKick() {
        Random random = new Random();
        float angle = MathUtils.PI2 * random.nextFloat();
        velocity.x += KICK_VELOCITY * MathUtils.cos(angle);
        velocity.y += KICK_VELOCITY * MathUtils.sin(angle);
    }


    public void update(float delta) {

        // Growing and shrinking
        if (Gdx.input.isKeyPressed(Keys.Z)) {
            radiusMultiplier += delta * RADIUS_GROWTH_RATE;
        }
        if (Gdx.input.isKeyPressed(Keys.X)) {
            radiusMultiplier -= delta * RADIUS_GROWTH_RATE;
            radiusMultiplier = Math.max(radiusMultiplier, MIN_RADIUS_MULTIPLIER);
        }

        // Movement
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            velocity.x -= delta * ACCELERATION;

        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            velocity.x += delta * ACCELERATION;

        }
        if (Gdx.input.isKeyPressed(Keys.UP)) {
            velocity.y += delta * ACCELERATION;

        }
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            velocity.y -= delta * ACCELERATION;

        }

        velocity.clamp(0, MAX_SPEED);

        velocity.x -= delta * DRAG * velocity.x;
        velocity.y -= delta * DRAG * velocity.y;

        position.x += delta * velocity.x;
        position.y += delta * velocity.y;


        collideWithWalls(baseRadius * radiusMultiplier, viewport.getWorldWidth(), viewport.getWorldHeight());
    }

    private void collideWithWalls(float radius, float viewportWidth, float viewportHeight) {
        if (position.x - radius < 0) {
            position.x = radius;
            velocity.x = -velocity.x;
        }
        if (position.x + radius > viewportWidth) {
            position.x = viewportWidth - radius;
            velocity.x = -velocity.x;
        }
        if (position.y - radius < 0) {
            position.y = radius;
            velocity.y = -velocity.y;
        }
        if (position.y + radius > viewportHeight) {
            position.y = viewportHeight - radius;
            velocity.y = -velocity.y;
        }
    }

    public void render(ShapeRenderer renderer) {
        renderer.set(ShapeType.Filled);
        renderer.setColor(COLOR);
        Gdx.gl.glClearColor( 0,0,0,1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT  );
        //ShapeRenderer.begin(ShapeType.Filled);
        //ShapeRenderer.setColor(Color.GREEN);
        //shapeRenderer.rect(10, 10, 90, 90);
        int col=Color.toIntBits( 1,0,0,1 );



        for(int i=0;i<25;i++)
        {
            int a=i%5;
            int b=i/5;
            float k33=(float)i/50;
            float k44=(float)a/10;


            renderer.setColor( k44, k33,0,1 );

            renderer.rect( a*110,b*100,100,90 );
        }





        renderer.setColor( Color.GREEN );

        time++;


        //renderer.setColor( Color.RED );

        int k33=(int)d1;
        int k44=(int)e1;
        k44=k44/100;
        k44=k44*100;
        k33=k33/110;
        k33=k33*110;
        if(time>100)
        {
            int k334=k33/110;
            int k335=k44/100;
            int k336=(((k334+1)*(k335+1))-1);


            int i=k336;
            int a=i%5;
            int b=i/5;
            float k331=(float)i/50;
            float k441=(float)a/10;
          //  k331+=.4;


            renderer.setColor( k441, k331,0,1 );
            renderer.rect(k33,k44 ,100,90);



        }
        if(time>100)
        {
            k33=-4;
            k44=-4;

        }


        if((k33>=0)||(k44>=0)) {

            renderer.setColor( Color.BLACK );


            renderer.rect( k33, k44, 100, 90 );
        }





      //  ShapeRenderer.end();


        //renderer.circle(position.x, position.y, baseRadius * radiusMultiplier);
    }


    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Keys.SPACE) {
            randomKick();
        }

        if (keycode == Keys.R) {
            init();
        }

        return true;
    }

    /**
     * TODO: Check out what happens when a touch starts
     *
     * When a touch starts, we first need to translate the point that the user touched from screen
     * coordinates to world coordinates. Since the viewport handles the projection from world
     * coordinates to screen coordinates, it also has an unproject() method that does the opposite.
     *
     * Next we use the Vector2.dst() method to see if the distance between the touch and the
     * position of the ball is smaller than the ball's radius. If the touch is inside the radius,
     * then we start a flick, and save the world coordinates of the touch.
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 worldClick = viewport.unproject(new Vector2(screenX, screenY));
        //(worldClick.dst(position) < baseRadius * radiusMultiplier)
        if(true) {
            flicking = true;
            flickStart = worldClick;
        }
        return true;
    }

    /**
     * TODO: Check out what happens when a touch ends
     *
     * If we were in the process of flicking the ball, we calculate the vector between the start of
     * the flick and the end of the flick. Remember that the incoming position of the touch is in
     * screen coordinates, so we need to use the viewport to unproject that position into world
     * coordinates.
     *
     * Then we add that flick vector to the velocity of the ball, times some multiplier. Give it a
     * try!
     */

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (flicking) {
            flicking = false;
            Vector2 flickEnd = viewport.unproject(new Vector2(screenX, screenY));
            float d=flickStart.x;
            d1=d;
            float e=flickStart.y;
            e1=e;
            time=0;

            //Music music = Gdx.audio.newMusic(Gdx.files.internal("C:\\Users\\91900\\Desktop\\AI FOLDER/music_f.mp3"));
            String s="";
            int k12=(int)d1;
            k12=k12%17;
            k12+=1;
            s+=k12;
            s+=".mp3";


            Music music = Gdx.audio.newMusic(Gdx.files.internal(s));

            music.play();





            Vector2 flickVector = new Vector2(d,e);




        }

        return true;
    }
}
