package com.example.android.booksearch;

public class Book {

    // Title of the book
    private String mTitle;

    //Author of the book
    private String mAuthor;

    //URL of the image
    private String mImageURL;

    private String mInfoURL;

    //Default Constructor
    public Book(String title, String author,String ImageURL, String InfoURL) {
        mTitle = title;
        mAuthor = author;
        mImageURL=ImageURL;
        mInfoURL=InfoURL;
    }

    /**
     * @return Title of the book
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * @return Author of the book
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     *
     * @return URL of the image
     */
    public String getImageURL(){
        return mImageURL;
    }

    public String getInfoURL(){
        return mInfoURL;
    }


}
