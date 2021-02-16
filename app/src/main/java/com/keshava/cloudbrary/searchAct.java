package com.keshava.cloudbrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class searchAct extends AppCompatActivity {

    private static final String TAG = "FirestoreSearchActivity";
    private static final String BOOKS = "Books";

    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();

    private BooksrecycleAdapter mAdapter;

    private CheckBox bname,bdis,btype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);



        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        Query query = mDb.collection(BOOKS)
                .orderBy("bookid", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Books> options = new FirestoreRecyclerOptions.Builder<Books>()
                .setQuery(query, Books.class)
                .build();

        mAdapter = new BooksrecycleAdapter(options);



        recyclerView.setAdapter(mAdapter);

        EditText searchBox = findViewById(R.id.searchBox);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Log.d(TAG, "Searchbox has changed to: " + s.toString());
                Query query;

                bname=findViewById(R.id.typetitle);
                bdis=findViewById(R.id.typedis);
                btype=findViewById(R.id.typegenre);



                if (s.toString().isEmpty()) {
                    query = mDb.collection(BOOKS)
                            .orderBy("bookid", Query.Direction.ASCENDING);
                } else if(bname.isChecked()){
                    query = mDb.collection(BOOKS)
                            .whereEqualTo("bookname", s.toString())
                            .orderBy("bookid", Query.Direction.ASCENDING);

                    FirestoreRecyclerOptions<Books> options = new FirestoreRecyclerOptions.Builder<Books>()
                            .setQuery(query, Books.class)
                            .build();
                    mAdapter.updateOptions(options);
                }  else if(bdis.isChecked()){
                    query = mDb.collection(BOOKS)
                            .whereEqualTo("bookdiscription", s.toString())
                            .orderBy("bookid", Query.Direction.ASCENDING);

                    FirestoreRecyclerOptions<Books> options = new FirestoreRecyclerOptions.Builder<Books>()
                            .setQuery(query, Books.class)
                            .build();
                    mAdapter.updateOptions(options);
                } else if(btype.isChecked()){
                    query = mDb.collection(BOOKS)
                            .whereEqualTo("booktype", s.toString())
                            .orderBy("bookid", Query.Direction.ASCENDING);

                    FirestoreRecyclerOptions<Books> options = new FirestoreRecyclerOptions.Builder<Books>()
                            .setQuery(query, Books.class)
                            .build();
                    mAdapter.updateOptions(options);
                }else {


                    query = mDb.collection(BOOKS)
                            .whereEqualTo("bookname", s.toString())
                            .orderBy("bookid", Query.Direction.ASCENDING);
                }
                FirestoreRecyclerOptions<Books> options = new FirestoreRecyclerOptions.Builder<Books>()
                        .setQuery(query, Books.class)
                        .build();
                mAdapter.updateOptions(options);
            }
        });






    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}
