package me.samuki.miningplanet;

import com.badlogic.gdx.graphics.Color;

class BlockTypes {
    int hp;
    Color blockColor;
    boolean canPass;

    void blank() {
        hp = 0;
        blockColor = Color.BLACK;
        canPass = true;
    }

    void dirt() {
        hp = 1;
        blockColor = Color.BROWN;
        canPass = true;
    }

    void copper() {
        hp = 5;
        blockColor = Color.SALMON;
        canPass = false;
    }

    void iron() {
        hp = 10;
        blockColor = Color.SKY;
        canPass = false;
    }
}
