package com.flow.lab42.allyourdatabasesbelongtous;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "task")
public class Task {


    @PrimaryKey(autoGenerate = true)
    public int taskId;

    @ColumnInfo(name = "task_name")
    private String task;

    @ColumnInfo(name = "place_name")
    private String place;

    public Task(String task, String place) {
        this.task = task;
        this.place = place;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }


}
