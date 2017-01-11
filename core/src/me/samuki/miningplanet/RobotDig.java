package me.samuki.miningplanet;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

import java.awt.Point;
import java.util.Iterator;

class RobotDig extends RobotBase {
    int blockTypeSearch;

    RobotDig(int startX, int startY) {
        super(startX, startY);
        blockTypeSearch = 1;
    }

    void move(final Block[][] blocks, final int x, final int y) {
        final Array<Point> finalPath = new Array<Point>();
        final Array<Point> path = new Array<Point>();
        final Array<Point> cantGo = new Array<Point>();

        int index = 0;
        int cantGoIndex = 0;

        boolean found = false;
        int tmpPosX = posX;
        int tmpPosY = posY;
        boolean wasUpDown = false;
        do {
            System.out.println(tmpPosX+"    "+tmpPosY);
            if(tmpPosX == x && tmpPosY == y) {
                found = true;
                finalPath.addAll(path);
                continue;
            }
            int directionX;
            if(tmpPosX < x)
                directionX = 1;
            else if(tmpPosX > x)
                directionX = -1;
            else
                directionX = 0;

            int directionY;
            if(tmpPosY < y)
                directionY = 1;
            else if(tmpPosY > y)
                directionY = -1;
            else
                directionY = 0;

            boolean upDown = false;
            try {
                if(directionX != 0)
                    upDown = blocks[tmpPosX + directionX][tmpPosY].canPass;
            } catch (ArrayIndexOutOfBoundsException e) {
                upDown = false;
            }

            boolean rightLeft = false;
            try {
                if(directionY != 0)
                    rightLeft = blocks[tmpPosX][tmpPosY + directionY].canPass;
            } catch (ArrayIndexOutOfBoundsException e) {
                rightLeft = false;
            }

            for(int i = 0; i < cantGoIndex; i++) {
                if(posX + directionX == cantGo.get(i).getX())
                    upDown = false;
                if(posY + directionY == cantGo.get(i).getY()) {
                    rightLeft = false;
                }
            }

            if(upDown) {
                path.add(new Point(tmpPosX + directionX, tmpPosY));
                index++;
                tmpPosX += directionX;
                wasUpDown = true;
                continue;
            }
            else if(rightLeft) {
                path.add(new Point(tmpPosX, tmpPosY + directionY));
                index++;
                tmpPosY += directionY;
                wasUpDown = false;
                continue;
            }
            else if(!upDown && !rightLeft && index > 0) {
                cantGo.add(new Point(tmpPosX + directionX, tmpPosY));
                cantGo.add(new Point(tmpPosX, tmpPosY + directionY));
                cantGoIndex+=2;
                if(wasUpDown && blocks[tmpPosX][tmpPosY - directionY].canPass) {
                    path.add(new Point(tmpPosX, tmpPosY - directionY));
                    tmpPosY -= directionY;
                    index++;
                    wasUpDown = false;
                }
                else if(!wasUpDown && blocks[tmpPosX - directionX][tmpPosY].canPass) {
                    path.add(new Point(tmpPosX - directionX, tmpPosY));
                    tmpPosX -= directionX;
                    index++;
                    wasUpDown = true;
                }
                else {
                    index--;
                    tmpPosX = (int) path.get(index).getX();
                    tmpPosY = (int) path.get(index).getY();
                    path.removeIndex(index);
                    continue;
                }
            }
            else if(!upDown && !rightLeft && index <= 0) {
                System.out.println("NIE MA DROGI :(");
            }
            /*
            if (tmpPosX == x && tmpPosY == y) {
                found = true;
                continue;
            }

            int directionX;
            if(tmpPosX < x)
                directionX = 1;
            else if(tmpPosX > x)
                directionX = -1;
            else
                directionX = 0;

            int directionY;
            if(tmpPosY < y)
                directionY = 1;
            else if(tmpPosY > y)
                directionY = -1;
            else
                directionY = 0;
            if(!upDown && directionX != 0) {
                finalPath.add(new Point(tmpPosX+directionX, tmpPosY));
                upDown = true;
                tmpPosX += directionX;
            }
            else {
                finalPath.add(new Point(tmpPosX, tmpPosY + directionY));
                upDown = false;
                tmpPosY += directionY;
            }*/
        }
        while(!found);
        final Timer timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                System.out.println(posX+"    "+posY);
                Iterator<Point> pathIter = finalPath.iterator();
                if(pathIter.hasNext()) {
                    Point moveTo = pathIter.next();
                    blocks[(int)moveTo.getX()][(int)moveTo.getY()].inFog = false;
                    posX = (int)moveTo.getX();
                    posY = (int)moveTo.getY();
                    pathIter.remove();
                }
                else
                    timer.clear();

            }
        }, 0.5f, 0.5f);
    }

    void searchAndDig() {

    }

    void changeSearch(int changeType){
        blockTypeSearch = changeType;
    }
}
