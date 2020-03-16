package SMAP.au523923Flow.assignment2.wordlearnerapp.utils;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import SMAP.au523923Flow.assignment2.wordlearnerapp.ListActivity;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Definition;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Word;

public class WordAPIHelper {
    private static WordAPIHelper instance;
    private static RequestQueue requestQueue;
    private static Context context;

    private WordAPIHelper(Context context){
        this.context = context;
        requestQueue = getRequestQueue();

    }

    public static RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized WordAPIHelper getInstance(Context context) {
        if (instance == null) {
            instance = new WordAPIHelper(context);
        }
        return instance;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
