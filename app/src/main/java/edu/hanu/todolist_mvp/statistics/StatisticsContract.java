package edu.hanu.todolist_mvp.statistics;


import edu.hanu.todolist_mvp.base.BasePresenter;
import edu.hanu.todolist_mvp.base.BaseView;

public interface StatisticsContract {

    interface View extends BaseView<Presenter> {

        void setProgressIndicator(boolean active);

        void showStatistics(int numberOfIncompleteTasks, int numberOfCompletedTasks);

        void showLoadingStatisticsError();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

    }
}