package com.flow.assignment1.wordlearnerapp;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

// To seperate concerns. Even though the context is passed to this class, the seperation is a nice
// extra layer to seperate functionality

public class DataHelper {
    private Context context;

    public DataHelper(Context context) {
        this.context = context;
    }

    // Inspiration :
    // https://stackoverflow.com/questions/52899516/android-how-to-search-image-name-string-in-drawable-directory-and-show-it-in
    private int setImgResFromWord (String word){
        int resId = context.getResources().getIdentifier(word, "drawable", context.getPackageName());
        return resId;
    }

    // Heavily inspired from this video:
    // https://www.youtube.com/watch?v=i-TqNzUryn8
    // and post
    // https://stackoverflow.com/questions/43055661/reading-csv-file-in-android-app
    public ArrayList<WordListItem> addDataFromFile(){
        ArrayList<WordListItem> wordListItems = new ArrayList<WordListItem>();
        BufferedReader br;
        InputStream inputStream = context.getResources().openRawResource(R.raw.animal_list);
        br = new BufferedReader(
                new InputStreamReader(inputStream, Charset.forName("UTF-8"))
        );
        String currentLine;
        try {
            while ((currentLine = br.readLine()) != null) {
                String[] tokens = currentLine.split(";");
                String word = tokens[0],pronunciation = tokens[1], description = tokens[2];

                WordListItem wordItem = new WordListItem(word, pronunciation, description);
                wordItem.setImgResNbr(setImgResFromWord(wordItem.getWord().toLowerCase()));
                wordListItems.add(wordItem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return wordListItems;
    }
}
