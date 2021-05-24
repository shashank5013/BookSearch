package com.example.android.booksearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    // For use in Log messages
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    //Loder ID
    private static final int LOADER_ID = 1;

    //API URL
    private String API_URL = "https://www.googleapis.com/books/v1/volumes?maxResults=20&q=";

    //Adapter to be set on list view
    private BookAdapter bookAdapter;

    //EditText field in which search keyword is entered
    private SearchView keyword;

    //Progressbar field which shows the progress of search
    private ProgressBar progressBar;

    //TextView for empty state view of list view
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hiding the Action Bar
        getSupportActionBar().hide();

        //Initialising Global Variables
        keyword = findViewById(R.id.keyword);
        bookAdapter = new BookAdapter(this, new ArrayList<>());
        progressBar=findViewById(R.id.loading_sign);
        emptyView=findViewById(R.id.empty_view);

        ListView listView = findViewById(R.id.list);
        listView.setAdapter(bookAdapter);

        listView.setEmptyView(emptyView);





        /**
         * Setting OnClickListener for searching queries whenever
         * search button is clicked
         */
        keyword.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            /**
             *
             * @param query to be searched
             */
            @Override
            public boolean onQueryTextSubmit(String query) {
                getSupportLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
                return false;
            }

            /**
             *
             * @param newText Search query whenever text changes in searchview
             */
            @Override
            public boolean onQueryTextChange(String newText) {
                //Clearing the bookadapter
                bookAdapter.clear();
                emptyView.setText("");

                //Checking if internet is present
                ConnectivityManager cm=(ConnectivityManager)MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork=cm.getActiveNetworkInfo();
                boolean isConnected=(activeNetwork!=null && activeNetwork.isConnectedOrConnecting());
                if(!isConnected){
                   emptyView.setText("NO INTERNET");
                }
                else{
                    //If internet is present then searching the query
                    progressBar.setVisibility(View.VISIBLE);
                    getSupportLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
                }


                return false;
            }
        });

        /**
         * Setting OnClickListener for each of the list item
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book currentBook=(Book)parent.getItemAtPosition(position);
                Intent intent=new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY,currentBook.getInfoURL());
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivity(intent);
                }
            }
        });


    }

    //Loader related calls
    @NonNull
    @Override
    public Loader<List<Book>> onCreateLoader(int id, @Nullable Bundle args) {
        BookLoader bookLoader = new BookLoader(MainActivity.this, API_URL + keyword.getQuery());
        return bookLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Book>> loader, List<Book> data) {
        progressBar.setVisibility(View.GONE);
        if(data.size()==0){
            TextView textView=findViewById(R.id.empty_view);
            textView.setText("No Books Found");
        }
        else{
            bookAdapter.clear();
            bookAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Book>> loader) {
        bookAdapter.clear();

    }
}