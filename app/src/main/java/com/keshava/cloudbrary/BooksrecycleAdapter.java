package com.keshava.cloudbrary;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BooksrecycleAdapter extends FirestoreRecyclerAdapter<Books,BooksrecycleAdapter.booksViewHolder> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    private final OnItemClickListener listener;

    BooksrecycleAdapter(FirestoreRecyclerOptions<Books> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }

    BooksrecycleAdapter(FirestoreRecyclerOptions<Books> options) {
        super(options);
        this.listener = null;
    }

    class booksViewHolder extends RecyclerView.ViewHolder {
        final CardView view;
        final TextView bookname;
        final TextView bookprice;
        final TextView bookavalibilty;
        final ImageView bookimage;
        final Button bookbuy;


        booksViewHolder(CardView v) {
            super(v);
            view = v;
            bookname = v.findViewById(R.id.book_name);
            bookprice = v.findViewById(R.id.book_price);
            bookavalibilty = v.findViewById(R.id.book_avalibility);
            bookimage=v.findViewById(R.id.book_img);
            bookbuy=v.findViewById(R.id.bookybuy);


        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final booksViewHolder holder, @NonNull int position, @NonNull final Books books) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bookname.setText(books.getBookname());
        holder.bookprice.setText(books.getBookprice());
        holder.bookavalibilty.setText(Integer.toString( books.getBookcount()));

        Picasso.get().load(books.getBookimg()).into(holder.bookimage);


        DocumentReference btnvali = db.collection("users").document( mAuth.getCurrentUser().getUid());
        btnvali.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                     Map<String, Object> friendsMap = document.getData();
                for (Map.Entry<String, Object> entry : friendsMap.entrySet()) {
                    if (entry.getKey().equals("mybooks")) {
                        Map<String, Object> newFriend0Map = (Map<String, Object>) entry.getValue();
                        for (Map.Entry<String, Object> e : newFriend0Map.entrySet()) {
                            if (e.getKey().equals(books.getBookid())) {


                                holder.bookbuy.setEnabled(false);


                            }
//                                Map<String, Object> fNameMap = (Map<String, Object>) e.getValue();
//                                for (Map.Entry<String, Object> dataEntry : fNameMap.entrySet()) {
//                                    if (dataEntry.getKey().equals("fName")) {
//                                        Log.d("TAG", dataEntry.getValue().toString());
//                                    }
//                                }
                            }
                        }
                    }



                    } else {

                    }
                } else {

                }
            }
        });







        holder.bookbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String uid =  mAuth.getCurrentUser().getUid();
              int newcount = books.getBookcount()-1;
//

                DocumentReference documentReference =db.collection("Books").document(books.getBookid());
                documentReference.update("]bookcount", newcount);

                DocumentReference addbooktouser =db.collection("users").document(uid);

//


                addbooktouser.update("mybooks."+books.getBookid(),1);


                holder.bookbuy.setEnabled(false);
//                meka adu vela eka db eke update venna one
//                userge collection eke gatta potha add venna one list ekak vage


            }
        });


        if (listener != null) {
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(holder.getAdapterPosition());

                }
            });
        }
    }




    @NonNull
    @Override
    public booksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list, parent, false);

        return new booksViewHolder(v);
    }
}
