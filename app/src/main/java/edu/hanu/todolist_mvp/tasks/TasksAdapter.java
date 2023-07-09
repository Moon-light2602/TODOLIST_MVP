package edu.hanu.todolist_mvp.tasks;

import static androidx.core.util.Preconditions.checkNotNull;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.hanu.todolist_mvp.R;
import edu.hanu.todolist_mvp.data.Task;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskHolder> {
    private List<Task> listTask;
    private TasksFragment.TaskItemListener itemListener;

    @SuppressLint("RestrictedApi")
    public TasksAdapter(List<Task> tasks, TasksFragment.TaskItemListener itemListener) {
        this.listTask = checkNotNull(tasks);
        this.itemListener = itemListener;

    }

    @SuppressLint("RestrictedApi")
    public void setData(List<Task> tasks) {
        this.listTask = checkNotNull(tasks);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task item = listTask.get(position);
        holder.setData(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listTask.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder {
        private LinearLayout layout_item;
        private CheckBox completeCheckBox;
        private TextView tvTitle;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            layout_item = itemView.findViewById(R.id.layout_item);
            tvTitle = itemView.findViewById(R.id.title);
            completeCheckBox = itemView.findViewById(R.id.complete);
        }

        private void setData(Task task) {
            tvTitle.setText(task.getTitle());
            completeCheckBox.setChecked(task.isCompleted());

            layout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.onTaskClick(task);
                }
            });

            completeCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!task.isCompleted()) {
                        itemListener.onCompleteTaskClick(task);
                    } else {
                        itemListener.onActivateTaskClick(task);
                    }
                }
            });
        }
    }
}