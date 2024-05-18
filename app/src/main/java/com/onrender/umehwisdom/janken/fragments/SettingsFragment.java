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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onrender.umehwisdom.janken.R;
import com.onrender.umehwisdom.janken.models.CurrentGame;
import com.onrender.umehwisdom.janken.models.SharedViewModel;

import kotlin.contracts.Returns;


public class SettingsFragment extends Fragment {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        sharedViewModel.setCurrentGame(null);
        sharedViewModel.setOpponent("computer");
        sharedViewModel.setNumberOfGames(1);
        sharedViewModel.setGameMode("RPS");

        ConstraintLayout vsHumanButton  = view.findViewById(R.id.vs_human_button);
        ConstraintLayout vsComputerButton  = view.findViewById(R.id.vs_computer_button);


        vsHumanButton.setOnClickListener(v->{
            sharedViewModel.setOpponent("human");
            vsHumanButton.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.options_background_selected));
            vsComputerButton.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.options_background_unselected));
        });


        vsComputerButton.setOnClickListener(v->{
            sharedViewModel.setOpponent("computer");
            vsComputerButton.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.options_background_selected));
            vsHumanButton.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.options_background_unselected));
        });


        TextView modeButton  = view.findViewById(R.id.mode_button);
        TextView rpsButton  = view.findViewById(R.id.rps_button);
        TextView rpslsButton  = view.findViewById(R.id.rpsls_button);
        RelativeLayout selectedBackground  = view.findViewById(R.id.selected_background);

        Animation moveRight = AnimationUtils.loadAnimation(requireContext(), R.anim.move_right);
        Animation moveLeft = AnimationUtils.loadAnimation(requireContext(), R.anim.move_left);


        rpsButton.setOnClickListener(v -> {
            if(sharedViewModel.getGameMode().equals("RPS")) return;
            selectedBackground.startAnimation(moveLeft);
            sharedViewModel.setGameMode("RPS");
            (new Handler()).postDelayed(() -> {
                rpsButton.setTextColor(ContextCompat.getColor(requireContext(),R.color.very_dark_blue));
                rpslsButton.setTextColor(ContextCompat.getColor(requireContext(),R.color.light_grey));
            },100);
        });

        rpslsButton.setOnClickListener(v -> {
            if(sharedViewModel.getGameMode().equals("RPSLS")) return;
            selectedBackground.startAnimation(moveRight);
            sharedViewModel.setGameMode("RPSLS");
            (new Handler()).postDelayed(() -> {
                rpsButton.setTextColor(ContextCompat.getColor(requireContext(),R.color.light_grey));
                rpslsButton.setTextColor(ContextCompat.getColor(requireContext(),R.color.very_dark_blue));
            },100);
        });


        ImageView numberOfGamesBackButton  = view.findViewById(R.id.number_of_games_back_button);
        ImageView numberOfGamesForwardButton  = view.findViewById(R.id.number_of_games_forward_button);
        TextView displayNumberOfGames  = view.findViewById(R.id.display_number_of_games);

        numberOfGamesForwardButton.setOnClickListener(v->{
            if(sharedViewModel.getNumberOfGames() == 9)return;
            if(sharedViewModel.getNumberOfGames() == 7) {
                numberOfGamesForwardButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.forward_arrow_grey));
            }else{
                numberOfGamesForwardButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.forward_arrow));
            }
            numberOfGamesBackButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.backward_arrow));

            sharedViewModel.setNumberOfGames(sharedViewModel.getNumberOfGames()+2);
            displayNumberOfGames.setText(String.valueOf(sharedViewModel.getNumberOfGames()));
        });

        numberOfGamesBackButton.setOnClickListener(v->{
            if(sharedViewModel.getNumberOfGames() == 1)return;
            if(sharedViewModel.getNumberOfGames() == 3) {
                numberOfGamesBackButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.backward_arrow_grey));
            }else {
                numberOfGamesBackButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.backward_arrow));
            }
            numberOfGamesForwardButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.forward_arrow));

            sharedViewModel.setNumberOfGames(sharedViewModel.getNumberOfGames()-2);
            displayNumberOfGames.setText(String.valueOf(sharedViewModel.getNumberOfGames()));
        });

        modeButton.setOnClickListener(v->{
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GameInfoFragment()).addToBackStack("info").commit();
        });



        // continue button is clicked, decide what page to go
        view.findViewById(R.id.proceed_to_game).setOnClickListener(v->{
            CurrentGame currentGame =  new CurrentGame(
                    sharedViewModel.getOpponent(),sharedViewModel.getGameMode(),sharedViewModel.getNumberOfGames()
            );
            sharedViewModel.setCurrentGame(currentGame);

            if(sharedViewModel.getCurrentGame().getOpponent().equals("human")){
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShareChallengeFragment()).addToBackStack("share").commit();
            }else{
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PlayFragment()).addToBackStack("game").commit();
            }
        });



    }
}