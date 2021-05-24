package com.example.android.booksearch;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles API Calls and extracts data from JSON
 */
public final class QueryUtilis {

    //For use in Log messages
    public static final String LOG_TAG = QueryUtilis.class.getSimpleName();


    /**
     * Private Constructor so that no instance of class QueryUtilis should be created
     * all the methods are static which can be accessed without an instance of class
     */
    private QueryUtilis() {

    }

    /**
     * @param urlString URL from which data is retreived
     * @return List of books are returned
     */
    public static List<Book> extractBooks(String urlString) {
        URL url = createURL(urlString);
        String jsonResponse = "";
        List<Book> Books = new ArrayList<>();

        try {
            jsonResponse = makeHTTPRequest(url);
            Books = ExtractJson(jsonResponse);
        } catch (IOException e) {
            Log.e(LOG_TAG, "IO Exception\n", e);
        }
        return Books;


    }

    private static URL createURL(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem with creating URL\n", e);
        }
        return url;
    }

    private static String makeHTTPRequest(URL url) throws IOException {

        if (url == null) {
            return " ";
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        String jsonResponse = "";
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(10000); // In miliseconds
            httpURLConnection.setConnectTimeout(15000);// In miliseconds
            httpURLConnection.connect();
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromInputStream(inputStream);

            } else {
                Log.e(LOG_TAG, "Response Code : " + responseCode + "\n");
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "IO Exception \n", e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;

    }

    private static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder jsonResponse = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                jsonResponse.append(line);
                line = bufferedReader.readLine();
            }

        }
        return jsonResponse.toString();
    }

    /**
     *
     * @param jsonResponse jsonResponse received from the server
     * @return List of books
     * Always check whether a particular key exits in JSON or not
     */
    private static List<Book> ExtractJson(String jsonResponse) {
        List<Book> Books = new ArrayList<>();
        if (jsonResponse.isEmpty()) {
            return Books;
        }
        try {
            JSONObject root = new JSONObject(jsonResponse);
            if(!root.has("items")){
                return Books;
            }
            JSONArray items = root.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                String title="",author="",imageURL="",infoURL="";


                JSONObject currentBook = items.getJSONObject(i);

                //Information regarding current book
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                //title of the book
                if(volumeInfo.has("title")){
                    title = volumeInfo.getString("title");
                }

                if(volumeInfo.has("authors")){
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    //Author of the book
                    author = authors.getString(0);
                }

                //imageLinks field may or maynot be available

                if(volumeInfo.has("imageLinks")){
                    JSONObject imageLinks=volumeInfo.getJSONObject("imageLinks");
                   StringBuilder tempImageURL=new StringBuilder(imageLinks.getString("thumbnail"));
                   //adding s at the end of http since http doesn't fetch images
                    tempImageURL.insert(4,'s');
                    imageURL=tempImageURL.toString();

                }
                else{
                    imageURL="https://image10.bizrate-images.com/resize?sq=60&uid=2216744464";
                }

                if(volumeInfo.has("previewLink")){
                    //More Information URL of the book
                    infoURL=volumeInfo.getString("previewLink");

                }

                Books.add(new Book(title, author,imageURL,infoURL));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON Exception\n", e);
        }
        return Books;

    }

}
