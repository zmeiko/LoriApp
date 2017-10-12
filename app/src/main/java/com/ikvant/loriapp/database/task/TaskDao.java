package com.ikvant.loriapp.database.task;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ikvant.loriapp.database.timeentry.TimeEntry;

import java.util.List;

/**
 * Created by ikvant.
 */


@Dao
public interface TaskDao {
    @Query("SELECT * FROM Task")
    List<Task> loadAll();

    @Query("DELETE FROM Task")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(Task entry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveAll(Task... timeEntries);
}