package com.keshava.cloudbrary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;



public class BooksrecycleAdapter extends FirestoreRecyclerAdapter<Books,BooksrecycleAdapter.booksViewHolder> {



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


        booksViewHolder(CardView v) {
            super(v);
            view = v;
            bookname = v.findViewById(R.id.book_name);
            bookprice = v.findViewById(R.id.book_price);
            bookavalibilty = v.findViewById(R.id.book_avalibility);

        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final booksViewHolder holder, @NonNull int position, @NonNull final Books books) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bookname.setText(books.getBookname());
        holder.bookprice.setText(books.getBookprice());
        holder.bookavalibilty.setText(books.getBookcount());
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
