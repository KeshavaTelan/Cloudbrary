package com.keshava.cloudbrary;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {
    EditText logpass,logemail;
    Button login ;
    TextView sign,welcometext;
    //  ImageView fingerprintbtn;
    FirebaseAuth mFirebaseAuth;
    LinearLayout registerlinier;
    FirebaseFirestore db;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        db = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        login =(Button) findViewById(R.id.login);
        logemail =(EditText) findViewById(R.id.emailtxt);
        logpass =(EditText) findViewById(R.id.paswrdtxt);
        registerlinier = (LinearLayout)findViewById(R.id.registerlinier);
        welcometext =(TextView)findViewById(R.id.wlcometxt) ;

        sign =(TextView) findViewById(R.id.signup);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if( mFirebaseUser != null ){
//

                    String uid = mFirebaseAuth.getCurrentUser().getUid();

                    DocumentReference docRef = db.collection("users").document(uid);

                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if(documentSnapshot.exists()){

                                int utype = documentSnapshot.getLong("utype").intValue();



                                if(utype==1){
                                    Toast.makeText(login.this,"adminr",Toast.LENGTH_SHORT).show();
                                    Intent gomain = new Intent(login.this,adminpage.class);
                                    startActivity(gomain);


                                }else if(utype==2){
                                    Toast.makeText(login.this,"user",Toast.LENGTH_SHORT).show();
                                    Intent gomain = new Intent(login.this,home.class);
                                    startActivity(gomain);


                                }

                                else{

                                    logpass.setText(null);
                                    logemail.setText(null);
                                    Toast.makeText(login.this,"sorry your not a user",Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                Toast.makeText(login.this,"Document does not exist",Toast.LENGTH_SHORT).show();
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(login.this,"Error ",Toast.LENGTH_SHORT).show();

                        }
                    });




                }
                else{
                    Toast.makeText(login.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };

        ///////////////////////
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = logemail.getText().toString();
                final String pwd = logpass.getText().toString();
                if(email.isEmpty()){
                    logemail.setError("Please enter email id");
                    logemail.requestFocus();
                }
                else  if(pwd.isEmpty()){
                    logpass.setError("Please enter your password");
                    logpass.requestFocus();
                }
                else  if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(login.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else  if(!(email.isEmpty() && pwd.isEmpty())){

                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){



                                String uid = mFirebaseAuth.getCurrentUser().getUid();

                                DocumentReference docRef = db.collection("users").document(uid);

                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        if(documentSnapshot.exists()){

                                            int utype = documentSnapshot.getLong("utype").intValue();



                                            if(utype==1){
                                                Toast.makeText(login.this,"adminr",Toast.LENGTH_SHORT).show();
                                                Intent gomain = new Intent(login.this,adminpage.class);
                                                startActivity(gomain);


                                            }else if(utype==2){
                                                Toast.makeText(login.this,"user",Toast.LENGTH_SHORT).show();
                                                Intent gomain = new Intent(login.this,home.class);
                                                startActivity(gomain);


                                            }

                                            else{

                                                logpass.setText(null);
                                                logemail.setText(null);
                                                Toast.makeText(login.this,"sorry your not a user",Toast.LENGTH_SHORT).show();
                                            }

                                        }else {
                                            Toast.makeText(login.this,"Document does not exist",Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(login.this,"Error ",Toast.LENGTH_SHORT).show();

                                    }
                                });



                            }
                            else{
                                Toast.makeText(login.this,"Login Error, Please Login Again",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(login.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

                }

            }
        });




    }

    @Override
    public void onBackPressed() {

    }
    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

}



