package com.vmanda.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


class BookLoaderTask extends AsyncTaskLoader<List<BookInfo>> {

    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    public static String searchString;

    public BookLoaderTask(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    public List<BookInfo> loadInBackground() {
        if(searchString == null)
            return null;

        List<BookInfo> bookList;
        String JSONResponse = "";

        try {
            URL url = new URL(BASE_URL + searchString);
            JSONResponse = makeHttpRequest(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        bookList = parseJSON(JSONResponse);
        return bookList;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    private List<BookInfo> parseJSON(String json) {
        List<BookInfo> books = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(json);

            if (jsonResponse.getInt("totalItems") == 0) {
                return books;
            }
            JSONArray jsonArray = jsonResponse.getJSONArray("items");

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject items = jsonArray.getJSONObject(i);

                JSONObject bookInfo = items.getJSONObject("volumeInfo");

                String title = bookInfo.getString("title");
                StringBuilder author =  new StringBuilder();
                if(bookInfo.has("authors")) {
                    JSONArray authors = bookInfo.getJSONArray("authors");
                    for (int j = 0; j < authors.length(); j++) {
                        if (j == 0)
                            author.append(authors.getString(j));
                        else
                            author.append(", ").append(authors.getString(j));
                    }
                }

                String url = "";
                if(items.has("accessInfo")) {
                    JSONObject accessInfo = items.getJSONObject("accessInfo");
                    url = accessInfo.getString("webReaderLink");
                }

                String imageUrl = "";
                if(bookInfo.has("imageLinks")){
                    JSONObject imageLinks = bookInfo.getJSONObject("imageLinks");
                    imageUrl = imageLinks.getString("smallThumbnail");
                }

                BookInfo book = new BookInfo(title, author.toString(), url, imageUrl);
                books.add(book);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }

    private String makeHttpRequest(URL url) {
        String jsonResponse = "";
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(10000);
            connection.connect();
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream inputStream = connection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonResponse;


    }

    private String readFromStream(InputStream inputStream) {
        StringBuilder result = new StringBuilder();
        try {
            InputStreamReader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            line = bufferedReader.readLine();
            while(line != null){
                result.append(line);
                line = bufferedReader.readLine();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
