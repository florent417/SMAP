package SMAP.au523923Flow.assignment2.wordlearnerapp.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

// Inspired by: https://stackoverflow.com/questions/44582397/android-room-persistent-library-typeconverter-error-of-error-cannot-figure-ou
public class DefinitionConverter {

    @TypeConverter
    public String fromDefinitionList(List<Definition> definitions){
        if (definitions == null){
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Definition>>(){}.getType();
        String json = gson.toJson(definitions,type);
        return json;
    }

    @TypeConverter
    public List<Definition> toDefinitionList(String definitionsListStr){
        if (definitionsListStr == null){
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Definition>>(){}.getType();
        List<Definition> definitionList = gson.fromJson(definitionsListStr, type);
        return definitionList;
    }
}
