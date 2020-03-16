package SMAP.au523923Flow.assignment2.wordlearnerapp.model;

import androidx.annotation.NonNull;
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
public class Word {

    //@PrimaryKey(autoGenerate = true)
    //public int wordId;

    // need a typeconverter
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

    //region Getters and setters
    @TypeConverters(DefinitionConverter.class)
    public List<Definition> getDefinitions() {
        return definitions;
    }

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
    //endregion
}