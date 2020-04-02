package SMAP.au523923Flow.assignment2.wordlearnerapp.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import SMAP.au523923Flow.assignment2.wordlearnerapp.ListActivity;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Definition;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Word;

// Inspired by and some comments copied from: (see comment from TommySM)
// https://stackoverflow.com/questions/28172496/android-volley-how-to-isolate-requests-in-another-class
public class WordAPIHelper {
    private static final String TAG = "WordAPIHelper";

    private static WordAPIHelper instance;
    public RequestQueue requestQueue;

    private WordAPIHelper(Context context){
        requestQueue = Volley.newRequestQueue(context);
    }

    public static synchronized WordAPIHelper getInstance(Context context) {
        if (instance == null) {
            instance = new WordAPIHelper(context);
        }
        return instance;
    }

    //this is so you don't need to pass context each time
    public static synchronized WordAPIHelper getInstance()
    {
        if (null == instance)
        {
            throw new IllegalStateException(WordAPIHelper.class.getSimpleName() +
                    " is not initialized, call getInstance(Context) first");
        }
        return instance;
    }

    public void getWordFromOWLBOT(String word, final OWLBOTResponseListener<Word> listener){
        String url = Globals.OWLBOT_API_CALL + word;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Word wordObj = WordJsonParser.parseWordJsonWithGson(response.toString());
                        Log.d(TAG, "onResponse: API call succeded and got word: " + wordObj.getWord());
                        listener.getResult(wordObj);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null){
                            error.printStackTrace();
                            listener.getResult(null);
                        }
                    }
                })
                // Set header for authorization
                {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put(Globals.OWLBOT_HEADER_AUTH_KEY, Globals.OWLBOT_HEADER_AUTH_VAL);
                        return params;
                    }
                };

        requestQueue.add(jsonObjectRequest);
    }
}
