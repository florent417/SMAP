package SMAP.au523923Flow.assignment2.wordlearnerapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.stetho.Stetho;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import SMAP.au523923Flow.assignment2.wordlearnerapp.data.WordLearnerDatabase;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Word;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.Globals;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.WordAPIHelper;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.WordJsonParser;

// Ref : https://developer.android.com/guide/components/bound-services

/*
When the last client unbinds from the service,
the system destroys the service, unless the service was also started by startService().
*/

// OnDestroy gets called

public class WordLearnerService extends Service {
    public static final String BROADCAST_WORDLEARNERSERVICE = "SMAP.au523923Flow.assignment2.wordlearnerapp.service.BROADCAST_WORDLEARNERSERVICE";
    private static final String TAG = "WordLearnerService";
    private static final String DATABASE_NAME = "Word-Learner_Database";
    // Binder given to clients
    private final IBinder binder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    // To keep it running forever? or make it as a foreground service
    // onDestroy still called when leaving listActivity
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    // use another name
    public class LocalBinder extends Binder {
        public WordLearnerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return WordLearnerService.this;
        }
    }

    public void testAddWord(String word){
        WordAPIHelper.getInstance(this).addToRequestQueue(setupStringReq(word));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: called");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(getApplicationContext());
    }

    // This probably needs to be in the list activity class, since the view has to be updated
    // It works though
    // This method exists: stringRequest.hasHadResponseDelivered();
    private StringRequest setupStringReq(String word){
        // Test word
        String url = Globals.OWLBOT_API_CALL + word;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            apiRespListener, apiErrRespListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //return super.getHeaders();
                Map<String, String> params = new HashMap<>();
                params.put(Globals.OWLBOT_HEADER_AUTH_KEY, Globals.OWLBOT_HEADER_AUTH_VAL);
                return params;
            }
        };
        
        return stringRequest;
    }

    private Response.Listener<String> apiRespListener = new Response.Listener<String>(){
        @Override
        public void onResponse(String response) {
            Word wordObj = WordJsonParser.parseWordJsonWithGson(response);
            startDbAddWordTask(wordObj);
        }
    };

    // Had to do it like this, otherwise the service could not be retrieved from the responseListener
    private void startDbAddWordTask(Word wordObj){
        DbAddWordTask dbAddWordTask = new DbAddWordTask(this);
        dbAddWordTask.execute(wordObj);
    }

    private Response.ErrorListener apiErrRespListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            //Toast.makeText(ListActivity.this,"fail", Toast.LENGTH_LONG).show();
            // What goes here?
        }
    };

    //send local broadcast
    private void broadcastTaskResult(){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(BROADCAST_WORDLEARNERSERVICE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    // Inspired by and some stuff copied from servicesdemo code from L5 in SMAP
    private class DbAddWordTask extends AsyncTask<Word, Void, Void> {

        //WeakReference is Java's way of indicating that the referenced object can be garbage collected if needed
        //we need this to avoid holding onto the service if the asynch task goes on (causing memory leak)
        private WeakReference<WordLearnerService> serviceRef;

        // only retain a weak reference to the activityReference
        DbAddWordTask(WordLearnerService service) {
            serviceRef = new WeakReference<>(service);
        }

        @Override
        protected Void doInBackground(Word... words) {
            // Check if word is already in database orrrr in the list that is in the service
            WordLearnerDatabase.getWordDbInstance(getApplicationContext())
                    .wordDAO().addWord(words[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            WordLearnerService service = serviceRef.get();

            if (service != null) {
                service.broadcastTaskResult();
            }
        }
    }

}
