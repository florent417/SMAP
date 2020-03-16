package SMAP.au523923Flow.assignment2.wordlearnerapp.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Definition;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Word;

public class RequestBuilder {
    public static StringRequest setupStringReq(String word){
        // Test word
        String url = "https://owlbot.info/api/v4/dictionary/" + word;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Word test = WordJsonParser.parseWordJsonWithGson(response);
                        Definition def = test.getDefinitions().get(0);
                        //Toast.makeText(ListActivity.this,def.getDefinition(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(ListActivity.this,"fail", Toast.LENGTH_LONG).show();
                        // What goes here?
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //return super.getHeaders();
                Map<String, String> params = new HashMap<>();
                // Make globals class
                params.put("Authorization", "Token 0dfed56fdd7e8c1355e33b848f1e068dff085598");
                return params;
            }
        };
        return stringRequest;
    }
}
