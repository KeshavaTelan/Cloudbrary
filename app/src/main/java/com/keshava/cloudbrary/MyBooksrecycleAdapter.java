package com.keshava.cloudbrary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class MyBooksrecycleAdapter extends FirestoreRecyclerAdapter<Books, MyBooksrecycleAdapter.MybooksViewHolder> {


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();



    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    private final OnItemClickListener listener;

    MyBooksrecycleAdapter(FirestoreRecyclerOptions<Books> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }

    MyBooksrecycleAdapter(FirestoreRecyclerOptions<Books> options) {
        super(options);
        this.listener = null;
    }

    class MybooksViewHolder extends RecyclerView.ViewHolder {
        final CardView view;
        final TextView bookname;
        final ImageView bookimage;
        final Button bookbuy;


        MybooksViewHolder(CardView v) {
            super(v);
            view = v;
            bookname = v.findViewById(R.id.item_user_id_label);
            bookimage=v.findViewById(R.id.mybook_img);
            bookbuy=v.findViewById(R.id.mybookybuy);


        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MybooksViewHolder holder, @NonNull int position, @NonNull final Books books) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bookname.setText(books.getBookname());

        Picasso.get().load(books.getBookimg()).into(holder.bookimage);


        DocumentReference btnvali2 = db.collection("users").document( mAuth.getCurrentUser().getUid());
        btnvali2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Map<String, Object> friendsMapm = document.getData();
                        for (Map.Entry<String, Object> entry : friendsMapm.entrySet()) {
                            if (entry.getKey().equals("mybooks")) {
                                Map<String, Object> newFriend0Mapm = (Map<String, Object>) entry.getValue();
                                for (Map.Entry<String, Object> e : newFriend0Mapm.entrySet()) {
                                    if (!e.getKey().equals(books.getBookid())) {
                                        holder.view.setVisibility(View.GONE);

//                                holder.view.setVisibility(View.GONE);

                                    }
                                    else if(e.getKey().equals(books.getBookid())) {
                                        holder.view.setVisibility(View.VISIBLE);
//                        holder.bookbuy.setEnabled(false);


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
                int newcount = books.getBookcount()+1;
//

                DocumentReference documentReference =db.collection("Books").document(books.getBookid());
                documentReference.update("bookcount", newcount);

                DocumentReference addbooktouser =db.collection("users").document(uid);
                Map <String, Object> updates = new HashMap<>();
                updates.put("mybooks."+books.getBookid(), FieldValue.delete());
                addbooktouser.update(updates);
//                        ("mybooks."+books.getBookid(),1);



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


//    @Override
//    protected void onLoadingStateChanged(@NonNull LoadingState state) {
//
//        super.onLoadingStateChanged(state);
//        switch (state){
//            case LOADING_INITIAL:
//                Log.d("adapterwork","loading more");
//                break;
//            case LOADING_MORE:
//                Log.d("adapterwork","next page");
//                break;
//            case FINISHED:
//                Log.d("adapterwork","finsih");
//                break;
//            case ERROR:
//                Log.d("adapterwork","nop");
//                break;
//            case LOADED:
//                Log.d("adapterwork","yep");
//                break;
//        }
//    }

    @NonNull
    @Override
    public MybooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mybook_list, parent, false);

        return new MybooksViewHolder(v);
    }
}
