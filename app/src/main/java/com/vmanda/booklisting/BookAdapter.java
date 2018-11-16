package com.vmanda.booklisting;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<BookInfo> books;

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_row_item,
                                                                    parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        BookInfo book = books.get(position);
        holder.textViewTitle.setText(book.getTitle());
        holder.textViewAuthor.setText(book.getAuthor());
        if(!book.getImageUrl().isEmpty())
            Picasso.get().load(book.getImageUrl()).into(holder.imageBook);
    }

    public void setBooks(List<BookInfo> books){
        this.books = books;
    }

    @Override
    public int getItemCount() {
        if(books == null ) return  0;
        return books.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView textViewTitle;
        final TextView textViewAuthor;
        final ImageView imageBook;

        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.book_title);
            textViewAuthor = itemView.findViewById(R.id.book_author);
            imageBook = itemView.findViewById(R.id.book_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String url = books.get(getAdapterPosition()).getUrl();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);
        }
    }
}
