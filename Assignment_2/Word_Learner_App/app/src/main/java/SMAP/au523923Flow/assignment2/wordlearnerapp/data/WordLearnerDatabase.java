package SMAP.au523923Flow.assignment2.wordlearnerapp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Definition;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.DefinitionConverter;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Word;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.Globals;

// Inspired by: https://developer.android.com/training/data-storage/room
// Maybe change to include definitions
@Database(entities = {Word.class}, version = 6)
@TypeConverters({DefinitionConverter.class})
public abstract class WordLearnerDatabase extends RoomDatabase {

    private static WordLearnerDatabase dbInstance;

    public abstract WordDAO wordDAO();

    public static synchronized WordLearnerDatabase getWordDbInstance(Context context){
        if (dbInstance == null){
            dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                    WordLearnerDatabase.class, Globals.DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return dbInstance;
    }

}
