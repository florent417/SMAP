package com.flow.lab42.allyourdatabasesbelongtous;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDAO {
    @Query("SELECT * FROM task")
    List<Task> getAllTask();

    @Query("SELECT * FROM task WHERE task_name LIKE :taskName LIMIT 1")
    Task findByTaskName(String taskName);

    @Query("SELECT * FROM task WHERE place_name LIKE :placeName LIMIT 1")
    Task findByPlaceName(String placeName);

    @Insert
    void insertAllTasks(Task... tasks);

    @Delete
    void deleteTask(Task task);
}
