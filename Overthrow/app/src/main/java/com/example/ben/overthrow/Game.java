package com.example.ben.overthrow;

import android.graphics.Point;

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
    private int scores[];
    private boolean gameFinished;
    private Point selectedTile;

    public Game(int size, int players, int timer) {
        this.size = size;
        this.players = players;
        this.timer = timer;
        this.scores = new int[]{1, 1, 1, 1, 1};
        gameFinished = false;
        board = new int[size][size];
        playerTurn = Utils.random(1, players);
        board[0][0] = nextPlayer();
        board[0][size - 1] = nextPlayer();
        board[size - 1][size - 1] = nextPlayer();
        board[size - 1][0] = nextPlayer();
    }

    public boolean hasFinished() {
        return gameFinished;
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

    public int getPlayers() { return players; }

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

    public int nextPlayer(boolean checkForMoves) {
        playerTurn++;
        if (playerTurn > players)
            playerTurn = 1;
        while (scores[playerTurn - 1] <= 0 || !playerCanMove(playerTurn) && checkForMoves) {
            if (!playerCanMove(playerTurn) && checkForMoves && players == 2)
                gameFinished = true;
            playerTurn++;
            if (playerTurn > players)
                playerTurn = 1;
            if (getEmptyTile() == null)
                break;
        }
        return playerTurn;
    }

    public int getPreviousPlayer() {
        int previous = playerTurn - 1;
        if (previous == 0)
            previous = players;
        return previous;
    }

    public int nextPlayer() {
        return nextPlayer(false);
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
        for (int x = -1 * radius; x < (1 + radius); x++) {
            for (int y = -1 * radius; y < (1 + radius); y++) {
                int checkX = selected.x + x;
                int checkY = selected.y + y;
                if ((checkX < 0 || checkX >= size || checkY < 0 || checkY >= size) || (x == 0 && y == 0))
                    continue;
                if (board[checkY][checkX] == 0) {
                    moves.add(new Point(checkY, checkX));
                }
            }
        }
        Point checks[] = {new Point(-2, 2), new Point(0, 2), new Point(2, 2), new Point(2, 0), new Point(2, -2), new Point(0, -2), new Point(-2, -2), new Point(-2, 0)};
        for (int i = 0; i < checks.length; i++) {
            int checkX = selected.y + checks[i].y;
            int checkY = selected.x + checks[i].x;
            if (checkX < 0 || checkX >= size || checkY < 0 || checkY >= size)
                continue;
            if (board[checkX][checkY] == 0)
                moves.add(new Point(checkX, checkY));
        }
        return moves;
    }

    public void setPossibleMoves(int x, int y) {
        List<Point> possibles = getValidMoves(new Point(x, y));
        for (Point tile : possibles) {
            board[tile.x][tile.y] = playerTurn * -1;
        }
    }

    public void clearPossibles() {
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++)
                if (board[y][x] < 0)
                    board[y][x] = 0;
    }

    public Point getEmptyTile() {
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++)
                if (board[y][x] == 0)
                    return new Point(x, y);
        return null;
    }

    public int getDistance(Point originTile, Point newTile) {
        int radius = 1;
        for (int x = -1 * radius; x < (1 + radius); x++) {
            for (int y = -1 * radius; y < (1 + radius); y++) {
                int checkX = originTile.x + x;
                int checkY = originTile.y + y;
                if ((checkX < 0 || checkX >= size || checkY < 0 || checkY >= size) || (x == 0 && y == 0))
                    continue;
                if (checkY == newTile.y && checkX == newTile.x) {
                    return 1;
                }
            }
        }
        return 2;
    }

    public void splat(Point tile) {
        int radius = 1;
        for (int x = -1 * radius; x < (1 + radius); x++) {
            for (int y = -1 * radius; y < (1 + radius); y++) {
                int checkX = tile.x + x;
                int checkY = tile.y + y;
                if ((checkX < 0 || checkX >= size || checkY < 0 || checkY >= size) || (x == 0 && y == 0))
                    continue;
                if (board[checkY][checkX] > 0 && board[checkY][checkX] != playerTurn)
                    board[checkY][checkX] = playerTurn;
            }
        }
    }

    public int getWinner() {
        int topPlayer = -1;
        int topScore = -1;
        for (int i = 0; i < players; i++) {
            if (scores[i] > topScore) {
                topScore = scores[i];
                topPlayer = i + 1;
            }
        }
        return topPlayer;
    }

    public int[] getScores() {
        scores = new int[players + 1];
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++)
                if (board[y][x] > 0)
                    scores[board[y][x] - 1]++;
                else if (board[y][x] == 0)
                    scores[players]++;
        if (scores[players] == 0)
            gameFinished = true;
        else if (players == 2 && (scores[0] == 0 || scores[1] == 0))
            gameFinished = true;
        else if (players == 4) {
            int count = 0;
            for (int i = 0; i < 4; i++) {
                if (scores[i] == 0)
                    count++;
            }
            if (count == 3)
                gameFinished = true;
        }
        return scores;
    }

    public boolean playerCanMove(int player) {
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++)
                if (board[y][x] == player)
                    if (getValidMoves(new Point(x, y)).size() > 0)
                        return true;
        return false;
    }

}
