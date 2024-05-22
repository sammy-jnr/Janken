package com.onrender.umehwisdom.janken.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.onrender.umehwisdom.janken.R;
import com.onrender.umehwisdom.janken.firebase.FirebaseDB;
import com.onrender.umehwisdom.janken.models.Multiplayer;
import com.onrender.umehwisdom.janken.models.SharedViewModel;

import java.util.Random;


public class ShareChallengeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_share_challenge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDB firebaseDB = FirebaseDB.getInstance();
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        ImageView shareBackButton = view.findViewById(R.id.share_back_button);
        ImageView copyButton = view.findViewById(R.id.copy_button);
        ImageView displayQrCode = view.findViewById(R.id.display_qr_code);


        //back button
        shareBackButton.setOnClickListener(v->{
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
        });


        //generate qr code
        String code = generateGameString();
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(code, BarcodeFormat.QR_CODE,700,700);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            displayQrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }

        Toast.makeText(requireContext(), sharedViewModel.getUsername() +" trial", Toast.LENGTH_SHORT).show();
        Multiplayer newGame = new Multiplayer(
                sharedViewModel.getCurrentGame().getMode(),
                sharedViewModel.getCurrentGame().getNoOfGames(),
                sharedViewModel.getUsername()
        );
        firebaseDB.createGame(code,newGame);

        // for multiplayer go to game page if challenge is accepted
        MutableLiveData<Boolean> isAccepted = firebaseDB.getGameAccepted();
        isAccepted.observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PlayFragment()).commit();
                }
            }
        });


        copyButton.setOnClickListener(v->{

        });

    }
    public String generateGameString(){
        String[] strings = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
                "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
                "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

        StringBuilder result = new StringBuilder();
        Random random = new Random();

        for (int i=0;i<15;i++){
            result.append(strings[random.nextInt(strings.length)]);
        }
        return result.toString();
    }
}