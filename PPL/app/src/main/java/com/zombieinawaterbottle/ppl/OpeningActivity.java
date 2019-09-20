package com.zombieinawaterbottle.ppl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

public class OpeningActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MyPREFERENCES = "MyPrefs";
    boolean fetchTeam = false;
    TextView currentUsers;
    EditText enterName;
    String name;
    Button signIn;
    Button joinAuction;
    Button newAuction;

    FirebaseFirestore db;
    String auctionerName = "NAN";
    ArrayList<Player> auctionList;
    ArrayList<String> users;
    boolean isAuctioner = false;
    SharedPreferences sharedpreferences;
    ListenerRegistration registration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_opening);
        Intent intent = getIntent();
        auctionerName = intent.getStringExtra("auctionerName");
        auctionList = new ArrayList<>();
        enterName = (EditText) findViewById(R.id.userName);
        signIn = (Button) findViewById(R.id.signIn);
        currentUsers = (TextView) findViewById(R.id.userList);
        newAuction = (Button) findViewById(R.id.newAuction);
        joinAuction = (Button) findViewById(R.id.continueAuction);
        newAuction.setOnClickListener(this);
        signIn.setOnClickListener(this);


        newAuction.setOnClickListener(this);
        joinAuction.setOnClickListener(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        name = sharedpreferences.getString("userName", null);

        if (isAuctioner)
            newAuction.setVisibility(View.INVISIBLE);
        else
            newAuction.setVisibility(View.GONE);
        //currentUsers.setVisibility(View.INVISIBLE);
        joinAuction.setClickable(false);
        enterName.setText(sharedpreferences.getString("userName", null));

    }

    @Override
    public void onClick(View view) {
        if (view == newAuction) {
            Toast.makeText(this, "Setup cholche.", Toast.LENGTH_LONG).show();

            registration.remove();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("userName", name);
            intent.putExtra("allUsers", users);
            if (isAuctioner)
                resetDBForAuction(intent);

        }
        if (view == joinAuction) {
            registration.remove();

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("userName", name);
            intent.putExtra("allUsers", users);
            intent.putExtra("isAuctioner", isAuctioner);
            fetchTeam = true;
            startActivity(intent);
            /*
            while(true) {
                if (!fetchTeam) {
                    intent.putExtra("auctionList", auctionList);
                    startActivity(intent);
                    break;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/

        }
        if (view == signIn) {
            name = enterName.getText().toString();
            if (enterName.getText() == null) {
                Toast.makeText(this, "Naam lekh be!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (name.equalsIgnoreCase(auctionerName))
                isAuctioner = true;
            //enterName.setVisibility(View.GONE);
            enterName.setEnabled(false);
            signIn.setClickable(false);
            joinAuction.setClickable(true);
            if (isAuctioner) newAuction.setVisibility(View.VISIBLE);
            else newAuction.setVisibility(View.GONE);
            currentUsers.setVisibility(View.VISIBLE);

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("userName", name);
            editor.commit();
            Map<String, Boolean> exists = new HashMap<>();
            db.collection("Users").document(name).set(exists);
            registration  = db.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    currentUsers.setText("USERS:");
                    users = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        users.add(doc.getId());
                        currentUsers.setText(currentUsers.getText() + "\n" + doc.getId());
                    }
                }
            });

            db.collection("Metadata").document("Auction")
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if ((Boolean) documentSnapshot.get("auctionOn") == true) {
                                joinAuction.setVisibility(View.VISIBLE);
                            }
                        }
                    });

        }
    }

    private void resetDBForAuction(final Intent intent) {
        if (!isAuctioner) return;
        Map<String, Boolean> auctionOn = new HashMap<>();
        auctionOn.put("auctionOn", true);
        auctionOn.put("auctionOver", false);

        db.collection("Metadata").document("Auction").set(auctionOn);

        db.collection("Transactions").document("current").delete();
        db.collection("Transactions").document("currentTimer").delete();
        double startingCash = 150.0;
        Map<String, Double> cash = new HashMap<>();
        for (String user : users) {
            cash.put(user, startingCash);
        }
        db.collection("Cash").document("current").set(cash);


        db.collection("Unsold").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                documentSnapshot.getReference().delete();
                            }
                        }
                    }
                });
        for (final String user:users){
            db.collection("Users").document(user).collection("Players").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful())
                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                Log.d("Auction", documentSnapshot.getId()+ " => " + documentSnapshot.getData());

                                db.collection("Users").document(user).collection("Players")
                                        .document(documentSnapshot.getId()).delete();
                            }
                        }
                    });

        }
        fetchTeam = true;

        db.collection("players")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Random random = new Random();
                                int v = Math.abs(random.nextInt(1000) * (int) (System.currentTimeMillis() / 1000) % 1000);

                                db.collection("players").document(document.getId()).update("auctionID", v);

                            }
                            try {
                                Thread.sleep(5000);

                                startActivity(intent);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            startActivity(intent);
                        }
                    }
                });



    }




}
