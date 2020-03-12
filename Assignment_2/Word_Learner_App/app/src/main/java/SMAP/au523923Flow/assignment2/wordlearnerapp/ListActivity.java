package SMAP.au523923Flow.assignment2.wordlearnerapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Definition;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Word;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.DataHelper;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.WordListItem;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.WordJsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private WordListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button exitBtn;
    private Button testBtn;
    // Should this be a resource?
    private final int EDIT_REQ = 1;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ArrayList<WordListItem> wordListItems = initWordListData(savedInstanceState);

        setUpRecyclerView(wordListItems);

        exitBtn = findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(exitBtnListener);
        testBtn = findViewById(R.id.testBtn);
        testBtn.setOnClickListener(testBtnListener);
    }

    private void setUpRecyclerView(ArrayList<WordListItem> wordListItems){
        // The implementation for adding a layout manager and adapter to recycler view,
        // is influenced by this site
        // https://www.tutorialspoint.com/how-to-use-constraint-layout-with-recyclerview
        recyclerView = findViewById(R.id.wordItemList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WordListAdapter(this, wordListItems);
        adapter.setOnItemListClickListener(onItemListClickListener);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<WordListItem> initWordListData(Bundle savedInstanceState){
        if (savedInstanceState != null)
            return savedInstanceState
                    .getParcelableArrayList(getString(R.string.WORD_LIST_ARRAY));
        else{
            DataHelper data = new DataHelper(this);
            return data.addDataFromFile();
        }
    }

    // Parcelable simplifies saving all list items
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.WORD_LIST_ARRAY),adapter.getWordListItems());
    }

    // Making sure the on click implementation is in activity, and not adapter
    private WordListAdapter.OnItemListClickListener onItemListClickListener = new WordListAdapter.OnItemListClickListener() {
        @Override
        public void onItemListClick(int position) {
            Intent intent = new Intent(ListActivity.this, DetailsActivity.class);

            WordListItem wordItem = adapter.getWordListItem(position);
            // Updating the position on the model itself, makes it separate concern for other classes
            // because the position is needed onActivityResult, to update the single item
            wordItem.setWordPosition(position);

            intent.putExtra(getString(R.string.WORD_LIST_ITEM),wordItem);

            startActivityForResult(intent, EDIT_REQ);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_REQ && resultCode == RESULT_OK){
            if (data != null){
                WordListItem wordListItem = data.getParcelableExtra(getString(R.string.WORD_LIST_ITEM));
                adapter.updateWordListItem(wordListItem.getWordPosition(), wordListItem);
                // Makes sure the list/adapter updates
                adapter.notifyDataSetChanged();
            }
        }
    }

    private View.OnClickListener exitBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finishAffinity(); // Finish activities
            System.exit(0); // Terminates the process/app
            //Runtime.getRuntime().exit(0); // the same as System, but better description of method
        }
    };
    private View.OnClickListener testBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendRequest();
        }
    };

    // Works just fine!
    private void sendRequest(){
        //send request using Volley
        if(queue==null){
            queue = Volley.newRequestQueue(this);
        }
        // Test word
        String url = "https://owlbot.info/api/v4/dictionary/vial";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Word test = WordJsonParser.parseWordJsonWithGson(response);
                    Definition def = test.getDefinitions().get(0);
                    Toast.makeText(ListActivity.this,def.getDefinition(), Toast.LENGTH_LONG).show();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ListActivity.this,"fail", Toast.LENGTH_LONG).show();
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

        queue.add(stringRequest);
    }
}

// Changed the file animal_list.csv according to the info on this post
// https://stackoverflow.com/questions/8432584/how-to-make-notepad-to-save-text-in-utf-8-without-bom
// and it worked, since it didn't read the lion correctly.
// Also kudo picture and word were not the same string so changed it to kudo, to match the drawable
