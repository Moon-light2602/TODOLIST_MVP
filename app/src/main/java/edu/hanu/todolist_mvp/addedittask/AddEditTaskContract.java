package edu.hanu.todolist_mvp.addedittask;

import edu.hanu.todolist_mvp.base.BasePresenter;
import edu.hanu.todolist_mvp.base.BaseView;

public interface AddEditTaskContract {

    interface View extends BaseView<Presenter> {
        void showEmptyTaskError();

        void showTasksList();

        void setTitle(String title);

        void setDescription(String description);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void createTask(String title, String description);

        void updateTask(String title, String description);

        void populateTask();
    }
}