package com.keshava.cloudbrary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class
adminpage extends AppCompatActivity {

    private FirebaseFirestore mDatabase;
    private EditText bookname, booktype, bookprice, bookcount, bookdis;
    private ImageView bookimg;
    private Button addevnt;
    private TextView logout;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage);

        mDatabase = FirebaseFirestore.getInstance();

        bookname = findViewById(R.id.bookname);
        booktype = findViewById(R.id.booktype);
        bookprice = findViewById(R.id.bookprice);
        bookcount = findViewById(R.id.bookcount);
        bookdis = findViewById(R.id.bookdis);
        addevnt = findViewById(R.id.addBook);
        bookimg = findViewById(R.id.bimg);
        logout = findViewById(R.id.logout);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent logout = new Intent(adminpage.this, login.class);
                startActivity(logout);
            }
        });



        ////////////////////////////////
        addevnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String  bookname2  = bookname.getText().toString();
                final String  booktype2  = booktype.getText().toString();
                final String  bookprice2 = bookprice.getText().toString();
                final int  bookcount2 = Integer.parseInt( bookcount.getText().toString());
                final String  bookdis2   = bookdis.getText().toString();




                if (bookname2.isEmpty() || booktype2.isEmpty()) {
                    Toast.makeText(adminpage.this, "Please Fill all all the Details", Toast.LENGTH_SHORT).show();
                    bookname.requestFocus();


                } else {

                    StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("book_images");
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


                                    DocumentReference newCityRef = mDatabase.collection("Books").document();

                                    String id = newCityRef.getId();
                                    Map<String, Object> event = new HashMap<>();
                                    event.put("bookid", id);
                                    event.put("bookimg", url);
                                    event.put("bookname", bookname2);
                                    event.put("booktype", booktype2);
                                    event.put("bookprice", bookprice2);
                                    event.put("bookcount", bookcount2);
                                    event.put("bookdiscription", bookdis2);
                                    event.put("status", 1);

                                    Map<String, Integer> tt = new HashMap<>();
                                    tt.put("test",11);
                                    event.put("mybooks",tt);

                                    newCityRef.set(event).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(adminpage.this, "Book Add Successful", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                   bookname.setText(null);
                                   booktype.setText(null);
                                   bookprice.setText(null);
                                   bookcount.setText(null);
                                   bookdis.setText(null);
                                   bookimg.setImageDrawable(null);

                                    //startActivity(new Intent(activity_addEvent.this, MainActivity.class));
                                }
                            });


                        }
                    });

                }


            }
        });


        bookimg.setOnClickListener(new View.OnClickListener() {
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
    ////////////////////////////////////////

    private void checkAndRequestForPermission() {


        if (ContextCompat.checkSelfPermission(adminpage.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(adminpage.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(adminpage.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(adminpage.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        } else
            openGallery();

    }

    ///////////////////////////////////////////////////////////////////
    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    ////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData();
            bookimg.setImageURI(pickedImgUri);


        }


    }


    @Override
    public void onBackPressed() {

    }



}