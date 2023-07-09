package edu.hanu.todolist_mvp.addedittask;
import static androidx.core.util.Preconditions.checkNotNull;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import edu.hanu.todolist_mvp.data.Task;
import edu.hanu.todolist_mvp.data.source.TasksDataSource;

public class AddEditTaskPresenter implements AddEditTaskContract.Presenter, TasksDataSource.GetTaskCallback  {
    @NonNull
    private final TasksDataSource tasksRepository;

    @NonNull
    private final AddEditTaskContract.View addTaskView;

    @Nullable
    private String taskId;

    @SuppressLint("RestrictedApi")
    public AddEditTaskPresenter(@Nullable String taskId, @NonNull TasksDataSource tasksRepository,
                                @NonNull AddEditTaskContract.View addTaskView) {
        this.taskId = taskId;
        this.tasksRepository = checkNotNull(tasksRepository);
        this.addTaskView = checkNotNull(addTaskView);

        addTaskView.setPresenter(this);
    }

    @Override
    public void createTask(String title, String description) {
        Task newTask = new Task(title, description);
        if (newTask.isEmpty()) {
            addTaskView.showEmptyTaskError();
        } else {
            tasksRepository.saveTask(newTask);
            addTaskView.showTasksList();
        }
    }

    @Override
    public void updateTask(String title, String description) {
        if (taskId == null) {
            throw new RuntimeException("updateTask() was called but task is new.");
        }
        tasksRepository.saveTask(new Task(taskId, title, description));
        addTaskView.showTasksList(); // After an edit, go back to the list.
    }

    @Override
    public void populateTask() {
        if (taskId == null) {
            throw new RuntimeException("populateTask() was called but task is new.");
        }
        tasksRepository.getTask(taskId, this);
    }

    @Override
    public void start() {
        if (taskId != null) {
            populateTask();
        }
    }

    @Override
    public void onTaskLoaded(Task task) {
        if (addTaskView.isActive()) {
            addTaskView.setTitle(task.getTitle());
            addTaskView.setDescription(task.getDesc());
        }
    }

    @Override
    public void onDataNotAvailable() {
        if (addTaskView.isActive()) {
            addTaskView.showEmptyTaskError();
        }
    }
}