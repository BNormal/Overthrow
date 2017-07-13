package com.example.ben.overthrow;

/**
 * Created by Ben on 7/13/2017.
 */

public class Player {
    int id;
    int score;

    public Player(int id, int score) {
        this.id = id;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }
}
