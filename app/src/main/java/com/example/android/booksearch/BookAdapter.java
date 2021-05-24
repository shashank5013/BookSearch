package com.example.android.booksearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    /**
     * @param context Activity's Context
     * @param books   List of books
     */
    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    /**
     * @param position    position of the object whose information is needed
     * @param convertView Recycled view
     * @param parent      Parent of the recycled view
     * @return Reused view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View list_item = convertView;
        Book currentBook = getItem(position);

        //If View is empty creating a new view
        if (list_item == null) {
            list_item = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        //Setting Title of the book
        TextView title = (TextView) list_item.findViewById(R.id.title);
        title.setText(currentBook.getTitle());

        //Setting Author of the book
        TextView author = (TextView) list_item.findViewById(R.id.author);
        author.setText(currentBook.getAuthor());

        //Setting image of book using Picasso library
        String imageURL=currentBook.getImageURL();
            ImageView imageView=(ImageView)list_item.findViewById(R.id.book_image);
            Picasso.get().load(imageURL).into(imageView);

        return list_item;

    }
}
