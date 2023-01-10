package dk.heimdaldata.aactodolist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private static final String DATE_FORMAT = "dd/MM/yyy";
    final private MyItemClickListener listener;
    private List<TaskEntry> taskLists;
    private Context context;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

//    public TaskAdapter(Context context) {
//        this.context = context;
//    }


    public TaskAdapter(List<TaskEntry> taskLists, MyItemClickListener listener) {
        this.taskLists = taskLists;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TaskEntry taskEntry = taskLists.get(position);
        String updatedAt = dateFormat.format(taskEntry.getUpdatedAt());
        holder.tv_taskDescription.setText(taskEntry.getDescription());
        holder.tv_updatedAt.setText(updatedAt);
        // Programmatically set the text and color for the priority TextView

        holder.tv_priority.setText(Integer.toString(taskEntry.getPriority()));

    }

    @Override
    public int getItemCount() {
        if (taskLists == null) {
            return 0;
        } else {
            return taskLists.size();
        }

    }

    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */
    public void setTaskLists(List<TaskEntry> newList) {
        taskLists = newList;
        notifyDataSetChanged();
    }

    public List<TaskEntry> getTaskLists() {
        return taskLists;
    }
    // -----------------------------------------------------------------------------
    // INNER CLASS AND INTERFACE
    // -----------------------------------------------------------------------------
    public interface MyItemClickListener {
        void onHolderClick(int index);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_taskDescription;
        TextView tv_updatedAt;
        TextView tv_priority;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_taskDescription = (TextView) itemView.findViewById(R.id.taskDescription);
            tv_updatedAt = (TextView) itemView.findViewById(R.id.taskUpdatedAt);
            tv_priority = (TextView) itemView.findViewById(R.id.priorityTextView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            listener.onHolderClick(clickedPosition);        // this will call the (listener = Mainactivity).onHolderClick
        }
    }

    /*
    Helper method for selecting the correct priority circle color.
    P1 = red, P2 = orange, P3 = yellow
    */
    private String getPriorityColor(int priority) {
        String colorHigh = "FF3700B3";
        String colorMid = "FF3700B3";
        String colorLow = "FF3700B3";
        String priorityColor = "";

        switch (priority) {
            case 1:
                priorityColor = colorHigh;
                break;
            case 2:
                priorityColor = colorMid;
                break;
            case 3:
                priorityColor = colorLow;
                break;
            default:
                break;
        }
        return priorityColor;
    }
}
