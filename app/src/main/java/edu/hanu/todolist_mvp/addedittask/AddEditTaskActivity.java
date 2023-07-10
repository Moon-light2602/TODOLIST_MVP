package edu.hanu.todolist_mvp.addedittask;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.hanu.todolist_mvp.R;
import edu.hanu.todolist_mvp.data.source.TasksRepository;
import edu.hanu.todolist_mvp.data.source.local.TasksLocalDataSource;
import edu.hanu.todolist_mvp.data.source.remote.TasksRemoteDataSource;
import edu.hanu.todolist_mvp.mock.Injection;
import edu.hanu.todolist_mvp.util.ActivityUtils;

public class AddEditTaskActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_TASK = 1;
    FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtask_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        AddEditTaskFragment addEditTaskFragment = (AddEditTaskFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        fab = (FloatingActionButton) findViewById(R.id.fab_edit_task_done);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddEditTaskActivity.this, "fab clicked", Toast.LENGTH_SHORT).show();
            }
        });

        String taskId = null;
        if (addEditTaskFragment == null) {
            addEditTaskFragment = new AddEditTaskFragment();

            if (getIntent().hasExtra(AddEditTaskFragment.EDIT_TASK_ID)) {
                taskId = getIntent().getStringExtra(AddEditTaskFragment.EDIT_TASK_ID);
                actionBar.setTitle(R.string.edit_task);
                Bundle bundle = new Bundle();
                bundle.putString(AddEditTaskFragment.EDIT_TASK_ID, taskId);
                addEditTaskFragment.setArguments(bundle);
            } else {
                actionBar.setTitle(R.string.add_task);
            }

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditTaskFragment, R.id.contentFrame);
        }

        new AddEditTaskPresenter(
                taskId,
                Injection.provideTasksRepository(getApplicationContext()),
                addEditTaskFragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}