package dk.heimdaldata.aactodolist;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
// DATA ACCESS OBJECT
@Dao
public interface TaskDao {
    @Query("SELECT * FROM task ORDER BY priority")
    List<TaskEntry> loadAllTask();

    @Insert
    void insertTask(TaskEntry taskEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(TaskEntry taskEntry);

    @Delete
    void deleteTask(TaskEntry taskEntry);

    @Query("SELECT * FROM task WHERE id = :id")
    TaskEntry loadTaskById(int id);
}
