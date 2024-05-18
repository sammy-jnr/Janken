package com.onrender.umehwisdom.janken.models;

import androidx.lifecycle.ViewModel;

import java.util.Arrays;
import java.util.Random;

public class SharedViewModel extends ViewModel {
    private String gameMode = "RPS";

    private int numberOfGames = 1;

    private String opponent = "computer";

    private CurrentGame currentGame;

    public CurrentGame getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(CurrentGame currentGame) {
        this.currentGame = currentGame;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }

    public void setNumberOfGames(int numberOfGames) {
        this.numberOfGames = numberOfGames;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getComputerOption(){
        String[] options = {"rock", "paper", "scissors"};
        String[] options5 = {"rock", "paper", "scissors","lizard","spock"};
        if(currentGame.getMode().equals("RPSLS"))options = options5;
        Random randomNum  = new Random();
        return options[randomNum.nextInt(options.length)];
    }
    public String determineWinner(String yourChoice, String opponentChoice){
        String[] rockArray = {"scissors", "lizard"};
        String[] paperArray = {"rock", "spock"};
        String[] scissorsArray = {"paper", "lizard"};
        String[] lizardArray = {"paper", "spock"};
        String[] spockArray = {"scissors", "rock"};

        switch (yourChoice) {
            case "rock":
                if(opponentChoice.equals("rock"))return "draw";
                if(Arrays.asList(rockArray).contains(opponentChoice))return "won";
                if(!Arrays.asList(rockArray).contains(opponentChoice))return "lost";
                break;
            case "paper":
                if(opponentChoice.equals("paper"))return "draw";
                if(Arrays.asList(paperArray).contains(opponentChoice))return "won";
                if(!Arrays.asList(paperArray).contains(opponentChoice))return "lost";
                break;
            case "scissors":
                if(opponentChoice.equals("scissors"))return "draw";
                if(Arrays.asList(scissorsArray).contains(opponentChoice))return "won";
                if(!Arrays.asList(scissorsArray).contains(opponentChoice))return "lost";
                break;
            case "lizard":
                if(opponentChoice.equals("lizard"))return "draw";
                if(Arrays.asList(lizardArray).contains(opponentChoice))return "won";
                if(!Arrays.asList(lizardArray).contains(opponentChoice))return "lost";

                break;
            case "spock":
                if(opponentChoice.equals("spock"))return "draw";
                if(Arrays.asList(spockArray).contains(opponentChoice))return "won";
                if(!Arrays.asList(spockArray).contains(opponentChoice))return "lost";
                break;
            default:
                break;
        }
        return "Invalid";
    }
}
