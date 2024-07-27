package com.blackjack.game.aggregate;

import java.util.ArrayList;

public class GameResponseObject {
    private ArrayList<Game> gameList;
    private int checkValueInt;
    private boolean checkValueBoolean;

    public GameResponseObject() {
    }

    public GameResponseObject(ArrayList<Game> gameList) {
        this.gameList = gameList;
    }

    public GameResponseObject(ArrayList<Game> gameList, int checkValueInt) {
        this.gameList = gameList;
        this.checkValueInt = checkValueInt;
    }

    public GameResponseObject(ArrayList<Game> gameList, boolean checkValueBoolean) {
        this.gameList = gameList;
        this.checkValueBoolean = checkValueBoolean;
    }

    public ArrayList<Game> getGameList() {
        return gameList;
    }

    public void setGameList(ArrayList<Game> gameList) {
        this.gameList = gameList;
    }

    public int getCheckValueInt() {
        return checkValueInt;
    }

    public void setCheckValueInt(int checkValueInt) {
        this.checkValueInt = checkValueInt;
    }

    public boolean isCheckValueBoolean() {
        return checkValueBoolean;
    }

    public void setCheckValueBoolean(boolean checkValueBoolean) {
        this.checkValueBoolean = checkValueBoolean;
    }
}
