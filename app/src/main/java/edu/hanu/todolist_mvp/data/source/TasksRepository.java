package edu.hanu.todolist_mvp.data.source;
import static androidx.core.util.Preconditions.checkNotNull;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.hanu.todolist_mvp.data.Task;

public class TasksRepository implements TasksDataSource {
    private static TasksRepository INSTANCE = null;
    private TasksDataSource tasksLocalDataSource;
    Map<String, Task> cachedTasks;
    boolean cacheIsDirty = false;

    @SuppressLint("RestrictedApi")
    private TasksRepository(@NonNull TasksDataSource tasksLocalDataSource) {
        this.tasksLocalDataSource = checkNotNull(tasksLocalDataSource);
    }

    public static TasksRepository getInstance(TasksDataSource tasksLocalDataSource) {
        if(INSTANCE == null) {
            INSTANCE = new TasksRepository(tasksLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
    @SuppressLint("RestrictedApi")
    @Override
    public void getTasks(@NonNull LoadTasksCallback callback) {
        checkNotNull(callback);
        if(cachedTasks != null && !cacheIsDirty) {
            callback.onTasksLoaded(new ArrayList<>(cachedTasks.values()));
            return;
        }

        tasksLocalDataSource.getTasks(new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                refreshCache(tasks);
                callback.onTasksLoaded(new ArrayList<>(cachedTasks.values()));
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {
        checkNotNull(taskId);
        checkNotNull(callback);

        Task cachedTask = getTaskWithId(taskId);

        // Respond immediately with cache if available
        if (cachedTask != null) {
            callback.onTaskLoaded(cachedTask);
            return;
        }

        // Load from server/persisted if needed.

        // Is the task in the local data source? If not, query the network.
        tasksLocalDataSource.getTask(taskId, new GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                callback.onTaskLoaded(task);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void saveTask(@NonNull Task task) {
        checkNotNull(task);
        tasksLocalDataSource.saveTask(task);

        if(cachedTasks == null) {
            cachedTasks = new LinkedHashMap<>();
        }
        cachedTasks.put(task.getId(), task);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void completeTask(@NonNull Task task) {
        checkNotNull(task);
        tasksLocalDataSource.completeTask(task);

        Task completedTask = new Task(task.getId(), task.getTitle(), task.getDesc(), true);

        if(cachedTasks == null) {
            cachedTasks = new LinkedHashMap<>();
        }
        cachedTasks.put(task.getId(), completedTask);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void completeTask(@NonNull String taskId) {
        checkNotNull(taskId);
        completeTask(getTaskWithId(taskId));
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void activateTask(@NonNull Task task) {
        checkNotNull(task);
        tasksLocalDataSource.activateTask(task);

        Task activeTask = new Task(task.getId(), task.getTitle(), task.getDesc());

        // Do in memory cache update to keep the app UI up to date
        if (cachedTasks == null) {
            cachedTasks = new LinkedHashMap<>();
        }
        cachedTasks.put(task.getId(), activeTask);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void activateTask(@NonNull String taskId) {
        checkNotNull(taskId);
        completeTask(getTaskWithId(taskId));
    }

    @Override
    public void clearCompletedTasks() {
        tasksLocalDataSource.clearCompletedTasks();

        // Do in memory cache update to keep the app UI up to date
        if (cachedTasks == null) {
            cachedTasks = new LinkedHashMap<>();
        }
        Iterator<Map.Entry<String, Task>> it = cachedTasks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Task> entry = it.next();
            if (entry.getValue().isCompleted()) {
                it.remove();
            }
        }
    }

    @Override
    public void refreshTasks() {
        cacheIsDirty = true;
    }

    @Override
    public void deleteAllTasks() {
        tasksLocalDataSource.deleteAllTasks();

        if (cachedTasks == null) {
            cachedTasks = new LinkedHashMap<>();
        }
        cachedTasks.clear();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void deleteTask(@NonNull String taskId) {
        tasksLocalDataSource.deleteTask(checkNotNull(taskId));

        cachedTasks.remove(taskId);
    }

    private void refreshCache(List<Task> tasks) {
        if(cachedTasks == null) {
            cachedTasks = new LinkedHashMap<>();
        }
        cachedTasks.clear();
        for(Task task : tasks) {
            cachedTasks.put(task.getId(), task);
        }
        cacheIsDirty = false;
    }

    @SuppressLint("RestrictedApi")
    @Nullable
    private Task getTaskWithId(@NonNull String id) {
        checkNotNull(id);
        if (cachedTasks == null || cachedTasks.isEmpty()) {
            return null;
        } else {
            return cachedTasks.get(id);
        }
    }
}