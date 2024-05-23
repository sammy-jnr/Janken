package com.onrender.umehwisdom.janken.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onrender.umehwisdom.janken.R;
import com.onrender.umehwisdom.janken.firebase.FirebaseDB;
import com.onrender.umehwisdom.janken.models.Multiplayer;
import com.onrender.umehwisdom.janken.models.SharedViewModel;
import com.onrender.umehwisdom.janken.models.SinglePlayer;


public class PlayFragment extends Fragment {


    private SharedViewModel sharedViewModel;

    private  ConstraintLayout _2ChipsLayout, _3ChipsLayout, _5ChipsLayout, playGameContainer;

    private ImageView yourOptionDisplay, opponentOptionDisplay;

    private TextView nextRoundButton, displayGamesPlayed, displayPoints, displayOpponentsPoints;

    private FirebaseDB firebaseDB;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        firebaseDB = FirebaseDB.getInstance();



        playGameContainer = view.findViewById(R.id.play_game_container);
        if(sharedViewModel.getCurrentGame() == null){
            playGameContainer.setVisibility(View.GONE);
        }


        yourOptionDisplay = view.findViewById(R.id.your_option_display);
        opponentOptionDisplay = view.findViewById(R.id.opponent_option_display);
        TextView theHouseText = view.findViewById(R.id.the_house_text);
        nextRoundButton = view.findViewById(R.id.next_round_button);
        displayGamesPlayed = view.findViewById(R.id.display_games_played);
        displayPoints = view.findViewById(R.id.display_points);
        displayOpponentsPoints = view.findViewById(R.id.display_opponents_points);
        TextView displayGameMode = view.findViewById(R.id.display_game_mode);
        _2ChipsLayout = view.findViewById(R.id._2_chips_layout);
        _3ChipsLayout = view.findViewById(R.id._3_chips_layout);
        _5ChipsLayout = view.findViewById(R.id._5_chips_layout);



        if(sharedViewModel.getCurrentGame() != null){
            displayGameMode.setText(sharedViewModel.getCurrentGame().getMode());
            displayGamesPlayed.setText(new StringBuilder("1/" + sharedViewModel.getCurrentGame().getNoOfGames()));

            if(sharedViewModel.getCurrentGame().getMode().equals("RPS")){
                _3ChipsLayout.setVisibility(View.VISIBLE);
            } else if (sharedViewModel.getCurrentGame().getMode().equals("RPSLS")) {
                _5ChipsLayout.setVisibility(View.VISIBLE);
            }
        }



        ImageView rockButtonBig = view.findViewById(R.id.rock_button_big);
        ImageView scissorsButtonBig = view.findViewById(R.id.scissors_button_big);
        ImageView paperButtonBig = view.findViewById(R.id.paper_button_big);

        rockButtonBig.setOnClickListener(v->{
            optionSelected(("rock"));
        });
        scissorsButtonBig.setOnClickListener(v->{
            optionSelected(("scissors"));
        });
        paperButtonBig.setOnClickListener(v->{
            optionSelected(("paper"));
        });


        ImageView rockButtonSmall = view.findViewById(R.id.rock_button_small);
        ImageView scissorsButtonSmall = view.findViewById(R.id.scissors_button_small);
        ImageView paperButtonSmall = view.findViewById(R.id.paper_button_small);
        ImageView lizardButtonSmall = view.findViewById(R.id.lizard_button_small);
        ImageView spockButtonSmall = view.findViewById(R.id.spock_button_small);


        rockButtonSmall.setOnClickListener(v->{
            optionSelected(("rock"));
        });
        scissorsButtonSmall.setOnClickListener(v->{
            optionSelected(("scissors"));
        });
        paperButtonSmall.setOnClickListener(v->{
            optionSelected(("paper"));
        });
        lizardButtonSmall.setOnClickListener(v->{
            optionSelected(("lizard"));
        });
        spockButtonSmall.setOnClickListener(v->{
            optionSelected(("spock"));
        });



