package SMAP.au523923Flow.assignment2.wordlearnerapp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Word;

// Inspired by the WeatherServiceDemos in this course from L6

public class WordJsonParser {

    public static Word parseWordJsonWithGson(String jsonString){
        Gson gson = new GsonBuilder().create();
        Word wordInfo = gson.fromJson(jsonString,Word.class);

        if(wordInfo != null){
            return wordInfo;
        }
        else{
            return null;
        }
    }

    // Maybe add for definition
}
