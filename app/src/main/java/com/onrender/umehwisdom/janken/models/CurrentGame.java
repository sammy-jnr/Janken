package com.onrender.umehwisdom.janken.models;

public class CurrentGame {
    private String opponent;
    private String mode;
    private int noOfGames;
    private int points = 0;
    private int round = 1;
    private int drawCount = 0;




    public CurrentGame(String opponent, String mode, int noOfGames) {
        this.opponent = opponent;
        this.mode = mode;
        this.noOfGames = noOfGames;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getNoOfGames() {
        return noOfGames;
    }

    public void setNoOfGames(int noOfGames) {
        this.noOfGames = noOfGames;
    }

    public int getPoints() {
        return points;
    }

    public void increasePoints() {
        this.points += 1;
    }

    public int getRound() {
        return round;
    }

    public void increaseRound() {
        this.round += 1;
    }

    public int getDrawCount() {
        return drawCount;
    }

    public void increaseDrawCount() {
        this.drawCount += 1;
    }
}
