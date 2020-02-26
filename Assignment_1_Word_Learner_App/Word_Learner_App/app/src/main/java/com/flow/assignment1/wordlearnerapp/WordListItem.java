package com.flow.assignment1.wordlearnerapp;

public class WordListItem {
    private String word, pronunciation, rating;
    private int imgResNbr;

    public void setRating(String rating) {
        this.rating = rating;
    }
// Set default rating to 0.0 and add getters and setters


    public WordListItem(String word, String pronunciation, String rating, int imgResNbr){
        this.word = word;
        this.pronunciation = pronunciation;
        this.rating = rating;
        this.imgResNbr = imgResNbr;
    }

    public String getWord(){ return word; }
    public String getPronunciation(){ return "[" + pronunciation + "]"; }
    public String getRating(){ return rating; }
    public int getImgResNbr(){ return imgResNbr; }
}
