package com.onrender.umehwisdom.janken.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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

import kotlin.contracts.Returns;


public class SettingsFragment extends Fragment {
    private String gameMode = "RPS";

    private int numberOfGames = 1;

    private String opponent = "computer";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ConstraintLayout vsHumanButton  = view.findViewById(R.id.vs_human_button);
        ConstraintLayout vsComputerButton  = view.findViewById(R.id.vs_computer_button);


        vsHumanButton.setOnClickListener(v->{
            opponent = "human";
            vsHumanButton.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.options_background_selected));
            vsComputerButton.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.options_background_unselected));
        });


        vsComputerButton.setOnClickListener(v->{
            opponent = "computer";
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
            if(gameMode.equals("RPS")) return;
            selectedBackground.startAnimation(moveLeft);
            gameMode = "RPS";
            (new Handler()).postDelayed(() -> {
                rpsButton.setTextColor(ContextCompat.getColor(requireContext(),R.color.very_dark_blue));
                rpslsButton.setTextColor(ContextCompat.getColor(requireContext(),R.color.light_grey));
            },100);
        });

        rpslsButton.setOnClickListener(v -> {
            if(gameMode.equals("RPSLS")) return;
            selectedBackground.startAnimation(moveRight);
            gameMode = "RPSLS";
            (new Handler()).postDelayed(() -> {
                rpsButton.setTextColor(ContextCompat.getColor(requireContext(),R.color.light_grey));
                rpslsButton.setTextColor(ContextCompat.getColor(requireContext(),R.color.very_dark_blue));
            },100);
        });


        ImageView numberOfGamesBackButton  = view.findViewById(R.id.number_of_games_back_button);
        ImageView numberOfGamesForwardButton  = view.findViewById(R.id.number_of_games_forward_button);
        TextView displayNumberOfGames  = view.findViewById(R.id.display_number_of_games);

        numberOfGamesForwardButton.setOnClickListener(v->{
            if(numberOfGames == 9)return;
            if(numberOfGames == 7) {
                numberOfGamesForwardButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.forward_arrow_grey));
            }else{
                numberOfGamesForwardButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.forward_arrow));
            }
            numberOfGamesBackButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.backward_arrow));

            numberOfGames += 2;
            displayNumberOfGames.setText(String.valueOf(numberOfGames));
        });

        numberOfGamesBackButton.setOnClickListener(v->{
            if(numberOfGames == 1)return;
            if(numberOfGames == 3) {
                numberOfGamesBackButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.backward_arrow_grey));
            }else {
                numberOfGamesBackButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.backward_arrow));
            }
            numberOfGamesForwardButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.forward_arrow));

            numberOfGames -= 2;
            displayNumberOfGames.setText(String.valueOf(numberOfGames));
        });



        Button goToSharePageButton  = view.findViewById(R.id.go_to_share_page_button);
        goToSharePageButton.setOnClickListener(v->{
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShareChallengeFragment()).addToBackStack("share").commit();
        });



        modeButton.setOnClickListener(v->{
        });
    }
}