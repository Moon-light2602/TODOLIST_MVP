package edu.hanu.todolist_mvp.tasks;

import static androidx.core.util.Preconditions.checkNotNull;

import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import edu.hanu.todolist_mvp.addedittask.AddEditTaskActivity;
import edu.hanu.todolist_mvp.data.Task;
import edu.hanu.todolist_mvp.data.source.TasksDataSource;
import edu.hanu.todolist_mvp.data.source.TasksRepository;

public class TasksPresenter implements TasksContract.Presenter {
    private final TasksRepository tasksRepository;
    private final TasksContract.View tasksView;
    private TasksFilterType currentFiltering = TasksFilterType.ALL_TASKS;
    private boolean firstLoad = true;

    @SuppressLint("RestrictedApi")
    public TasksPresenter (@NonNull TasksRepository tasksRepository,
                           @NonNull TasksContract.View tasksView)  {
        this.tasksRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null");
        this.tasksView = checkNotNull(tasksView, "tasksView cannot be null");
        this.tasksView.setPresenter(this);
    }
    @Override
    public void start() {
        loadTasks(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        if(AddEditTaskActivity.REQUEST_ADD_TASK == requestCode && Activity.RESULT_OK == resultCode) {
            tasksView.showSuccessfullySavedMessage();
        }
    }

    @Override
    public void loadTasks(boolean forceUpdate) {
        loadTasks(forceUpdate || firstLoad, true);
        firstLoad = false;
    }

    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if(showLoadingUI) {
            tasksView.setLoadingIndicator(true);
        }
        if(forceUpdate) {
            tasksRepository.refreshTasks();
        }

        tasksRepository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                List<Task> tasksToShow = new ArrayList<Task>();

                for(Task task : tasks) {
                    switch (currentFiltering) {
                        case ALL_TASKS:
                            tasksToShow.add(task);
                            break;
                        case ACTIVE_TASKS:
                            if(task.isActive()) {
                                tasksToShow.add(task);
                            }
                            break;
                        case COMPLETED_TASKS:
                            if(task.isCompleted()) {
                                tasksToShow.add(task);
                            }
                            break;
                        default:
                            tasksToShow.add(task);
                            break;
                    }
                }

                if(!tasksView.isActive()) {
                    return;
                }
                if(showLoadingUI) {
                    tasksView.setLoadingIndicator(false);
                }

                processTasks(tasksToShow);
            }

            @Override
            public void onDataNotAvailable() {
                if (!tasksView.isActive()) {
                    return;
                }
                tasksView.showLoadingTasksError();
            }
        });
    }

    private void processTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            processEmptyTasks();
        } else {
            // Show the list of tasks
            tasksView.showTasks(tasks);
            // Set the filter label's text.
            showFilterLabel();
        }
    }

    private void processEmptyTasks() {
        switch (currentFiltering) {
            case ACTIVE_TASKS:
                tasksView.showNoActiveTasks();
                break;
            case COMPLETED_TASKS:
                tasksView.showNoCompletedTasks();
                break;
            default:
                tasksView.showNoTasks();
                break;
        }
    }

    private void showFilterLabel() {
        switch (currentFiltering) {
            case ACTIVE_TASKS:
                tasksView.showActiveFilterLabel();
                break;
            case COMPLETED_TASKS:
                tasksView.showCompletedFilterLabel();
                break;
            default:
                tasksView.showAllFilterLabel();
                break;
        }
    }

    @Override
    public void addNewTask() {
        tasksView.showAddTask();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void openTaskDetails(Task requestedTask) {
        checkNotNull(requestedTask, "requestedTask cannot be null!");
        tasksView.showTaskDetailsUi(requestedTask.getId());
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void completeTask(Task completedTask) {
        checkNotNull(completedTask, "completedTask cannot be null!");
        tasksRepository.completeTask(completedTask);
        tasksView.showTaskMarkedComplete();
        loadTasks(false, false);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void activateTask(Task activeTask) {
        checkNotNull(activeTask, "activeTask cannot be null!");
        tasksRepository.activateTask(activeTask);
        tasksView.showTaskMarkedActive();
        loadTasks(false, false);
    }

    @Override
    public void clearCompletedTasks() {
        tasksRepository.clearCompletedTasks();
        tasksView.showCompletedTasksCleared();
        loadTasks(false, false);
    }

    @Override
    public void setFiltering(TasksFilterType requestType) {
        currentFiltering = requestType;
    }

    @Override
    public TasksFilterType getFiltering() {
        return currentFiltering;
    }
}