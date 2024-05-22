package com.onrender.umehwisdom.janken.models;

import com.onrender.umehwisdom.janken.interfaces.Game;

public class Multiplayer implements Game {


    public boolean gameAccepted = false;



    public int noOfGames;
    public String mode;
    public String creatorUsername;
    public String opponentUsername;

    public Multiplayer(String mode,int noOfGames,String creatorUsername) {
        this.noOfGames = noOfGames;
        this.mode = mode;
        this.creatorUsername = creatorUsername;
    }
    public Multiplayer() {}

    public int currentRound = 1;
    public int creatorPoints = 0;
    public int opponentPoints = 0;
    public int drawCount = 0;
    public String creatorOption = "";
    public String opponentOption = "";
    public String winner = "";



    public boolean isGameAccepted() {
        return gameAccepted;
    }

    public void setGameAccepted(boolean gameAccepted) {
        this.gameAccepted = gameAccepted;
    }


    public String getCreatorOption() {
        return creatorOption;
    }

    public void setCreatorOption(String creatorOption) {
        this.creatorOption = creatorOption;
    }

    public void setOpponentOption(String opponentOption) {
        this.opponentOption = opponentOption;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public String getOpponentUsername() {
        return opponentUsername;
    }

    public void setOpponentUsername(String opponentUsername) {
        this.opponentUsername = opponentUsername;
    }

    public int getDrawCount() {
        return drawCount;
    }

    public void setDrawCount(int drawCount) {
        this.drawCount = drawCount;
    }

    @Override
    public String getType() {
        return "multiplayer";
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
        return creatorPoints;
    }

    @Override
    public void increasePoints() {
        this.creatorPoints +=1;
    }
    public void setCreatorPoints(int points) {
        this.creatorPoints = points;
    }
    public void setOpponentPoints(int points) {
        this.opponentPoints = points;
    }

    @Override
    public int getOpponentPoints() {
        return opponentPoints;
    }

    @Override
    public void increaseOpponentPoints() {
        this.opponentPoints+=1;
    }

    @Override
    public int getRound() {
        return currentRound;
    }

    @Override
    public void increaseRound() {
        currentRound+=1;
    }
}
