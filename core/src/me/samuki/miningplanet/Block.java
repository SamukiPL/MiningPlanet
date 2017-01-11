package me.samuki.miningplanet;

import com.badlogic.gdx.utils.Array;

import java.awt.Point;
import java.util.Iterator;

class Block extends BlockTypes {
    int blockType;
    int x;
    int y;
    boolean inFog;
    Array<Point> neighbours;

    Block(final int blockType) {
        this.blockType = blockType;
        inFog = true;

        if(blockType == 1)
            dirt();
        else if(blockType == 2)
            copper();
        else if(blockType == 3)
            iron();
    }

    void setNeighbours(int x, int y) {
        int index = 0;

        neighbours = new Array<Point>();
        for(int i = x-3; i <= x+3; i++) {
            if(i >= 0)
                for(int j = y-3; j <= y+3; j++) {
                    if(j >= 0) {
                        neighbours.add(new Point());
                        neighbours.get(index).setLocation(i, j);
                        index++;
                    }
                }
        }

    }



    void hpDown(Block[][] blocks) {
        if(!inFog) {
            hp--;
            if (hp <= 0) {
                blockType = 0;
                blank();
                disableFog(blocks);
            }
        }
    }

    private void disableFog(Block[][] blocks){
        for (Point thatPoint : neighbours) {
            blocks[(int) thatPoint.getX()][(int) thatPoint.getY()].inFog = false;
        }
    }

}
