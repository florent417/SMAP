package com.flow.assignment1.wordlearnerapp;

public class WordListItem {
    private String word, pronounciation, rating;
    private int imgResNbr;

    public WordListItem(String word, String pronounciation, String rating, int imgResNbr){
        this.word = word;
        this.pronounciation = pronounciation;
        this.rating = rating;
        this.imgResNbr = imgResNbr;
    }

    public String getWord(){ return word; }
    public String getPronounciation(){ return pronounciation; }
    public String getRating(){ return rating; }
    public int getImgResNbr(){ return imgResNbr; }
}
