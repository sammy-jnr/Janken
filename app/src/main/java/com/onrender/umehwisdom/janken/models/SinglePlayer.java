package com.onrender.umehwisdom.janken.models;

import com.onrender.umehwisdom.janken.interfaces.Game;

public class SinglePlayer implements Game {

    private String mode;
    private int noOfGames;
    private int points = 0;
    private int opponentPoints = 0;
    private int round = 1;




    public SinglePlayer( String mode, int noOfGames) {
        this.mode = mode;
        this.noOfGames = noOfGames;
    }

    @Override
    public String getType() {
        return "singleplayer";
    }

    @Override
    public String getMode() {
        return mode;
    }
    @Override
    public void setMode(String mode) {
        this.mode = mode;
    }
    @Override
    public int getNoOfGames() {
        return noOfGames;
    }
    @Override
    public void setNoOfGames(int noOfGames) {
        this.noOfGames = noOfGames;
    }
    @Override
    public int getPoints() {
        return points;
    }
    @Override
    public void increasePoints() {
        this.points += 1;
    }
    @Override
    public int getOpponentPoints() {
        return opponentPoints;
    }
    @Override
    public void increaseOpponentPoints() {
        opponentPoints +=1;
    }
    @Override
    public int getRound() {
        return round;
    }
    @Override
    public void increaseRound() {
        this.round += 1;
    }
}
