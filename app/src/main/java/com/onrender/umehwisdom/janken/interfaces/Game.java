package com.onrender.umehwisdom.janken.interfaces;

public interface Game {
    public String getType();
    public String getMode();
    public void setMode(String mode);
    public int getNoOfGames();
    public void setNoOfGames(int noOfGames);
    public int getPoints();
    public void increasePoints();
    public int getOpponentPoints();
    public void increaseOpponentPoints();
    public int getRound();
    public void increaseRound();
}
