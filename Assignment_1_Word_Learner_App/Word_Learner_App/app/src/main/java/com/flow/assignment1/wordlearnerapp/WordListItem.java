package com.flow.assignment1.wordlearnerapp;

public class WordListItem {
    private String word, pronunciation, rating;
    private int imgResNbr;


    public WordListItem(String word, String pronounciation, String rating, int imgResNbr){
        this.word = word;
        this.pronunciation = pronounciation;
        this.rating = rating;
        this.imgResNbr = imgResNbr;
    }

    public String getWord(){ return word; }
    public String getPronunciation(){ return "[" + pronunciation + "]"; }
    public String getRating(){ return rating; }
    public int getImgResNbr(){ return imgResNbr; }
}
