package SMAP.au523923Flow.assignment2.wordlearnerapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static androidx.room.ForeignKey.CASCADE;

// Extracted with: http://www.jsonschema2pojo.org/
//@Entity(tableName = "definition", foreignKeys = @ForeignKey(entity = Word.class, parentColumns = "wordId", childColumns = "definitionId", onDelete = CASCADE))
public class Definition implements Parcelable {

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

    //region Parcel and Parcelable implementation
    // Not needed to implement
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(definition);
        dest.writeString(example);
        dest.writeString(imageUrl);
        dest.writeString(emoji);
    }

    /** Static field used to regenerate object, individually or as arrays */
    public static final Parcelable.Creator<Definition> CREATOR = new Parcelable.Creator<Definition>() {
        public Definition createFromParcel(Parcel pc) {
            return new Definition(pc);
        }
        public Definition[] newArray(int size) {
            return new Definition[size];
        }
    };

    /**Ctor from Parcel, reads back fields IN THE ORDER they were written */
    public Definition(Parcel pc){
        type = pc.readString();
        definition = pc.readString();
        example = pc.readString();
        imageUrl = pc.readString();
        emoji = pc.readString();
    }
    //endregion
}