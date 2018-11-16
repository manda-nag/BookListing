package com.vmanda.booklisting;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

public class BookListingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<BookInfo>> {

    private ImageButton buttonSearch;
    private EditText editTextSearch;

    RecyclerView booksRecyclerView;
    BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSearch = findViewById(R.id.button_search);
        editTextSearch = findViewById(R.id.edit_text_search);
        booksRecyclerView = findViewById(R.id.books_recycler_view);

        bookAdapter = new BookAdapter();
        booksRecyclerView.setHasFixedSize(true);
        booksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getLoaderManager().initLoader(1, null, BookListingActivity.this);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                String searchText = editTextSearch.getText().toString();
                searchText = searchText.trim().replaceAll("\\s", "+");
                if(searchText.trim().length() == 0){
                    Toast.makeText(BookListingActivity.this, "Enter valid search string", Toast.LENGTH_SHORT).show();
                    return;
                }
                // search with the searchText
                String url = searchText.trim().replaceAll("\\s", "+");
                if(isNetworkConnected()) {
                    BookLoaderTask.searchString = searchText;
                    getLoaderManager().restartLoader(1, null, BookListingActivity.this);
                }
                else
                    Toast.makeText(BookListingActivity.this, "Network not connected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isNetworkConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public Loader<List<BookInfo>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoaderTask(this);
    }

    @Override
    public void onLoadFinished(Loader<List<BookInfo>> loader, List<BookInfo> bookInfos) {
        bookAdapter.setBooks(bookInfos);
        booksRecyclerView.setAdapter(bookAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<BookInfo>> loader) {

    }

}
