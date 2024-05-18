package com.onrender.umehwisdom.janken.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.onrender.umehwisdom.janken.R;
import com.onrender.umehwisdom.janken.models.SharedViewModel;



public class PlayFragment extends Fragment {


    private SharedViewModel sharedViewModel;

    private  ConstraintLayout _2ChipsLayout, _3ChipsLayout, _5ChipsLayout;

    private ImageView yourOptionDisplay, opponentOptionDisplay;

    private TextView nextRoundButton, displayGamesPlayed, displayPoints;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);


        yourOptionDisplay = view.findViewById(R.id.your_option_display);
        opponentOptionDisplay = view.findViewById(R.id.opponent_option_display);
        TextView theHouseText = view.findViewById(R.id.the_house_text);
        nextRoundButton = view.findViewById(R.id.next_round_button);
        displayGamesPlayed = view.findViewById(R.id.display_games_played);
        displayPoints = view.findViewById(R.id.display_points);
        TextView displayGameMode = view.findViewById(R.id.display_game_mode);

        _2ChipsLayout = view.findViewById(R.id._2_chips_layout);
        _3ChipsLayout = view.findViewById(R.id._3_chips_layout);
        _5ChipsLayout = view.findViewById(R.id._5_chips_layout);


        displayGameMode.setText(sharedViewModel.getCurrentGame().getMode());
        displayGamesPlayed.setText(new StringBuilder("1/" + sharedViewModel.getCurrentGame().getNoOfGames()));

        if(sharedViewModel.getCurrentGame().getMode().equals("RPS")){
            _3ChipsLayout.setVisibility(View.VISIBLE);
        } else if (sharedViewModel.getCurrentGame().getMode().equals("RPSLS")) {
            _5ChipsLayout.setVisibility(View.VISIBLE);
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
            _2ChipsLayout.setVisibility(View.GONE);
            nextRoundButton.setVisibility(View.GONE);
            opponentOptionDisplay.setVisibility(View.GONE);
            displayGamesPlayed.setText(new StringBuilder(sharedViewModel.getCurrentGame().getRound() + "/" + sharedViewModel.getCurrentGame().getNoOfGames()));
            if(sharedViewModel.getCurrentGame().getMode().equals("RPS")){
                _3ChipsLayout.setVisibility(View.VISIBLE);
            }else{
                _5ChipsLayout.setVisibility(View.VISIBLE);
            }
        });




    }

    private void optionSelected(String option){
        singlePlayer(option);
    }

    private void singlePlayer(String yourChoice){
        switch (yourChoice) {
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
        _3ChipsLayout.setVisibility(View.GONE);
        _5ChipsLayout.setVisibility(View.GONE);
        _2ChipsLayout.setVisibility(View.VISIBLE);

        String computerChoice = sharedViewModel.getComputerOption();
        String verdict = sharedViewModel.determineWinner(yourChoice, computerChoice);

        (new Handler()).postDelayed(() -> {
            sharedViewModel.getCurrentGame().increaseRound();
            if (verdict.equals("won")){
                sharedViewModel.getCurrentGame().increasePoints();
            } else if (verdict.equals("draw")) {
                sharedViewModel.getCurrentGame().increaseDrawCount();
            }

            switch (computerChoice) {
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
            if(sharedViewModel.getCurrentGame().getRound() == sharedViewModel.getCurrentGame().getNoOfGames()){
                gameOver();
            }else{
                // if the user wins without getting to the last rounds
//                boolean forcedLose =
//                        (sharedViewModel.getCurrentGame().getNoOfGames() - sharedViewModel.getCurrentGame().getRound() + sharedViewModel.getCurrentGame().getPoints())
//                        < ((float)(sharedViewModel.getCurrentGame().getNoOfGames() - sharedViewModel.getCurrentGame().getDrawCount()) /2);
                if(sharedViewModel.getCurrentGame().getPoints() > sharedViewModel.getCurrentGame().getNoOfGames()/2){
                    displayPoints.setText(new StringBuilder("Points: " +  sharedViewModel.getCurrentGame().getPoints()));
                    gameOver();
                    return;
                }


                nextRoundButton.setVisibility(View.VISIBLE);
                displayPoints.setText(new StringBuilder("Points: " +  sharedViewModel.getCurrentGame().getPoints()));
            }
        },1000);



    }

    private void gameOver(){
        TextView displayVerdict = requireActivity().findViewById(R.id.display_verdict);
        TextView playAgainButton = requireActivity().findViewById(R.id.play_again_button);

        float benchmark = (float) (sharedViewModel.getCurrentGame().getNoOfGames() - sharedViewModel.getCurrentGame().getDrawCount()) /2;

        if(sharedViewModel.getCurrentGame().getPoints() > benchmark){
            displayVerdict.setText(getResources().getString(R.string.you_win_text));
            displayVerdict.setCompoundDrawablesRelative(null,null,ContextCompat.getDrawable(requireContext(),R.drawable.happy_icon),null);
        } else if (sharedViewModel.getCurrentGame().getPoints() < benchmark) {
            displayVerdict.setText(getResources().getString(R.string.you_lose_text));
            displayVerdict.setCompoundDrawablesRelative(null,null,ContextCompat.getDrawable(requireContext(),R.drawable.sad2_icon),null);
        }else {
            displayVerdict.setText(getResources().getString(R.string.draw_text));
            displayVerdict.setCompoundDrawablesRelative(null,null,ContextCompat.getDrawable(requireContext(),R.drawable.sad2_icon),null);
        }

        displayVerdict.setVisibility(View.VISIBLE);
        playAgainButton.setVisibility(View.VISIBLE);

        playAgainButton.setOnClickListener(v->{
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).addToBackStack("settings").commit();
        });
    }


}