package edu.hanu.todolist_mvp.mock;

import static com.google.firebase.crashlytics.buildtools.reloc.com.google.common.base.Preconditions.checkNotNull;

import android.content.Context;

import androidx.annotation.NonNull;

import edu.hanu.todolist_mvp.data.source.TasksRepository;
import edu.hanu.todolist_mvp.data.source.local.TasksLocalDataSource;
import edu.hanu.todolist_mvp.mock.db.FakeTasksRemoteDataSource;

public class Injection {
    public static TasksRepository provideTasksRepository(@NonNull Context context) {
        checkNotNull(context);
        return TasksRepository.getInstance(FakeTasksRemoteDataSource.getInstance(),
                TasksLocalDataSource.getInstance(context));
    }
}
