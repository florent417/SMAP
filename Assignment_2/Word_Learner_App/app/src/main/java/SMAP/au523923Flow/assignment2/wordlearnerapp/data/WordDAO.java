package SMAP.au523923Flow.assignment2.wordlearnerapp.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Word;

@Dao
public interface WordDAO {
    @Query("SELECT * FROM word")
    List<Word> getAllWords();

    // :word is the parameter
    @Query("SELECT * FROM word WHERE word LIKE :word LIMIT 1")
    Word getWord(String word);

    @Delete
    void deleteWord(Word word);

    @Update
    void updateWord(Word word);

    @Insert
    void addWord(Word... word);

    @Insert
    void addWords(List<Word> words);
}
