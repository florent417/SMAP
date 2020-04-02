package SMAP.au523923Flow.assignment2.wordlearnerapp.data;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Update;

import java.util.List;
import java.util.concurrent.ExecutionException;

import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Word;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.DbOperationsListener;

// The mindset behind making the repository and the callback flow
// is first of all to separate functionality, so async tasks implementations to the
// db are implemented here, instead of being implemented in the service.
// The callback flow is added to control what happens in the service, so it is
// possible to call e.g. broadcast functions only when the operations are done,
// and also to check if the operations were actually executed and handle it in the
// service if they weren't executed and e.g. null object is returned.

// Inspired by :
// https://codinginflow.com/tutorials/android/room-viewmodel-livedata-recyclerview-mvvm/part-4-repository
public class WordRepository {
    private static final String TAG = "WordRepository";

    private WordDAO wordDAO;

    // ########## Async task executors ##########
    //region Async task executors
    public WordRepository(Context context){
        WordLearnerDatabase wordLearnerDatabase = WordLearnerDatabase.getWordDbInstance(context);
        wordDAO = wordLearnerDatabase.wordDAO();
    }

    public void getAllWords(DbOperationsListener<List<Word>> listener) {
        GetAllWordsAsyncTask getAllWordsAsyncTask = new GetAllWordsAsyncTask(wordDAO, listener);
        getAllWordsAsyncTask.execute();
    }

    public void getWord(String word, DbOperationsListener<Word> listener) {
        GetWordAsyncTask getWordAsyncTask = new GetWordAsyncTask(wordDAO, listener);
        getWordAsyncTask.execute(word);
    }

    public void addWord(Word word){
        AddWordAsyncTask addWordAsyncTask = new AddWordAsyncTask(wordDAO);
        addWordAsyncTask.execute(word);
    }

    public void deleteWord(Word word, DbOperationsListener<Word> listener){
        DeleteWordAsyncTask deleteWordAsyncTask = new DeleteWordAsyncTask(wordDAO, listener, word);
        deleteWordAsyncTask.execute(word);
    }

    public void updateWord(Word word, DbOperationsListener<Word> listener){
        UpdateWordAsyncTask updateWordAsyncTask = new UpdateWordAsyncTask(wordDAO, listener);
        updateWordAsyncTask.execute(word);
    }

    public void addWords(List<Word> words, DbOperationsListener<List<Word>> listener){
        AddWordsAsyncTask addWordsAsyncTask = new AddWordsAsyncTask(wordDAO, listener);
        addWordsAsyncTask.execute(words);
    }
    //endregion

    // ########## Asynctasks implementations ##########
    //region AsyncTasks
    private class GetAllWordsAsyncTask extends AsyncTask<Void, Void, List<Word>> {
        private WordDAO wordDAO;
        private DbOperationsListener<List<Word>> listener;

        private GetAllWordsAsyncTask(WordDAO wordDAO, DbOperationsListener<List<Word>> listener){
            this.wordDAO = wordDAO;
            this.listener = listener;
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

        // Execute when db operations are done. This applies for most of the async task
        // implementations in this file
        @Override
        protected void onPostExecute(List<Word> words) {
            listener.DbOperationDone(words);
        }
    }

    private class GetWordAsyncTask extends AsyncTask<String, Void, Word> {
        private WordDAO wordDAO;
        private DbOperationsListener<Word> listener;

        private GetWordAsyncTask(WordDAO wordDAO, DbOperationsListener<Word> listener){
            this.wordDAO = wordDAO;
            this.listener = listener;
        }

        @Override
        protected Word doInBackground(String... words) {
            try {
                return wordDAO.getWord(words[0]);
            } catch (Exception e){
                Log.d(TAG, "failed to get " + words[0]);
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Word word) {
            listener.DbOperationDone(word);
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
                Log.d(TAG, "Failed to add " + words[0].getWord());
                e.printStackTrace();
            }
            return null;
        }

        // Since the listener is implemented in the api helper
        // it is not necessary to implement it here
    }

    private class DeleteWordAsyncTask extends AsyncTask<Word, Void, Word> {
        private WordDAO wordDAO;
        private DbOperationsListener<Word> listener;
        private Word word;

        private DeleteWordAsyncTask(WordDAO wordDAO, DbOperationsListener<Word> listener, Word word){
            this.wordDAO = wordDAO;
            this.listener = listener;
            this.word = word;
        }

        @Override
        protected Word doInBackground(Word... words) {
            try {
                // If successful return the word that has been added
                wordDAO.deleteWord(words[0]);
                Log.d(TAG, words[0].getWord() + "successfully deleted from db");
                return word;
            } catch (Exception e){
                Log.d(TAG, "Failed to delete " + words[0].getWord());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Word word) {
            listener.DbOperationDone(word);
        }
    }

    private class UpdateWordAsyncTask extends AsyncTask<Word, Void, Word> {
        private WordDAO wordDAO;
        private DbOperationsListener<Word> listener;

        private UpdateWordAsyncTask(WordDAO wordDAO, DbOperationsListener<Word> listener){
            this.wordDAO = wordDAO;
            this.listener = listener;
        }

        @Override
        protected Word doInBackground(Word... words) {
            try {
                wordDAO.updateWord(words[0]);
                return words[0];
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Word word) {
            listener.DbOperationDone(word);
        }
    }

    private class AddWordsAsyncTask extends AsyncTask<List<Word>, Void, List<Word>> {
        private WordDAO wordDAO;
        private DbOperationsListener<List<Word>> listener;

        private AddWordsAsyncTask(WordDAO wordDAO, DbOperationsListener<List<Word>> listener){
            this.wordDAO = wordDAO;
            this.listener = listener;
        }

        @Override
        protected List<Word> doInBackground(List<Word>... words) {
            try {
                wordDAO.addWords(words[0]);
                return words[0];
            } catch (Exception e){
                e.printStackTrace();
                Log.d(TAG, "Word not added see exception: " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Word> words) {
            listener.DbOperationDone(words);
        }
    }
    //endregion
}
