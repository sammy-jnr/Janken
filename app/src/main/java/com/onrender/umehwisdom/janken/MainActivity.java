package com.onrender.umehwisdom.janken;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.onrender.umehwisdom.janken.firebase.FirebaseDB;
import com.onrender.umehwisdom.janken.fragments.HomeFragment;
import com.onrender.umehwisdom.janken.models.SharedViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        SharedViewModel sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("games");

        FirebaseDB firebaseDB = FirebaseDB.getInstance();
        firebaseDB.setRef(myRef);


        File usernamePath = new File(this.getFilesDir(), "username.txt");
        try {
            if(usernamePath.exists()){
                Scanner usernameScanner = new Scanner(usernamePath);
                sharedViewModel.setUsername(usernameScanner.nextLine());
                usernameScanner.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).addToBackStack("home").commit();
    }
}