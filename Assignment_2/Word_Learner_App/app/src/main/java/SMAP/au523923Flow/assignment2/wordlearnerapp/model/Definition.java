package SMAP.au523923Flow.assignment2.wordlearnerapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
// Extracted with: http://www.jsonschema2pojo.org/
public class Definition {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("definition")
    @Expose
    private String definition;
    @SerializedName("example")
    @Expose
    private String example;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
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