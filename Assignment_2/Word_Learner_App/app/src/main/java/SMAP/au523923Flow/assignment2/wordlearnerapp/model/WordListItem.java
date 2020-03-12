package SMAP.au523923Flow.assignment2.wordlearnerapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Random;

// Parcelable makes it possible to send objects to other activities
// Parcelable implementation influnced by
// https://stackoverflow.com/questions/7181526/how-can-i-make-my-custom-objects-parcelable
// and (Some comments in the code from the post are copied here as well)
// https://dzone.com/articles/using-android-parcel
public class WordListItem implements Parcelable {
    private String word;
    private String pronunciation;
    private String description;
    private String rating;
    private String notes;
    private int imgResNbr;
    private int wordPosition;
    private float MIN_RATING = 0.00f, MAX_RATING = 10.00f;

    public WordListItem(String word, String pronunciation, String description){
        this.word = word;
        this.pronunciation = pronunciation;
        this.description = description;

        rating = getRandomRating();
    }

    //region Getters and setters
    public String getWord(){ return word; }
    public String getPronunciation(){ return "[" + pronunciation + "]"; }
    public String getRating(){ return rating; }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public int getImgResNbr(){ return imgResNbr; }
    public void setImgResNbr(int imgResNbr) {this.imgResNbr = imgResNbr;}
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public void setWordPosition(int wordPosition){this.wordPosition = wordPosition;}
    public int getWordPosition(){return wordPosition;}
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    //endregion

    // Not needed to implement
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(word);
        dest.writeString(pronunciation);
        dest.writeString(description);
        dest.writeString(rating);
        dest.writeString(notes);
        dest.writeInt(imgResNbr);
        dest.writeInt(wordPosition);
    }

    /** Static field used to regenerate object, individually or as arrays */
    public static final Parcelable.Creator<WordListItem> CREATOR = new Parcelable.Creator<WordListItem>() {
        public WordListItem createFromParcel(Parcel pc) {
            return new WordListItem(pc);
        }
        public WordListItem[] newArray(int size) {
            return new WordListItem[size];
        }
    };

    /**Ctor from Parcel, reads back fields IN THE ORDER they were written */
    public WordListItem(Parcel pc){
        word = pc.readString();
        pronunciation = pc.readString();
        description = pc.readString();
        rating = pc.readString();
        notes = pc.readString();
        imgResNbr = pc.readInt();
        wordPosition = pc.readInt();
    }

    private String getRandomRating(){
        // Random ratings inspired by
        // https://stackoverflow.com/questions/40431966/what-is-the-best-way-to-generate-a-random-float-value-included-into-a-specified
        Random random = new Random();
        float randomRating = MIN_RATING + random.nextFloat() * (MAX_RATING - MIN_RATING);
        // Formatting inspired from :
        // https://mkyong.com/java/how-to-round-double-float-value-to-2-decimal-points-in-java/
        double formattedRating = Math.round(randomRating * 10.0) / 10.0;
        return Double.toString(formattedRating);
    }
}
