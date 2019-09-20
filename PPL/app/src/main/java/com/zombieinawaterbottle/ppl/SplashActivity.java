package com.zombieinawaterbottle.ppl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {
    FirebaseFirestore db;
    String auctionerName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        db = FirebaseFirestore.getInstance();
        db.collection("Metadata").document("refresh").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();
                        auctionerName = doc.get("auctioner").toString();
                        Intent intent = new Intent(SplashActivity.this, OpeningActivity.class);
                        intent.putExtra("auctionerName", auctionerName);

                        startActivity(intent);
                        finish();
                    }
                });
    }

}
