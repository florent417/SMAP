package SMAP.au523923Flow.assignment2.wordlearnerapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static androidx.room.ForeignKey.CASCADE;

// Extracted with: http://www.jsonschema2pojo.org/
//@Entity(tableName = "definition", foreignKeys = @ForeignKey(entity = Word.class, parentColumns = "wordId", childColumns = "definitionId", onDelete = CASCADE))
public class Definition {

    //@PrimaryKey(autoGenerate = true)
    //public int definitionId;

    @ColumnInfo(name = "type")
    @SerializedName("type")
    @Expose
    private String type;

    @ColumnInfo(name = "definition")
    @SerializedName("definition")
    @Expose
    private String definition;

    @ColumnInfo(name = "example")
    @SerializedName("example")
    @Expose
    private String example;

    @ColumnInfo(name = "image_url")
    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    @ColumnInfo(name = "emoji")
    @SerializedName("emoji")
    @Expose
    private String emoji;

    //region Getters and setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }
    //endregion
}