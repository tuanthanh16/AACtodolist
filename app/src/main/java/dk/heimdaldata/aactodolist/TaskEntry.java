package dk.heimdaldata.aactodolist;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

// annotation @Entry tell room to turn our class into an entity with table name = "task" in the database
@Entity(tableName = "task")
public class TaskEntry {

    // annotation @PrimaryKey tell room that id will be the primary key of table
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String description;
    private int priority;
    private Date updatedAt;

    @Ignore
    public TaskEntry(String description, int priority, Date updatedAt) {
        this.description = description;
        this.priority = priority;
        this.updatedAt = updatedAt;
    }

    public TaskEntry(int id, String description, int priority, Date updatedAt) {
        this.id = id;
        this.description = description;
        this.priority = priority;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
