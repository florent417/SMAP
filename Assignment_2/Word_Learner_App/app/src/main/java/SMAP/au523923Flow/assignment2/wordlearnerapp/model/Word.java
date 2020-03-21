package SMAP.au523923Flow.assignment2.wordlearnerapp.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// Extracted with: http://www.jsonschema2pojo.org/
@Entity (tableName = "word")
public class Word implements Parcelable {
    //@PrimaryKey(autoGenerate = true)
    //public int wordId;

    //@Ignore
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

    private Definition firstDefinition;

    //region Getters and setters
    @TypeConverters(DefinitionConverter.class)
    public List<Definition> getDefinitions() {
        return definitions;
    }

    // add a method to return the first definition

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
    }

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

    public Definition getFirstDefinition() {return firstDefinition;}

    public void setFirstDefinition(){firstDefinition = definitions.get(0);}

    //endregion

    //region Parcel and Parcelable implementation
    // Not needed to implement
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dest.writeParcelableList(definitions,flags);
        }
        dest.writeString(word);
        dest.writeString(pronunciation);
        dest.write(firstDefinition);
        dest.writeString(rating);
        dest.writeString(notes);
        dest.writeInt(imgResNbr);
        dest.writeInt(wordPosition);
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
        word = pc.readString();
        pronunciation = pc.readString();
        description = pc.readString();
        rating = pc.readString();
        notes = pc.readString();
        imgResNbr = pc.readInt();
        wordPosition = pc.readInt();
    }
    //endregion
}