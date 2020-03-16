package SMAP.au523923Flow.assignment2.wordlearnerapp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Definition;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.DefinitionConverter;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Word;

// Inspired by: https://developer.android.com/training/data-storage/room
// Maybe change to include definitions
@Database(entities = {Word.class}, version = 1)
@TypeConverters({DefinitionConverter.class})
public abstract class WordLearnerDatabase extends RoomDatabase {
    private static WordLearnerDatabase dbInstance;
    private static final String DATABASE_NAME = "Word-Learner_Database";

    public abstract WordDAO wordDAO();

    public static synchronized WordLearnerDatabase getWordDbInstance(Context context){
        if (dbInstance == null){
            dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                    WordLearnerDatabase.class, DATABASE_NAME)
                    .build();
        }
        return dbInstance;
    }

}
