package dk.heimdaldata.aactodolist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dk.heimdaldata.aactodolist.TaskEntry;

public class DatabaseHelper extends SQLiteOpenHelper {
    // using Singleton pattern
    private static DatabaseHelper sInstance;
    private static Context mContext;

    public static String DATABASE_NAME = "todolist.db";
    public static String TODO_TABLE = "todos_table";
    public static String USER_TABLE = "users_table";
    public static String COL_1 = "id";
    public static String COL_2 = "description";
    public static String COL_3 = "priority";
    public static String COL_4 = "updatedAt";
    public static String USER_1 = "id";
    public static String USER_2 = "name";
    public static String USER_3 = "password";


    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        mContext = context;
        // test database
//        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TODO_TABLE + " (" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_2 + " TEXT," +
                COL_3 + " INTEGER," +
                COL_4 + " TEXT)";
        db.execSQL(query);
        // create table users
        String query2 = "CREATE TABLE " + USER_TABLE + " (" +
                USER_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USER_2 + " TEXT," +
                USER_3 + " TEXT)";
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    public boolean insertData(String task_name, int priority, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, task_name);
        contentValues.put(COL_3, priority);
        contentValues.put(COL_4, date);
        long result = db.insert(TODO_TABLE, null, contentValues);
        if (result == -1) {
            return false;
        }
        return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor rs = db.rawQuery("select * from " + TODO_TABLE, null);
        return rs;
    }

    public List<TaskEntry> getAllTasks () {
        List<TaskEntry> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor rs = db.rawQuery("select * from " + TODO_TABLE, null);
        if (rs.getCount() == 0)
            return null;
        // while rs can moveToNext()
        while (rs.moveToNext()) {
            int id = rs.getInt(0);
            String description = rs.getString(1);
            int priority = rs.getInt(2);
            String strDate = rs.getString(3);
            Date date;
            try {
                date = new SimpleDateFormat("dd/MM/yyyy").parse(strDate);
                TaskEntry taskEntry = new TaskEntry(id, description, priority, date);
                list.add(taskEntry);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public boolean updateTask(String id, String description, int priority, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, description);
        contentValues.put(COL_3, priority);
        contentValues.put(COL_4, date);

        db.update(TODO_TABLE, contentValues, "ID = ?", new String[] { id });
        return true;
    }

    public Integer deleteTask(String id) {
        // if return 0 ==> no item has been deleted
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TODO_TABLE, "ID = ?", new String[] { id });
    }

    public TaskEntry getTaskById (String id) throws ParseException {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " + TODO_TABLE + " where ID = ?";
        Cursor cursor = db.rawQuery(query, new String[] { id });
        cursor.moveToFirst();
        int int_id = Integer.parseInt(id);
        String description = cursor.getString(1);
        int priority = cursor.getInt(2);
        String strDate = cursor.getString(3);
        Date date = new SimpleDateFormat("dd/MM/yyy").parse(strDate);
        TaskEntry taskEntry = new TaskEntry(int_id, description, priority, date);
        return taskEntry;
    }

    // manipulate Users table
    public boolean insertUser(String userName, String password) {
        // open the database in writing mode
        SQLiteDatabase db = this.getWritableDatabase();
        // use ContentValues to insert data
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_2, userName);
        contentValues.put(USER_3, password);
        long result = db.insert(USER_TABLE, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
}
