package smap.au523923.lab9.firebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Something extends AppCompatActivity {
    private static final String TAG = "Something";
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ListenerRegistration itemsListener;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_something);

        //final List<String> items = new ArrayList<>(Arrays.asList("Heya", "you too", "boom"));

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListAdapter(new ArrayList<String>());
        recyclerView.setAdapter(adapter);

        Map<String, Object> item = new HashMap<>();
        item.put("Some text", new Date().toString());

        firestore.collection("items").add(item).addOnSuccessListener(
                new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Added " + documentReference.getId());
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemsListener = firestore.collection("items").addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.getDocuments().isEmpty()){
                            List<String> items = new ArrayList<>();
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                                items.add(snapshot.getData().get("Some text").toString());
                            }
                            adapter.setItems(items);
                        }
                    }
                }
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        itemsListener.remove();
    }
}
