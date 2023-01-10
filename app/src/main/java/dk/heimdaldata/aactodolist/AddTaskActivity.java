package dk.heimdaldata.aactodolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import dk.heimdaldata.aactodolist.database.DatabaseHelper;

public class AddTaskActivity extends AppCompatActivity {
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;

    // Extra for the task ID to be received in the intent
    public static final String EXTRA_TASK_ID = "extraTaskId";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_TASK_ID = "instanceTaskId";

    int DEFAULT_TASK_ID = -1;
    EditText et_task;
    RadioGroup rd_group;
    Button btn_add;

    int taskId = DEFAULT_TASK_ID;   // if =-1 -> create new task, otherwise received id from Intent to populate and update existing task
    private AppDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        initialViews();
        mDb = AppDatabase.getInstance(getApplicationContext());


        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            taskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }
        Log.d("ONCREATE #2", "onCreate: default task id " + taskId);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ID")) {
            // if edit task
            // there is a message from MainActivity
            // receive taskId from intent
            String description = intent.getStringExtra("DESC");
            int priority = intent.getIntExtra("PRIORITY", PRIORITY_LOW);
            // populate UI
            et_task.setText(description);
            setPriorityInViews(priority);
            btn_add.setText("Update");
            taskId = intent.getIntExtra("ID", DEFAULT_TASK_ID);
            // load taskById using DatabaseHelper

        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(INSTANCE_TASK_ID, taskId);
        super.onSaveInstanceState(outState);
    }

    private void populateUI(TaskEntry taskEntry) {
        if (taskEntry == null) {
            return;
        }
        // set text
        et_task.setText(taskEntry.getDescription());
        setPriorityInViews(taskEntry.getPriority());
    }

    private void initialViews() {
        et_task = (EditText) findViewById(R.id.editTextTaskDescription);
        rd_group = (RadioGroup) findViewById(R.id.radioGroup);
        btn_add = (Button) findViewById(R.id.saveButton);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChange();
            }
        });
    }

    private void saveChange() {
        String description = et_task.getText().toString();
        int priority = getPriorityFromViews();
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyy");
        String strDate = dateFormat.format(date);
        // final ???
        TaskEntry taskEntry = new TaskEntry(description, priority, date);
        //send data back to Main Activity
        // dont need UserID because it will be handle in Main Activity
        Intent intent = new Intent();
        intent.putExtra("DESC", description);
        intent.putExtra("PRIORITY", priority);
        intent.putExtra("DATE", strDate);
        if (taskId == DEFAULT_TASK_ID) {
            setResult(100, intent);
        } else {
            intent.putExtra("ID", taskId);
            setResult(200, intent);
        }

        finish();

    }
    private int getPriorityFromViews() {
        int priority = 1;
        int checkedId = rd_group.getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.taskHigh:
                priority = PRIORITY_HIGH;
                break;
            case R.id.taskMid:
                priority = PRIORITY_MEDIUM;
                break;
            case R.id.taskLow:
                priority = PRIORITY_LOW;
                break;
        }
        return priority;
    }
    /**
     * setPriority is called when we receive a task from MainActivity
     *
     * @param priority the priority value
     */
    public void setPriorityInViews(int priority) {
        switch (priority) {
            case PRIORITY_HIGH:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.taskHigh);
                break;
            case PRIORITY_MEDIUM:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.taskMid);
                break;
            case PRIORITY_LOW:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.taskLow);
        }
    }
}