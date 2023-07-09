package edu.hanu.todolist_mvp.tasks;

import java.util.List;

import edu.hanu.todolist_mvp.base.BasePresenter;
import edu.hanu.todolist_mvp.base.BaseView;
import edu.hanu.todolist_mvp.data.Task;

/**
 * This specifies the contract between the view and presenter
 */
public interface TasksContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showTasks(List<Task> tasks);

        void showAddTask();

        void showTaskDetailsUi(String taskId);

        void showTaskMarkedComplete();

        void showTaskMarkedActive();

        void showCompletedTasksCleared();

        void showLoadingTasksError();

        void showNoTasks();

        void showActiveFilterLabel();

        void showCompletedFilterLabel();

        void showAllFilterLabel();

        void showNoActiveTasks();

        void showNoCompletedTasks();

        void showSuccessfullySavedMessage();

        boolean isActive();

        void showFilteringPopUpMenu();
    }
    interface Presenter extends BasePresenter {
        void result (int requestCode, int resultCode);

        void loadTasks(boolean forceUpdate);

        void addNewTask();

        void openTaskDetails(Task requestedTask);

        void completeTask(Task completedTask);

        void activateTask(Task activeTask);

        void clearCompletedTasks();

        void setFiltering(TasksFilterType requestType);

        TasksFilterType getFiltering();
    }
}