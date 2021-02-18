package com.keshava.cloudbrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class mybooks extends AppCompatActivity {

    private static final String TAG = "FirestoreSearchActivity";
    private static final String BOOKS = "Books";

    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();

    private MyBooksrecycleAdapter mAdapterr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybooks);







        RecyclerView recyclerView = findViewById(R.id.myrecycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));





        Query query = mDb.collection(BOOKS);
//                ;

//        final PagedList.Config config = new PagedList.Config.Builder()
//                .setInitialLoadSizeHint(1)
//                .setPageSize(1)
//                .build();


        FirestoreRecyclerOptions<Books> optionss = new FirestoreRecyclerOptions.Builder<Books>()
                .setQuery(query, Books.class)
                .build();

        mAdapterr = new MyBooksrecycleAdapter(optionss);



        recyclerView.setAdapter(mAdapterr);




    }
    @Override
    protected void onStart() {
        super.onStart();
        mAdapterr.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapterr.stopListening();
    }

}