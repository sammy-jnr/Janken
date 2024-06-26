package com.onrender.umehwisdom.janken.fragments;

import android.health.connect.datatypes.units.Length;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.onrender.umehwisdom.janken.R;
import com.onrender.umehwisdom.janken.firebase.FirebaseDB;
import com.onrender.umehwisdom.janken.interfaces.Game;
import com.onrender.umehwisdom.janken.models.Multiplayer;
import com.onrender.umehwisdom.janken.models.SinglePlayer;
import com.onrender.umehwisdom.janken.models.SharedViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


public class SettingsFragment extends Fragment {

    SharedViewModel sharedViewModel;
    ConstraintLayout vsHumanButton, vsComputerButton;
    TextView modeButton, rpsButton, rpslsButton, displayNumberOfGames;
    RelativeLayout selectedBackground;

    ImageView numberOfGamesBackButton, numberOfGamesForwardButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        sharedViewModel.setGameMode("RPS");
        sharedViewModel.setNumberOfGames(1);
        sharedViewModel.setOpponent("computer");

        vsComputerButton  = view.findViewById(R.id.vs_computer_button);
        vsHumanButton  = view.findViewById(R.id.vs_human_button);


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


        modeButton  = view.findViewById(R.id.mode_button);
        rpsButton  = view.findViewById(R.id.rps_button);
        rpslsButton  = view.findViewById(R.id.rpsls_button);
        selectedBackground  = view.findViewById(R.id.selected_background);

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


        numberOfGamesBackButton  = view.findViewById(R.id.number_of_games_back_button);
        numberOfGamesForwardButton  = view.findViewById(R.id.number_of_games_forward_button);
        displayNumberOfGames  = view.findViewById(R.id.display_number_of_games);

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
            Game currentGame;
            if(sharedViewModel.getOpponent().equals("human")){
                if(sharedViewModel.getUsername() == null) {
                    handleNoUsername();
                    return;
                }
                currentGame = new Multiplayer(sharedViewModel.getGameMode(),sharedViewModel.getNumberOfGames(),sharedViewModel.getUsername());
                sharedViewModel.setCurrentGame(currentGame);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShareChallengeFragment()).addToBackStack("share").commit();
            }else{
                currentGame = new SinglePlayer(sharedViewModel.getGameMode(),sharedViewModel.getNumberOfGames());
                sharedViewModel.setCurrentGame(currentGame);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PlayFragment()).commit();
            }

        });
    }
    private void handleNoUsername(){
        ConstraintLayout settingsUsernamePopup = requireActivity().findViewById(R.id.settings_username_popup);
        TextInputEditText settingsUsername = requireActivity().findViewById(R.id.settings_username);
        MaterialButton settingsUsernameSaveButton = requireActivity().findViewById(R.id.settings_username_save_button);

        settingsUsernamePopup.startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.slide_up));

        settingsUsernameSaveButton.setOnClickListener(v -> {
            String username = settingsUsername.getText().toString().trim();
            if(username.isEmpty()){
                Toast.makeText(requireContext(),"Username cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if(username.length() > 16){
                Toast.makeText(requireContext(),"Username is too long", Toast.LENGTH_SHORT).show();
                return;
            }
            File file = new File(requireActivity().getFilesDir(),"username.txt");

            try {
                FileOutputStream writer = new FileOutputStream(file);
                writer.write(username.getBytes());
                writer.close();
                Toast.makeText(requireContext(),"saved",Toast.LENGTH_SHORT).show();
                settingsUsernamePopup.startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.slide_down));
            }catch (Exception e){
                e.printStackTrace();
            }


        });




    }

}