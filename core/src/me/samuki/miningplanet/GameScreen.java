package me.samuki.miningplanet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Random;

class GameScreen implements Screen {
    private GameClass game;

    private static final int BLOCKS_HORIZONTAL = 100;
    private static final int BLOCKS_VERTICAL = 100;

    private Block[][] block = new Block[BLOCKS_HORIZONTAL][BLOCKS_VERTICAL];
    private static int mapX;
    private static int mapY;
    private static int squareSide;
    private static int screenVelocityX;
    private static int screenVelocityY;
    private int mapWidth, mapHeight;
    //private Texture[] blockSprite;

    private Stage stage;
    private FitViewport viewport;
    private Actor screenActor;

    GameScreen (GameClass game) {
        this.game = game;
    }

    @Override
    public void show() {
        //VIEWPORT & STAGE
        viewport = new FitViewport(GameClass.SCREEN_WIDTH, GameClass.SCREEN_HEIGHT, game.camera);
        viewport.setScaling(Scaling.stretch);

        stage = new Stage(viewport,game.batch);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new MyInputProcessor() {
            @Override
            public boolean scrolled(int amount) {
                    if (squareSide <= 10&& amount == -1)
                        squareSide = 10;
                    else if (squareSide >= 100 && amount == 1)
                        squareSide = 100;
                    else
                        squareSide += amount * 5;
                screenActor.setBounds(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
                return true;
            }
        });
        Gdx.input.setInputProcessor(multiplexer);


        squareSide = 100;
        mapX = squareSide;
        mapY = squareSide;
        screenVelocityX = 0;
        screenVelocityY = 0;

        screenActor = new Actor();
        screenActor.setBounds(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        screenActor.setPosition(0, 0);
        screenActor.addListener(new ActorGestureListener() {
            int xWas;
            int yWas;
            boolean wasDragged;
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                xWas = GameScreen.mapX;
                screenVelocityX = 0;
                yWas = GameScreen.mapY;
                screenVelocityY = 0;
                wasDragged = false;
            }
            @Override
            public void fling (InputEvent event, float velocityX, float velocityY, int button) {
                screenVelocityX += velocityX;
                screenVelocityY += velocityY;
            }
            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                GameScreen.mapX += deltaX;
                GameScreen.mapY += deltaY;
                if(xWas+20 >= GameScreen.mapX || xWas-20 <= GameScreen.mapX || yWas+20 >= GameScreen.mapY || yWas+20 <= GameScreen.mapY)
                    wasDragged = true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                int blockX = (int)(x-GameScreen.mapX)/squareSide-1;
                int blockY = (int)(y-GameScreen.mapY)/squareSide-1;
                if(!wasDragged) {
                    if(        blockX > -1
                            && blockX < BLOCKS_HORIZONTAL
                            && blockY > -1
                            && blockY < BLOCKS_VERTICAL)
                            block[blockX][blockY].hpDown(block);
                }
            }
            @Override
            public void zoom(InputEvent event, float initialDistance, float distance) {
                if(squareSide >= 10 && squareSide <= 100)
                    squareSide +=  (int)initialDistance-distance;
                else if(squareSide < 10)
                    squareSide = 10;
                else if(squareSide > 100)
                    squareSide = 100;            }
        });
        stage.addActor(screenActor);

        setMap();
        RobotDig robot = new RobotDig(0, 0);
        robot.move(block, 15, 15);

        //blockSprite = new Texture[4];
        //blockSprite[0] = new Texture("blank.jpg");
        //for(int i = 1; i < blockSprite.length; i++) {
        //    blockSprite[i] = new Texture("type_"+i+".jpg");
        //}

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.end();

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        stopMapMoving();
        drawBlocks();
        game.shapeRenderer.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1/60f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private void setMap() {
        mapWidth = BLOCKS_HORIZONTAL *squareSide-((int)viewport.getWorldWidth()-squareSide*2);
        mapHeight = BLOCKS_VERTICAL *squareSide-((int)viewport.getWorldHeight()-squareSide*2);

        for(int i = 0; i < BLOCKS_HORIZONTAL; i++) {
            for(int j = 0; j < BLOCKS_VERTICAL; j++) {
                Random random = new Random();
                int type = random.nextInt(100);
                if(type < 80)
                    block[i][j] = new Block(1);
                else if (type >= 80 && type < 90)
                    block[i][j] = new Block(2);
                else if (type >= 90)
                    block[i][j] = new Block(3);
                block[i][j].x = GameScreen.mapX+(i*squareSide)+squareSide;
                block[i][j].y = GameScreen.mapY+(j*squareSide)+squareSide;
                block[i][j].setNeighbours(i, j);
            }
        }
        block[0][0].inFog = false;
    }

    private void drawBlocks() {
        GameScreen.mapX += Gdx.graphics.getDeltaTime()*screenVelocityX;
        GameScreen.mapY += Gdx.graphics.getDeltaTime()*screenVelocityY;

        int startingBlockI = -GameScreen.mapX / squareSide;
        int endingBlockI = (int)viewport.getWorldWidth()/squareSide+startingBlockI+1;
        if(startingBlockI > 0)
            startingBlockI = startingBlockI-1;
        else if(startingBlockI < 0)
            startingBlockI = 0;
        if(endingBlockI > BLOCKS_HORIZONTAL -2)
            endingBlockI = BLOCKS_HORIZONTAL;
        int startingBlockJ = -GameScreen.mapY/squareSide;
        int endingBlockJ = (int)viewport.getWorldWidth()/squareSide+startingBlockJ+1;
        if(startingBlockJ > 0)
            startingBlockJ = startingBlockJ-1;
        else if(startingBlockJ < 0)
            startingBlockJ = 0;
        if(endingBlockJ > BLOCKS_VERTICAL -2)
            endingBlockJ = BLOCKS_VERTICAL;

        for(int i = startingBlockI; i < endingBlockI; i++) {
            for(int j = startingBlockJ; j < endingBlockJ; j++) {
                game.shapeRenderer.setColor(block[i][j].blockColor);
                if(block[i][j].inFog)
                    game.shapeRenderer.setColor(Color.GRAY);
                game.shapeRenderer.rect(GameScreen.mapX+(i*squareSide)+squareSide, GameScreen.mapY+(j*squareSide)+squareSide, squareSide, squareSide);
                //game.batch.draw(blockSprite[block[i][j].blockType], mapX+(i*squareSide)+squareSide, mapY+(j*squareSide)+squareSide, squareSide, squareSide);
            }
        }
    }

    private void stopMapMoving() {
        mapWidth = BLOCKS_HORIZONTAL *squareSide-((int)viewport.getWorldWidth()-squareSide*2);
        mapHeight = BLOCKS_VERTICAL *squareSide-((int)viewport.getWorldHeight()-squareSide*2);
        if(mapX <= -mapWidth) {
            screenVelocityX = 0;
            mapX = -mapWidth;
        }
        if(mapX >= 0) {
            screenVelocityX = 0;
            mapX = 0;
        }
        if(mapY <= -mapHeight) {
            screenVelocityY = 0;
            mapY = -mapHeight;
        }
        if(mapY >= 0) {
            screenVelocityY = 0;
            mapY = 0;
        }
    }

}
