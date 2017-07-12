package com.example.ben.overthrow;

/**
 * Created by Ben on 7/9/2017.
 */

public class Game {

    private int board[][];
    private int size;
    private int playerTurn;
    private int players;
    private int timer;

    public Game(int size, int players, int timer) {
        this.size = size;
        this.players = players;
        this.timer = timer;
        board = new int[size][size];
        playerTurn = Utils.random(1, players);
        board[0][0] = nextPlayer();
        board[0][size - 1] = nextPlayer();
        board[size - 1][size - 1] = nextPlayer();
        board[size - 1][0] = nextPlayer();
    }

    public int getTimer() { return timer; }

    public int getSize() {
        return size;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int x, int y, int value) {
        board[y][x] = value;
    }

    public boolean validPlayerChip(int x, int y) {
        return board[y][x] == playerTurn;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int turn) {
        playerTurn = turn;
    }

    public int nextPlayer() {
        playerTurn++;
        if (playerTurn > players)
            playerTurn = 1;
        return playerTurn;
    }

    public String getCurrentPlayerColor() {
        switch (playerTurn) {
            case 1: return "#ED1C24";//red
            case 2: return "#00A2E8";//blue
            case 3: return "#22B14C";//green
            case 4: return "#A349A4";//purple
        }
        return "#ffffff";
    }
}
