package com.example.android.miwok;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class WordAdaptor extends ArrayAdapter<Word> {

    private int mColorId;

    public WordAdaptor(Activity context, ArrayList<Word> words, int color) {
        super(context, 0, color, words);
        mColorId = color;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item, parent, false);
        }


        Word currentWord = getItem(position);

        TextView defaultWordView = (TextView) listItemView.findViewById(R.id.default_text_view);

        defaultWordView.setText(currentWord.getDefaultTranslation());

        TextView miwokWordView = (TextView) listItemView.findViewById(R.id.miwok_text_view);

        miwokWordView.setText(currentWord.getmMiwokTranslation());

        ImageView iconView = (ImageView) listItemView.findViewById(R.id.list_img);

        Log.i("check get View value", "" + currentWord.hasImage());

        if (currentWord.hasImage()) {

            iconView.setVisibility(View.VISIBLE);

            iconView.setImageResource(currentWord.getImageResourceID());

        } else {
            iconView.setVisibility(View.GONE);
        }

        View textContainer = listItemView.findViewById(R.id.text_container);

        int colorBackground = ContextCompat.getColor(getContext(), mColorId);

        textContainer.setBackgroundColor(colorBackground);

        return listItemView;
    }


}
