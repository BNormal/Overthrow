package com.example.ben.overthrow;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 7/9/2017.
 */

public class Game {

    private int board[][];
    private int size;
    private int playerTurn;
    private int players;
    private int timer;
    private Point selectedTile;

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

    public void setSelected(int x, int y) {
        selectedTile = new Point(x, y);
    }

    public void setSelected(Point tile) {
        selectedTile = tile;
    }

    public Point getSelected() {
        return selectedTile;
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

    public boolean isValidSelection(int x, int y) {
        return board[y][x] == playerTurn || (board[y][x] > 4 && board[y][x] - 4 == playerTurn);
    }

    public List<Point> getValidMoves(Point selected) {
        List<Point> moves = new ArrayList<Point>();
        int radius = 1;
        Log.d("O", "X: " + selected.x + ", Y: " + selected.y);
        for (int x = -1 * radius; x < (1 + radius); x++) {
            for (int y = -1 * radius; y < (1 + radius); y++) {
                int checkX = selected.x + x;
                int checkY = selected.y + y;
                if ((checkX < 0 || checkX >= size || checkY < 0 || checkY >= size))
                    continue;
                Log.w("new", "X: " + checkX + ", Y: " + checkY);
                if (board[checkX][checkY] == 0)
                    moves.add(new Point(checkX, checkY));
            }
        }
        Point checks[] = {new Point(-2, 2), new Point(0, 2), new Point(2, 2), new Point(2, 0), new Point(2, -2), new Point(0, -2), new Point(-2, -2), new Point(-2, 0)};
        for (int i = 0; i < checks.length; i++) {
            int checkX = selected.x + checks[i].x;
            int checkY = selected.y + checks[i].y;
            if (checkX < 0 || checkX >= size || checkY < 0 || checkY >= size)
                continue;
            if (board[checkX][checkY] == 0)
                moves.add(new Point(checkX, checkY));
        }
        return moves;
    }

    public void clearPossibles() {
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++)
                if (board[y][x] < 0)
                    board[y][x] = 0;
    }

    public void setPossibleMoves(int x, int y) {
        List<Point> possibles = getValidMoves(new Point(x, y));
        for (Point tile : possibles) {
            if (tile.x != x && tile.y != y)
                board[tile.y][tile.x] = playerTurn * -1;
        }
    }
}