        nextRoundButton.setOnClickListener(v -> {
            if(sharedViewModel.getCurrentGame() == null)return;
            if(sharedViewModel.getCurrentGame().getType().equals("singleplayer")){
                _2ChipsLayout.setVisibility(View.GONE);
                nextRoundButton.setVisibility(View.GONE);
                opponentOptionDisplay.setVisibility(View.GONE);
                sharedViewModel.getCurrentGame().increaseRound();
                displayGamesPlayed.setText(new StringBuilder(sharedViewModel.getCurrentGame().getRound() + "/" + sharedViewModel.getCurrentGame().getNoOfGames()));
                if(sharedViewModel.getCurrentGame().getMode().equals("RPS")){
                    _3ChipsLayout.setVisibility(View.VISIBLE);
                }else{
                    _5ChipsLayout.setVisibility(View.VISIBLE);
                }
            }else{
                multiplayerNextRound();
            }
        });

        view.findViewById(R.id.play_back_button).setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
        });



        // observer for multiplayer game
        MutableLiveData<Multiplayer> multiplayerGameDetails = FirebaseDB.getInstance().getMultiplayerGameDetails();
        multiplayerGameDetails.observe(requireActivity(), multiplayer -> {
            sharedViewModel.setCurrentGame(multiplayer);
            setMultiplayerUI((Multiplayer) sharedViewModel.getCurrentGame());
            Toast.makeText(requireContext(), "callback called", Toast.LENGTH_SHORT).show();
        });

        // When CREATOR OPTION CHANGE OBSERVER
        MutableLiveData<String> creatorOption = FirebaseDB.getInstance().getCreatorOption();
        creatorOption.observe(requireActivity(), option -> {
            if(sharedViewModel.getCurrentGame() == null || option.isEmpty())return;

            Multiplayer currentGame = (Multiplayer) sharedViewModel.getCurrentGame();
            if(nextRoundButton.getVisibility() == View.VISIBLE) multiplayerNextRound();

            ((Multiplayer) sharedViewModel.getCurrentGame()).setCreatorOption(option);
            if(currentGame.creatorUsername.equals(sharedViewModel.getUsername())){
                setBackgroundYourOption(option);
            }else {
                setBackgroundOpponentOption(option);
            }
        });

        // When OPPONENT OPTION CHANGE OBSERVER
        MutableLiveData<String> opponentOption = FirebaseDB.getInstance().getOpponentOption();
        opponentOption.observe(requireActivity(), option -> {
            if(sharedViewModel.getCurrentGame() == null || option.isEmpty())return;

            Multiplayer currentGame = (Multiplayer) sharedViewModel.getCurrentGame();
            if(nextRoundButton.getVisibility() == View.VISIBLE){
                multiplayerNextRound();
            }

            ((Multiplayer) sharedViewModel.getCurrentGame()).setOpponentOption(option);
            if(currentGame.creatorUsername.equals(sharedViewModel.getUsername())){
                setBackgroundOpponentOption(option);
            }else {
                setBackgroundYourOption(option);
            }
        });

        // When CREATOR POINTS INCREASE OBSERVER
        MutableLiveData<Integer> creatorPoints = FirebaseDB.getInstance().getCreatorPoints();
        creatorPoints.observe(requireActivity(), points -> {
            if(sharedViewModel.getCurrentGame() == null)return;
            Multiplayer currentGame = (Multiplayer) sharedViewModel.getCurrentGame();
            if(points==0  && currentGame.opponentPoints == 0)return;

            ((Multiplayer) sharedViewModel.getCurrentGame()).setCreatorPoints(points);
            if(currentGame.creatorUsername.equals(sharedViewModel.getUsername())){
                displayPoints.setText(new StringBuilder("You:  " +sharedViewModel.getCurrentGame().getPoints()));
                displayOpponentsPoints.setText(new StringBuilder(((Multiplayer) sharedViewModel.getCurrentGame()).getOpponentUsername()+":  " +sharedViewModel.getCurrentGame().getOpponentPoints()));
            }else {
                displayPoints.setText(new StringBuilder("You:  " +sharedViewModel.getCurrentGame().getOpponentPoints()));
                displayOpponentsPoints.setText(new StringBuilder(((Multiplayer) sharedViewModel.getCurrentGame()).getCreatorUsername() +":  " +((Multiplayer) sharedViewModel.getCurrentGame()).creatorPoints));
            }

            //if the previous round was the final round
            if (currentGame.currentRound == currentGame.noOfGames) {
                if(currentGame.creatorPoints > currentGame.opponentPoints){
                    firebaseDB.updateWinner(currentGame.getCreatorUsername());
                } else if (currentGame.creatorPoints < currentGame.opponentPoints) {
                    firebaseDB.updateWinner(currentGame.getOpponentUsername());
                }else {
                    firebaseDB.updateWinner("draw");
                }
            }else{
                nextRoundButton.setVisibility(View.VISIBLE);
            }

        });

        // When OPPONENT POINTS INCREASE OBSERVER
        MutableLiveData<Integer> opponentPoints = FirebaseDB.getInstance().getOpponentPoints();
        opponentPoints.observe(requireActivity(), points -> {
            if(sharedViewModel.getCurrentGame() == null)return;

            Multiplayer currentGame = (Multiplayer) sharedViewModel.getCurrentGame();
            if(currentGame.creatorPoints == 0  && points == 0)return;

            ((Multiplayer) sharedViewModel.getCurrentGame()).setOpponentPoints(points);
            if(currentGame.creatorUsername.equals(sharedViewModel.getUsername())){
                displayPoints.setText(new StringBuilder("You:  " +sharedViewModel.getCurrentGame().getPoints()));
                displayOpponentsPoints.setText(new StringBuilder(((Multiplayer) sharedViewModel.getCurrentGame()).getOpponentUsername()+":  " +sharedViewModel.getCurrentGame().getOpponentPoints()));
            }else {
                displayPoints.setText(new StringBuilder("You:  " +sharedViewModel.getCurrentGame().getOpponentPoints()));
                displayOpponentsPoints.setText(new StringBuilder(((Multiplayer) sharedViewModel.getCurrentGame()).getCreatorUsername() +":  " +((Multiplayer) sharedViewModel.getCurrentGame()).creatorPoints));
            }
            //if the previous round was the final round
            if (currentGame.currentRound == currentGame.noOfGames) {
                if(currentGame.creatorPoints > currentGame.opponentPoints){
                    firebaseDB.updateWinner(currentGame.getCreatorUsername());
                } else if (currentGame.creatorPoints < currentGame.opponentPoints) {
                    firebaseDB.updateWinner(currentGame.getOpponentUsername());
                }else {
                    firebaseDB.updateWinner("draw");
                }
            }else{
                nextRoundButton.setVisibility(View.VISIBLE);
            }
        });

        MutableLiveData<Integer> drawCount = FirebaseDB.getInstance().getDrawCount();
        drawCount.observe(requireActivity(), count -> {
            if(sharedViewModel.getCurrentGame() == null || count == 0)return;
            ((Multiplayer) sharedViewModel.getCurrentGame()).setDrawCount(count);

            Multiplayer currentGame = (Multiplayer) sharedViewModel.getCurrentGame();
            if (currentGame.currentRound == currentGame.noOfGames) {
                if(currentGame.creatorPoints > currentGame.opponentPoints){
                    firebaseDB.updateWinner(currentGame.getCreatorUsername());
                } else if (currentGame.creatorPoints < currentGame.opponentPoints) {
                    firebaseDB.updateWinner(currentGame.getOpponentUsername());
                }else {
                    firebaseDB.updateWinner("draw");
                }
            }else{
                nextRoundButton.setVisibility(View.VISIBLE);
            }
        });

        MutableLiveData<String> winner = FirebaseDB.getInstance().getWinner();
        winner.observe(requireActivity(), winnerString->{
            if(winnerString.isEmpty())return;
            multiplayerGameOver(winnerString);
        });


    }

    private void optionSelected(String option){
        if (sharedViewModel.getCurrentGame().getType().equals("singleplayer")) {
            singlePlayer(option);
        }else{
            multiPlayer(option);
        }
    }


    private void setMultiplayerUI(Multiplayer multiplayer){
        if(playGameContainer.getVisibility() == View.GONE){
            playGameContainer.setVisibility(View.VISIBLE);
        }
        if(multiplayer.getMode().equals("RPS")){
            _3ChipsLayout.setVisibility(View.VISIBLE);
        } else if (multiplayer.getMode().equals("RPSLS")) {
            _5ChipsLayout.setVisibility(View.VISIBLE);
        }
        displayGamesPlayed.setText(new StringBuilder(multiplayer.getRound() + "/" + multiplayer.getNoOfGames()));

        Multiplayer currentGame = (Multiplayer) sharedViewModel.getCurrentGame();

        if(currentGame.creatorUsername.equals(sharedViewModel.getUsername())){
            displayPoints.setText(new StringBuilder("You:  " +0));
            displayOpponentsPoints.setText(new StringBuilder(((Multiplayer) sharedViewModel.getCurrentGame()).getOpponentUsername()+":  " +0));
        }else {
            displayPoints.setText(new StringBuilder("You:  " +0));
            displayOpponentsPoints.setText(new StringBuilder(((Multiplayer) sharedViewModel.getCurrentGame()).getCreatorUsername() +":  " +0));
        }
    }

    private void multiPlayer(String option) {
        Multiplayer currentGame = (Multiplayer) sharedViewModel.getCurrentGame();

        if (sharedViewModel.getUsername().equals(currentGame.getCreatorUsername())) {
            firebaseDB.updateCreatorOption(option);
            if (currentGame.opponentOption.isEmpty() && currentGame.creatorOption.isEmpty()) {
                setBackgroundYourOption(option);
                opponentOptionDisplay.setVisibility(View.GONE);
                _2ChipsLayout.setVisibility(View.VISIBLE);
                _3ChipsLayout.setVisibility(View.GONE);
                _5ChipsLayout.setVisibility(View.GONE);
            } else {

                String verdict = sharedViewModel.determineWinner(option, currentGame.opponentOption);
                opponentOptionDisplay.setVisibility(View.VISIBLE);
                _2ChipsLayout.setVisibility(View.VISIBLE);
                _3ChipsLayout.setVisibility(View.GONE);
                _5ChipsLayout.setVisibility(View.GONE);
                switch (verdict) {
                    case "won":
                        firebaseDB.updateCreatorPoints(currentGame.creatorPoints + 1);
                        break;
                    case "lost":
                        firebaseDB.updateOpponentPoints(currentGame.opponentPoints + 1);
                        break;
                    case "draw":
                        firebaseDB.updateDrawCount(currentGame.getDrawCount()+1);
                        break;
                }
                firebaseDB.updateCurrentRound(currentGame.currentRound + 1);
            }
        }else{
            firebaseDB.updateOpponentOption(option);
            if (currentGame.opponentOption.isEmpty() && currentGame.creatorOption.isEmpty()) {
                setBackgroundOpponentOption(option);
                opponentOptionDisplay.setVisibility(View.GONE);
                _2ChipsLayout.setVisibility(View.VISIBLE);
                _3ChipsLayout.setVisibility(View.GONE);
                _5ChipsLayout.setVisibility(View.GONE);
            } else {
                String verdict = sharedViewModel.determineWinner(currentGame.creatorOption,option);
                opponentOptionDisplay.setVisibility(View.VISIBLE);
                _2ChipsLayout.setVisibility(View.VISIBLE);
                _3ChipsLayout.setVisibility(View.GONE);
                _5ChipsLayout.setVisibility(View.GONE);
                switch (verdict) {
                    case "won":
                        firebaseDB.updateCreatorPoints(currentGame.creatorPoints + 1);
                        break;
                    case "lost":
                        firebaseDB.updateOpponentPoints(currentGame.opponentPoints + 1);
                        break;
                    case "draw":
                        firebaseDB.updateDrawCount(currentGame.getDrawCount()+1);
                        break;
                }
                firebaseDB.updateCurrentRound(currentGame.currentRound + 1);
            }
        }
    }


    private void multiplayerGameOver(String winner) {

        TextView displayVerdict = requireActivity().findViewById(R.id.display_verdict);
        TextView playAgainButton = requireActivity().findViewById(R.id.play_again_button);
        LinearLayout playAgainContainer = requireActivity().findViewById(R.id.play_again_container);

        Multiplayer currentGame = (Multiplayer) sharedViewModel.getCurrentGame();

        if(currentGame.creatorUsername.equals(sharedViewModel.getUsername())){
            if(winner.equals(currentGame.creatorUsername)){
                displayVerdict.setText(getResources().getString(R.string.you_win_text));
                displayVerdict.setCompoundDrawablesRelative(null,null,ContextCompat.getDrawable(requireContext(),R.drawable.happy_icon),null);
            } else if (winner.equals(currentGame.opponentUsername)) {
                displayVerdict.setText(getResources().getString(R.string.you_lose_text));
                displayVerdict.setCompoundDrawablesRelative(null,null,ContextCompat.getDrawable(requireContext(),R.drawable.sad2_icon),null);
            }else{
                displayVerdict.setText(getResources().getString(R.string.draw_text));
                displayVerdict.setCompoundDrawablesRelative(null,null,ContextCompat.getDrawable(requireContext(),R.drawable.sad2_icon),null);
            }
        }else {
            if(winner.equals(currentGame.creatorUsername)){
                displayVerdict.setText(getResources().getString(R.string.you_lose_text));
                displayVerdict.setCompoundDrawablesRelative(null,null,ContextCompat.getDrawable(requireContext(),R.drawable.sad2_icon),null);
            } else if (winner.equals(currentGame.opponentUsername)) {
                displayVerdict.setText(getResources().getString(R.string.you_win_text));
                displayVerdict.setCompoundDrawablesRelative(null,null,ContextCompat.getDrawable(requireContext(),R.drawable.happy_icon),null);
            }else{
                displayVerdict.setText(getResources().getString(R.string.draw_text));
                displayVerdict.setCompoundDrawablesRelative(null,null,ContextCompat.getDrawable(requireContext(),R.drawable.sad2_icon),null);
            }
        }

        displayVerdict.setVisibility(View.VISIBLE);
        playAgainContainer.setVisibility(View.VISIBLE);


        playAgainButton.setOnClickListener(v->{
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
        });
    }
    private void multiplayerNextRound() {
        _2ChipsLayout.setVisibility(View.GONE);
        nextRoundButton.setVisibility(View.GONE);
        opponentOptionDisplay.setVisibility(View.GONE);
        if(sharedViewModel.getCurrentGame().getMode().equals("RPS")){
            _3ChipsLayout.setVisibility(View.VISIBLE);
        }else{
            _5ChipsLayout.setVisibility(View.VISIBLE);
        }
        sharedViewModel.getCurrentGame().increaseRound();
        ((Multiplayer) sharedViewModel.getCurrentGame()).setCreatorOption("");
        ((Multiplayer) sharedViewModel.getCurrentGame()).setOpponentOption("");
        firebaseDB.clearOptions();
        displayGamesPlayed.setText(new StringBuilder(sharedViewModel.getCurrentGame().getRound() + "/" + sharedViewModel.getCurrentGame().getNoOfGames()));
    }

    private void singlePlayer(String yourChoice){
        setBackgroundYourOption(yourChoice);
        _3ChipsLayout.setVisibility(View.GONE);
        _5ChipsLayout.setVisibility(View.GONE);
        _2ChipsLayout.setVisibility(View.VISIBLE);

        String computerChoice = sharedViewModel.getComputerOption();
        String verdict = sharedViewModel.determineWinner(yourChoice, computerChoice);

        (new Handler()).postDelayed(() -> {

            if (verdict.equals("won")){
                sharedViewModel.getCurrentGame().increasePoints();
            } else if (verdict.equals("lost")) {
                sharedViewModel.getCurrentGame().increaseOpponentPoints();
            }
            setBackgroundOpponentOption(computerChoice);

            if(sharedViewModel.getCurrentGame().getRound() >= sharedViewModel.getCurrentGame().getNoOfGames()){
                gameOver();
            }else{
//                if(sharedViewModel.getCurrentGame().getPoints() > sharedViewModel.getCurrentGame().getNoOfGames()/2){
//                    displayPoints.setText(new StringBuilder("Points: " +  sharedViewModel.getCurrentGame().getPoints()));
//                    gameOver();
//                    return;
//                }

                nextRoundButton.setVisibility(View.VISIBLE);
                displayPoints.setText(new StringBuilder("You: " +  sharedViewModel.getCurrentGame().getPoints()));
                displayOpponentsPoints.setText(new StringBuilder("Computer: " +  sharedViewModel.getCurrentGame().getOpponentPoints()));
            }
        },1000);



    }

    private void gameOver(){
        TextView displayVerdict = requireActivity().findViewById(R.id.display_verdict);
        TextView playAgainButton = requireActivity().findViewById(R.id.play_again_button);
        LinearLayout playAgainContainer = requireActivity().findViewById(R.id.play_again_container);


        if(sharedViewModel.getCurrentGame().getPoints() > sharedViewModel.getCurrentGame().getOpponentPoints()){
            displayVerdict.setText(getResources().getString(R.string.you_win_text));
            displayVerdict.setCompoundDrawablesRelative(null,null,ContextCompat.getDrawable(requireContext(),R.drawable.happy_icon),null);
        } else if (sharedViewModel.getCurrentGame().getPoints() < sharedViewModel.getCurrentGame().getOpponentPoints()) {
            displayVerdict.setText(getResources().getString(R.string.you_lose_text));
            displayVerdict.setCompoundDrawablesRelative(null,null,ContextCompat.getDrawable(requireContext(),R.drawable.sad2_icon),null);
        }else {
            displayVerdict.setText(getResources().getString(R.string.draw_text));
            displayVerdict.setCompoundDrawablesRelative(null,null,ContextCompat.getDrawable(requireContext(),R.drawable.sad2_icon),null);
        }

        displayPoints.setText(new StringBuilder("You: " +  sharedViewModel.getCurrentGame().getPoints()));
        displayOpponentsPoints.setText(new StringBuilder("Computer: " +  sharedViewModel.getCurrentGame().getOpponentPoints()));
        displayGamesPlayed.setText(new StringBuilder(sharedViewModel.getCurrentGame().getRound() + "/" + sharedViewModel.getCurrentGame().getNoOfGames()));
        displayVerdict.setVisibility(View.VISIBLE);
        playAgainContainer.setVisibility(View.VISIBLE);

        playAgainButton.setOnClickListener(v->{
            sharedViewModel.setCurrentGame(new SinglePlayer(sharedViewModel.getGameMode(),sharedViewModel.getNumberOfGames()));
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PlayFragment()).commit();
        });
    }


    private void setBackgroundYourOption(String option){
        switch (option) {
            case "rock":
                yourOptionDisplay.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.chip_rock_big));
                break;
            case "paper":
                yourOptionDisplay.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.chip_paper_big));
                break;
            case "scissors":
                yourOptionDisplay.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.chip_scissors_big));
                break;
            case "lizard":
                yourOptionDisplay.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.chip_lizard_big));
                break;
            case "spock":
                yourOptionDisplay.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.chip_spock_big));
                break;
        }
    }
    private void setBackgroundOpponentOption(String option){

        switch (option) {
            case "rock":
                opponentOptionDisplay.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.chip_rock_big));
                break;
            case "paper":
                opponentOptionDisplay.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.chip_paper_big));
                break;
            case "scissors":
                opponentOptionDisplay.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.chip_scissors_big));
                break;
            case "lizard":
                opponentOptionDisplay.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.chip_lizard_big));
                break;
            case "spock":
                opponentOptionDisplay.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.chip_spock_big));
                break;
        }
        opponentOptionDisplay.setVisibility(View.VISIBLE);
    }
}