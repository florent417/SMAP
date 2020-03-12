package SMAP.au523923Flow.assignment2.wordlearnerapp.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// Extracted with: http://www.jsonschema2pojo.org/

public class Word {

    @SerializedName("definitions")
    @Expose
    private List<Definition> definitions = null;
    @SerializedName("word")
    @Expose
    private String word;
    @SerializedName("pronunciation")
    @Expose
    private String pronunciation;

    //region Getters and setters
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