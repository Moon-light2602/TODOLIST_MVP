package edu.hanu.todolist_mvp.taskdetail;
import static androidx.core.util.Preconditions.checkNotNull;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import edu.hanu.todolist_mvp.data.Task;
import edu.hanu.todolist_mvp.data.source.TasksDataSource;
import edu.hanu.todolist_mvp.data.source.TasksRepository;

/**
 * Listens to user actions from the UI, retrieves the data
 * and updates the UI as required
 */
public class TaskDetailPresenter implements TaskDetailContract.Presenter {
    private final TasksRepository tasksRepository;
    private final TaskDetailContract.View taskDetailView;
    @Nullable
    private String taskId;

    @SuppressLint("RestrictedApi")
    public TaskDetailPresenter(@Nullable String taskId,
                               @NonNull TasksRepository tasksRepository,
                               @NonNull TaskDetailContract.View taskDetailView) {
        this.taskId = taskId;
        this.tasksRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null");
        this.taskDetailView = checkNotNull(taskDetailView, "tasksDetailView cannot be null");
        taskDetailView.setPresenter(this);
    }

    @Override
    public void start() {
        openTask();
    }

    private void openTask() {
        if (taskId == null || taskId.isEmpty()) {
            taskDetailView.showMissingTask();
            return;
        }

        taskDetailView.setLoadingIndicator(true);
        tasksRepository.getTask(taskId, new TasksDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                if (!taskDetailView.isActive()) {
                    return;
                }
                taskDetailView.setLoadingIndicator(false);
                if (null == task) {
                    taskDetailView.showMissingTask();
                } else {
                    showTask(task);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (!taskDetailView.isActive()) {
                    return;
                }
                taskDetailView.showMissingTask();
            }
        });
    }

    @Override
    public void editTask() {
        if (taskId == null || taskId.isEmpty()) {
            taskDetailView.showMissingTask();
            return;
        }
        taskDetailView.showEditTask(taskId);
    }

    @Override
    public void deleteTask() {
        tasksRepository.deleteTask(taskId);
        taskDetailView.showTaskDeleted();
    }

    @Override
    public void completeTask() {
        if (taskId == null || taskId.isEmpty()) {
            taskDetailView.showMissingTask();
            return;
        }
        tasksRepository.completeTask(taskId);
        taskDetailView.showTaskMarkedComplete();
    }

    @Override
    public void activateTask() {
        if (taskId == null || taskId.isEmpty()) {
            taskDetailView.showMissingTask();
            return;
        }
        tasksRepository.activateTask(taskId);
        taskDetailView.showTaskMarkedComplete();
    }

    private void showTask(Task task) {
        String title = task.getTitle();
        String description = task.getDesc();

        if (title != null && title.isEmpty()) {
            taskDetailView.hideTitle();
        } else {
            taskDetailView.showTitle(title);
        }

        if (description != null && description.isEmpty()) {
            taskDetailView.hideDescription();
        } else {
            taskDetailView.showDescription(description);
        }

        taskDetailView.showCompletionStatus(task.isCompleted());
    }
}