package dk.heimdaldata.aactodolist;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dk.heimdaldata.aactodolist.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity implements TaskAdapter.MyItemClickListener {
    public static String KEY_USER_ID = "USER_ID";
    FloatingActionButton fab;
    RecyclerView recyclerView;
    TaskAdapter adapter;
    AppDatabase mDb;
    DatabaseHelper sqlDb;
    List<TaskEntry> temp;
    int userID;
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d("DEBUG", "onActivityResult: ");
                    // data handle here
                    if (result.getResultCode() != 0) {
                        Intent intent = result.getData();
                        if (intent == null)
                            return;
                        String description, strDate;
                        int priority;

                        switch (result.getResultCode()) {
                            case 100:
                                // new Task --> add to table
                                // extract data
                                    description = intent.getStringExtra("DESC");
                                    priority = intent.getIntExtra("PRIORITY", 3);
                                    strDate = intent.getStringExtra("DATE");
                                    boolean inserted = sqlDb.insertData(description, priority, strDate, userID);
                                    if (inserted) {
                                        Toast.makeText(MainActivity.this, "data inserted", Toast.LENGTH_SHORT).show();
                                    }
                                break;
                            case 200:
                                // update Task
                                    // extract data
                                    int id = intent.getIntExtra("ID", -1);
                                    description = intent.getStringExtra("DESC");
                                    priority = intent.getIntExtra("PRIORITY", 3);
                                    strDate = intent.getStringExtra("DATE");
                                    boolean updated =sqlDb.updateTask(String.valueOf(id), description, priority, strDate);
                                    if (updated) {
                                        Toast.makeText(MainActivity.this, "data updated", Toast.LENGTH_SHORT).show();
                                    }

                                break;
                            default:
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewTasks);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });
        recyclerView = findViewById(R.id.recyclerViewTasks);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        temp = new ArrayList<>();
        temp.add(new TaskEntry("test", 1, new Date()));
        temp.add(new TaskEntry("Second",1, new Date()));
        adapter = new TaskAdapter(temp, this);
        recyclerView.setAdapter(adapter);

        // add swipe
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // delete the task
                int position = viewHolder.getAdapterPosition();
                TaskEntry task = adapter.getTaskLists().get(position);
                String id = String.valueOf(task.getId());
                if (sqlDb.deleteTask(id) != 0) {
                    Toast.makeText(MainActivity.this, "Task deleted successful", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Something wrong!!!", Toast.LENGTH_LONG).show();
                }
                retrieveTasks();
            }
        }).attachToRecyclerView(recyclerView);
        // initialize database
//        sqlDb = new DatabaseHelper(this);
        // get intent extra
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(KEY_USER_ID)) {
            // have extra data
            userID = intent.getIntExtra(KEY_USER_ID, 0);
        }
        sqlDb = DatabaseHelper.getInstance(getApplicationContext());

    }

    // override onCreateOptionMenu to get the option Menu
    // --------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    // override onOptionsItemSelected to add logic when user click menu
    // --------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.lock_menu:
                Toast.makeText(this, "Menu clicked", Toast.LENGTH_LONG).show();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveTasks();

    }


    private void retrieveTasks() {

        temp = sqlDb.getAllTasksByUser(String.valueOf(userID));
        Log.d("MAIN", "retrieveTasks: ");
        adapter.setTaskLists(temp);
    }

    private void openActivity() {
        // using startActivityForResult to start second activity
        // override onActivityResult to receive data
        Intent intent = new Intent(this, AddTaskActivity.class);
//        startActivity(intent);
        activityResultLauncher.launch(intent);
    }


    @Override
    public void onHolderClick(int index) {
        Toast.makeText(this, "Clicked at " + index , Toast.LENGTH_SHORT).show();
        // get the task at the click position
        TaskEntry task = adapter.getTaskLists().get(index);
        Intent intent = new Intent(this, AddTaskActivity.class);
        // pass TaskEntry data to second activity to display
        intent.putExtra("ID", task.getId());
        intent.putExtra("DESC", task.getDescription());
        intent.putExtra("PRIORITY", task.getPriority());
        // only send description & priority
        // date will be update at update time
        activityResultLauncher.launch(intent);
    }
}