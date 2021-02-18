package com.keshava.cloudbrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class home extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private CardView logout,books,mybook;
    private TextView greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        logout = findViewById(R.id.cardthree);
        books = findViewById(R.id.cardone);
        mybook = findViewById(R.id.cardtwo);
        greeting = findViewById(R.id.greeting);



        DocumentReference docRef = db.collection("users").document( auth.getCurrentUser().getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String m =  document.getString("fname");

                            greeting.setText("Hello "+m);



                    } else {

                    }
                } else {

                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent logout = new Intent(home.this, login.class);
                startActivity(logout);
            }
        });

        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent searchAct = new Intent(home.this, searchAct.class);
                startActivity(searchAct);
            }
        });

        mybook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchAct = new Intent(home.this, mybooks.class);
                startActivity(searchAct);
            }
        });
    }
}
