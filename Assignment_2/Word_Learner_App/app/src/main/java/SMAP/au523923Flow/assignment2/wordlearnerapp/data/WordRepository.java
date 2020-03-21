package SMAP.au523923Flow.assignment2.wordlearnerapp.data;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Update;

import java.util.List;
import java.util.concurrent.ExecutionException;

import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Word;
// Inspired by :
// https://codinginflow.com/tutorials/android/room-viewmodel-livedata-recyclerview-mvvm/part-4-repository
public class WordRepository {
    private WordDAO wordDAO;

    public WordRepository(Context context){
        WordLearnerDatabase wordLearnerDatabase = WordLearnerDatabase.getWordDbInstance(context);
        wordDAO = wordLearnerDatabase.wordDAO();
    }

    public List<Word> getAllWords() {
        GetAllWordsAsyncTask getAllWordsAsyncTask = new GetAllWordsAsyncTask(wordDAO);
        try {
            return getAllWordsAsyncTask.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Word getWord(Word word) {
        GetWordAsyncTask getWordAsyncTask = new GetWordAsyncTask(wordDAO);
        try {
            return getWordAsyncTask.execute(word).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addWord(Word word){
        AddWordAsyncTask addWordAsyncTask = new AddWordAsyncTask(wordDAO);

        addWordAsyncTask.execute(word);
    }

    public void addWords(List<Word> words){
        AddWordsAsyncTask addWordsAsyncTask = new AddWordsAsyncTask(wordDAO);

        addWordsAsyncTask.execute(words);
    }

    public void deleteWord(Word word){
        DeleteWordAsyncTask deleteWordAsyncTask = new DeleteWordAsyncTask(wordDAO);

        deleteWordAsyncTask.execute(word);
    }

    public void updateWord(Word word){
        UpdateWordAsyncTask updateWordAsyncTask = new UpdateWordAsyncTask(wordDAO);

        updateWordAsyncTask.execute(word);
    }

    private class GetAllWordsAsyncTask extends AsyncTask<Void, Void, List<Word>> {
        private WordDAO wordDAO;

        private GetAllWordsAsyncTask(WordDAO wordDAO){
            this.wordDAO = wordDAO;
        }

        @Override
        protected List<Word> doInBackground(Void... voids) {
            try {
                return wordDAO.getAllWords();
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private class GetWordAsyncTask extends AsyncTask<Word, Void, Word> {
        private WordDAO wordDAO;

        private GetWordAsyncTask(WordDAO wordDAO){
            this.wordDAO = wordDAO;
        }

        @Override
        protected Word doInBackground(Word... words) {
            try {
                return wordDAO.getWord(words[0].getWord());
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private class AddWordAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDAO wordDAO;

        private AddWordAsyncTask(WordDAO wordDAO){
            this.wordDAO = wordDAO;
        }

        @Override
        protected Void doInBackground(Word... words) {
            try {
                wordDAO.addWord(words[0]);
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private class DeleteWordAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDAO wordDAO;

        private DeleteWordAsyncTask(WordDAO wordDAO){
            this.wordDAO = wordDAO;
        }

        @Override
        protected Void doInBackground(Word... words) {
            try {
                wordDAO.deleteWord(words[0]);
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private class UpdateWordAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDAO wordDAO;

        private UpdateWordAsyncTask(WordDAO wordDAO){
            this.wordDAO = wordDAO;
        }

        @Override
        protected Void doInBackground(Word... words) {
            try {
                wordDAO.updateWord(words[0]);
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private class AddWordsAsyncTask extends AsyncTask<List<Word>, Void, Void> {
        private WordDAO wordDAO;

        private AddWordsAsyncTask(WordDAO wordDAO){
            this.wordDAO = wordDAO;
        }

        @Override
        protected Void doInBackground(List<Word>... words) {
            try {
                wordDAO.addWords(words[0]);
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
