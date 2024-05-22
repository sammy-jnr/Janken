package com.onrender.umehwisdom.janken.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.onrender.umehwisdom.janken.models.Multiplayer;

import java.util.concurrent.atomic.AtomicReference;

public class FirebaseDB {
    private static FirebaseDB INSTANCE;
    private final MutableLiveData<Multiplayer> multiplayerGameDetails;
    private final MutableLiveData<Boolean> gameAccepted;
    private final MutableLiveData<Integer> currentRound;
    private final MutableLiveData<Integer> creatorPoints;
    private final MutableLiveData<Integer> opponentPoints;
    private final MutableLiveData<Integer> drawCount;
    private final MutableLiveData<String> creatorOption;
    private final MutableLiveData<String> opponentOption;
    private final MutableLiveData<String> winner;



    private FirebaseDB(){
        multiplayerGameDetails = new MutableLiveData<>();
        gameAccepted = new MutableLiveData<>();
        currentRound = new MutableLiveData<>();
        creatorPoints = new MutableLiveData<>();
        opponentPoints = new MutableLiveData<>();
        drawCount = new MutableLiveData<>();
        creatorOption = new MutableLiveData<>();
        opponentOption = new MutableLiveData<>();
        winner = new MutableLiveData<>();
    }
    public static FirebaseDB getInstance(){
        if(INSTANCE ==null){
            INSTANCE = new FirebaseDB();
        }
        return INSTANCE;
    }

    DatabaseReference myRef;

    String gameID;
    public void  setRef(DatabaseReference myRef) {
        this.myRef = myRef;
    }

    ValueEventListener gameAcceptedListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            if(Boolean.FALSE.equals(snapshot.getValue(Boolean.class)))return;

            // get gameDetails for both player when gameAccepted changes
            myRef.child(gameID).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    multiplayerGameDetails.setValue(task.getResult().getValue(Multiplayer.class));
                }
            });

            gameAccepted.setValue(snapshot.getValue(Boolean.class));

            // Add all necessary fields listeners
            myRef.child(gameID).child("creatorOption").addValueEventListener(creatorOptionListener);
            myRef.child(gameID).child("currentRound").addValueEventListener(currentRoundListener);
            myRef.child(gameID).child("creatorPoints").addValueEventListener(creatorPointsListener);
            myRef.child(gameID).child("opponentPoints").addValueEventListener(opponentPointsListener);
            myRef.child(gameID).child("drawCount").addValueEventListener(drawCountListener);
            myRef.child(gameID).child("opponentOption").addValueEventListener(opponentOptionListener);
            myRef.child(gameID).child("winner").addValueEventListener(winnerListener);
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };
    ValueEventListener currentRoundListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            currentRound.setValue(snapshot.getValue(Integer.class));
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };
    ValueEventListener creatorPointsListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            creatorPoints.setValue(snapshot.getValue(Integer.class));
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };
    ValueEventListener opponentPointsListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            opponentPoints.setValue(snapshot.getValue(Integer.class));
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };
    ValueEventListener drawCountListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            drawCount.setValue(snapshot.getValue(Integer.class));
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };
    ValueEventListener creatorOptionListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            creatorOption.setValue(snapshot.getValue(String.class));
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };
    ValueEventListener opponentOptionListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            opponentOption.setValue(snapshot.getValue(String.class));
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };
    ValueEventListener winnerListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            winner.setValue(snapshot.getValue(String.class));
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };



    public  void createGame(String gameId,Multiplayer multiplayerGame){
        if(myRef == null) return;
        this.gameID=gameId;
        myRef.child(gameID).setValue(multiplayerGame);
        myRef.child(gameID).child("gameAccepted").addValueEventListener(gameAcceptedListener);

    }


    public  void joinGame(String gameID,String username){
        if(myRef == null) return;
        this.gameID=gameID;
        myRef.child(gameID).child("opponentUsername").setValue(username);
        myRef.child(gameID).child("gameAccepted").addValueEventListener(gameAcceptedListener);
        myRef.child(gameID).child("gameAccepted").setValue(true);
    }

    public void clearOptions(){
        myRef.child(gameID).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Multiplayer game = task.getResult().getValue(Multiplayer.class);
                if(!game.creatorOption.isEmpty() && !game.opponentOption.isEmpty()){
                    myRef.child(gameID).child("creatorOption").setValue("");
                    myRef.child(gameID).child("opponentOption").setValue("");
                }
            }
        });
    }




    public  void updateCreatorOption(String option){
        myRef.child(gameID).child("creatorOption").setValue(option);
    }
    public  void updateOpponentOption(String option){
        myRef.child(gameID).child("opponentOption").setValue(option);
    }
    public  void updateCurrentRound(int option){
        myRef.child(gameID).child("currentRound").setValue(option);
    }
    public  void updateCreatorPoints(int option){
        myRef.child(gameID).child("creatorPoints").setValue(option);
    }
    public  void updateOpponentPoints(int option){
        myRef.child(gameID).child("opponentPoints").setValue(option);
    }
    public  void updateDrawCount(int option){
        myRef.child(gameID).child("drawCount").setValue(option);
    }
    public  void updateWinner(String option){
        myRef.child(gameID).child("winner").setValue(option);
    }



    public MutableLiveData<Boolean> getGameAccepted() {
        return gameAccepted;
    }

    public MutableLiveData<Multiplayer> getMultiplayerGameDetails() {
        return multiplayerGameDetails;
    }

    public MutableLiveData<Integer> getDrawCount() {
        return drawCount;
    }

    public MutableLiveData<Integer> getCreatorPoints() {
        return creatorPoints;
    }

    public MutableLiveData<Integer> getOpponentPoints() {
        return opponentPoints;
    }

    public MutableLiveData<String> getCreatorOption() {
        return creatorOption;
    }

    public MutableLiveData<String> getOpponentOption() {
        return opponentOption;
    }

    public MutableLiveData<String> getWinner() {
        return winner;
    }
}
