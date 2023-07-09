package edu.hanu.todolist_mvp.statistics;

import static androidx.core.util.Preconditions.checkNotNull;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.util.List;

import edu.hanu.todolist_mvp.data.Task;
import edu.hanu.todolist_mvp.data.source.TasksDataSource;
import edu.hanu.todolist_mvp.data.source.TasksRepository;


public class StatisticsPresenter implements StatisticsContract.Presenter {

    private final TasksRepository tasksRepository;

    private final StatisticsContract.View statisticsView;

    @SuppressLint("RestrictedApi")
    public StatisticsPresenter(@NonNull TasksRepository tasksRepository,
                               @NonNull StatisticsContract.View statisticsView) {
        this.tasksRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null");
        this.statisticsView = checkNotNull(statisticsView, "StatisticsView cannot be null!");

        statisticsView.setPresenter(this);
    }

    @Override
    public void start() {
        loadStatistics();
    }

    private void loadStatistics() {
        statisticsView.setProgressIndicator(true);

        tasksRepository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                int activeTasks = 0;
                int completedTasks = 0;

                // We calculate number of active and completed tasks
                for (Task task : tasks) {
                    if (task.isCompleted()) {
                        completedTasks += 1;
                    } else {
                        activeTasks += 1;
                    }
                }
                // The view may not be able to handle UI updates anymore
                if (!statisticsView.isActive()) {
                    return;
                }
                statisticsView.setProgressIndicator(false);

                statisticsView.showStatistics(activeTasks, completedTasks);
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!statisticsView.isActive()) {
                    return;
                }
                statisticsView.showLoadingStatisticsError();
            }
        });
    }
}