package edu.hanu.todolist_mvp.taskdetail;

import static androidx.core.util.Preconditions.checkNotNull;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import edu.hanu.todolist_mvp.R;
import edu.hanu.todolist_mvp.addedittask.AddEditTaskActivity;
import edu.hanu.todolist_mvp.addedittask.AddEditTaskFragment;

public class TaskDetailFragment extends Fragment implements TaskDetailContract.View {
    public static final String DETAIL_TASK_ID = "TASK_ID";

    public static final int REQUEST_EDIT_TASK = 1;

    private TaskDetailContract.Presenter presenter;
    private TextView detailTitle, detailDesc;
    private CheckBox completeCheckBox;

    public static TaskDetailFragment newInstance(String taskId) {
        Bundle arguments = new Bundle();
        arguments.putString(DETAIL_TASK_ID, taskId);
        TaskDetailFragment fragment = new TaskDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.taskdetail_frag, container, false);
        setHasOptionsMenu(true);
        detailTitle = root.findViewById(R.id.task_detail_title);
        detailDesc = root.findViewById(R.id.task_detail_description);
        completeCheckBox = root.findViewById(R.id.task_detail_complete);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.editTask();
            }
        });

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                presenter.deleteTask();
                return true;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.taskdetail_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setPresenter(TaskDetailContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if(active) {
            detailTitle.setText("");
            detailDesc.setText(getString(R.string.loading));
        }
    }

    @Override
    public void showMissingTask() {
        detailTitle.setText("");
        detailDesc.setText(getString(R.string.no_data));
    }

    @Override
    public void hideTitle() {
        detailTitle.setVisibility(View.GONE);
    }

    @Override
    public void showTitle(String title) {
        detailTitle.setVisibility(View.VISIBLE);
        detailTitle.setText(title);
    }

    @Override
    public void hideDescription() {
        detailDesc.setVisibility(View.GONE);
    }

    @Override
    public void showDescription(String description) {
        detailDesc.setVisibility(View.VISIBLE);
        detailDesc.setText(description);
    }

    @Override
    public void showCompletionStatus(boolean complete) {
        completeCheckBox.setChecked(complete);
        completeCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                        if(isCheck) {
                            presenter.completeTask();
                        } else {
                            presenter.activateTask();
                        }
                    }
                }
        );
    }

    @Override
    public void showEditTask(String taskId) {
        Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
        intent.putExtra(AddEditTaskFragment.EDIT_TASK_ID, taskId);
        startActivityForResult(intent, REQUEST_EDIT_TASK);
    }

    @Override
    public void showTaskDeleted() {
        getActivity().finish();
    }

    @Override
    public void showTaskMarkedComplete() {
        Snackbar.make(getView(), getString(R.string.task_marked_complete), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTaskMarkedActive() {
        Snackbar.make(getView(), getString(R.string.task_marked_active), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded(); // return true if fragment is currently added to its activity
    }
}