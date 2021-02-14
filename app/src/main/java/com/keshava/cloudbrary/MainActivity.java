package com.keshava.cloudbrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    EditText fname, lname, tell, emailId, password;
    Button btnSignUp;
    FirebaseAuth mFirebaseAuth;
    TextView tvSignIn;
    ProgressDialog progressDialog;
    FirebaseFirestore mfirebaseFirestore;
    String userId;
    CircleImageView ImgUserPhoto;

    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mfirebaseFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.eemail);
        fname = findViewById(R.id.ufirstname);
        lname = findViewById(R.id.ulastname);
        tell = findViewById(R.id.utell);
        password = findViewById(R.id.ppassword);
        btnSignUp = findViewById(R.id.uregisterr);
        tvSignIn = findViewById(R.id.loginto);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailId.getText().toString();
                final String pwd = password.getText().toString();
                final String Fname = fname.getText().toString();
                final String Lname = lname.getText().toString();
                final String Tell = tell.getText().toString();

                if (email.isEmpty()) {
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Please enter your password");
                    password.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields Are Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && pwd.isEmpty())) {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "SignUp Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                            } else {

                                userId = mFirebaseAuth.getCurrentUser().getUid();


                                // first we need to upload user photo to firebase storage and get url

                                StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
                                final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
                                imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        // image uploaded succesfully
                                        // now we can get our image url

                                        imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                // uri contain user image url

                                                String url = uri.toString();


                                                DocumentReference documentReference = mfirebaseFirestore.collection("users").document(userId);
                                                Map<String, Object> user = new HashMap<>();
                                                user.put("Uid", userId);
                                                user.put("fname", Fname);
                                                user.put("lname", Lname);
                                                user.put("tell", Tell);
                                                user.put("email", email);
                                                user.put("password", pwd);
                                                user.put("userimage", url);
                                                user.put("utype", 2);
                                                user.put("status", 1);
                                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(MainActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                startActivity(new Intent(MainActivity.this, home.class));


                                            }
                                        });


                                    }
                                });


                            }

                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();

                }

            }
        });
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, login.class);
                startActivity(i);
            }
        });


        ImgUserPhoto = findViewById(R.id.user_image);

        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 22) {

                    checkAndRequestForPermission();

                } else {
                    openGallery();
                }


            }
        });



    }

    ///////////////////////////////////////////////////////////////////////////////
    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }
////////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////
    private void checkAndRequestForPermission() {


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(MainActivity.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        } else
            openGallery();

    }
///////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData();
            ImgUserPhoto.setImageURI(pickedImgUri);


        }


    }
////////////////////////////////////////////////////////////////
}