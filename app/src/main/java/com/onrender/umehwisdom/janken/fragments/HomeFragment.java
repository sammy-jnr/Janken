package com.onrender.umehwisdom.janken.fragments;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.onrender.umehwisdom.janken.CaptureAct;
import com.onrender.umehwisdom.janken.R;
import com.onrender.umehwisdom.janken.firebase.FirebaseDB;
import com.onrender.umehwisdom.janken.models.Multiplayer;
import com.onrender.umehwisdom.janken.models.SharedViewModel;

import java.io.File;
import java.io.FileOutputStream;


public class HomeFragment extends Fragment {

    FirebaseDB firebaseDB;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseDB = FirebaseDB.getInstance();

        MaterialButton newGameButton = view.findViewById(R.id.new_game_button);
        ImageView scanButton = view.findViewById(R.id.home_scan_button);
        ConstraintLayout homeLayout1 = view.findViewById(R.id.home_layout1);
        ConstraintLayout homeLayout2 = view.findViewById(R.id.home_layout2);
        ConstraintLayout homeLayout3 = view.findViewById(R.id.home_layout3);


        scanButton.setOnClickListener(v->{
//            homeLayout1.setVisibility(View.GONE);
            scan();
//            homeLayout2.setVisibility(View.VISIBLE);
        });

        view.findViewById(R.id.home_profile_button).setOnClickListener(v->{
            homeLayout1.setVisibility(View.GONE);
            homeLayout3.setVisibility(View.VISIBLE);
        });


        view.findViewById(R.id.profile_back_button).setOnClickListener(v->{
            homeLayout3.setVisibility(View.GONE);
            homeLayout1.setVisibility(View.VISIBLE);
        });

        TextInputEditText profileUsername = view.findViewById(R.id.profile_username);

        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        if(sharedViewModel.getUsername() != null){
            profileUsername.setText(sharedViewModel.getUsername());
        }

        view.findViewById(R.id.save_username_button).setOnClickListener(v->{
            File file = new File(requireContext().getFilesDir(), "username.txt");
            String username = profileUsername.getText().toString().trim();
            if(username.isEmpty()){
                Toast.makeText(requireContext(),"Username cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if(username.length() > 16){
                Toast.makeText(requireContext(),"Username is too long", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                FileOutputStream writer = new FileOutputStream(file);
                writer.write(username.getBytes());
                writer.close();
                Toast.makeText(requireContext(), "saved", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        newGameButton.setOnClickListener(v->{
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).addToBackStack("settings").commit();
        });


    }

    private void scan() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to turn on flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        qrLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> qrLauncher = registerForActivityResult(new ScanContract(), result->{
        if (result.getContents() != null){
            SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

            firebaseDB.joinGame(result.getContents(),sharedViewModel.getUsername());
            MutableLiveData<Boolean> isAccepted = firebaseDB.getGameAccepted();
            isAccepted.observe(requireActivity(), aBoolean -> {
                if (aBoolean){
                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PlayFragment()).commit();
                }
            });

        }else{
            Toast.makeText(requireContext(), "Failed to load", Toast.LENGTH_LONG).show();
        }
    });



}