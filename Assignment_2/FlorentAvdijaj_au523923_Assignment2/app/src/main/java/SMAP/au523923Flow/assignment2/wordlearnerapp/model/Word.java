package SMAP.au523923Flow.assignment2.wordlearnerapp.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;
import java.util.Random;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// Extracted with: http://www.jsonschema2pojo.org/
// Parcelable makes it possible to send objects to other activities, and save that object
// for e.g. when the user flips the screen.
// Parcelable implementation influenced by
// https://stackoverflow.com/questions/7181526/how-can-i-make-my-custom-objects-parcelable
// and (Some comments in the code from the post are copied here as well)
// https://dzone.com/articles/using-android-parcel
@Entity (tableName = "word")
public class Word implements Parcelable {
    // No need to save to db, hence Ignore annotation
    @Ignore
    private float MIN_RATING = 0.00f, MAX_RATING = 10.00f;

    public Word (){
        rating = getRandomRating();
        notes = "";
    }

    // Primary usage: EditActivity, when flipping the screen
    public Word (Word otherWord){
        this.word = otherWord.getWord();
        this.definitions = otherWord.getDefinitions();
        this.pronunciation = otherWord.getPronunciation();
        this.rating = otherWord.getRating();
        this.notes = otherWord.getNotes();
    }

    // ########## Database variables and database annotations ##########
    //region variables and database annotations
    @SerializedName("definitions")
    @Expose
    private List<Definition> definitions;

    @PrimaryKey()
    @ColumnInfo(name = "word")
    @SerializedName("word")
    @Expose
    @NonNull
    private String word;

    @ColumnInfo(name = "pronunciation")
    @SerializedName("pronunciation")
    @Expose
    private String pronunciation;

    @ColumnInfo(name = "rating")
    private String rating;
    @ColumnInfo(name = "notes")
    private String notes;
    //endregion

    // ########## Getters and setters ##########
    //region Getters and setters
    // To be able to save the definitions to db
    // see ref in DefinitionConverter
    @TypeConverters(DefinitionConverter.class)
    public List<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
    }

    public Definition getFirstDefinition() {return definitions.get(0);}

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getRating() {return rating;}

    public void setRating(String rating) {this.rating = rating;}

    public String getNotes (){return notes;}

    public void setNotes(String notes) {this.notes = notes;}
    //endregion

    // ########## Parcel and Parcelable implementation ##########
    //region Parcel and Parcelable implementation
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dest.writeParcelableList(definitions,flags);
        }
        else {
            dest.writeList(definitions);
        }
        dest.writeString(word);
        dest.writeString(pronunciation);
        dest.writeString(rating);
        dest.writeString(notes);
    }

    /** Static field used to regenerate object, individually or as arrays */
    public static final Parcelable.Creator<Word> CREATOR = new Parcelable.Creator<Word>() {
        public Word createFromParcel(Parcel pc) {
            return new Word(pc);
        }
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    /**Ctor from Parcel, reads back fields IN THE ORDER they were written */
    public Word(Parcel pc){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            pc.readParcelableList(definitions, Definition.class.getClassLoader());
        }
        else {
            pc.readList(definitions, Definition.class.getClassLoader());
        }
        word = pc.readString();
        pronunciation = pc.readString();
        rating = pc.readString();
        notes = pc.readString();
    }
    //endregion

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