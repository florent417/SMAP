package com.flow.lab42.allyourdatabasesbelongtous;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {

    private static final String DATABASE_NAME = "task-database";
    EditText task, place;
    Button addTaskBtn;
    private TaskDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Stetho.initializeWithDefaults(this);


        db = Room.databaseBuilder(this,
                TaskDatabase.class, DATABASE_NAME).allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        task = findViewById(R.id.task);
        place = findViewById(R.id.place);
        addTaskBtn = findViewById(R.id.addTaskBtn);

        addTaskBtn.setOnClickListener(addTaskBtnListener);
    }

    private View.OnClickListener addTaskBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Task newTask = new Task(task.getText().toString(), place.getText().toString());
            // add to DB

            //Task newItems[] = new Task[]{newTask};


            db.taskDAO().insertAllTasks(newTask);
        }
    };


}
