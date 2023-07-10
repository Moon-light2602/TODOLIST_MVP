package edu.hanu.todolist_mvp.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import edu.hanu.todolist_mvp.data.source.TasksRepository;
import edu.hanu.todolist_mvp.data.source.local.TasksLocalDataSource;
import edu.hanu.todolist_mvp.mock.Injection;
import edu.hanu.todolist_mvp.tasks.MainActivity;
import edu.hanu.todolist_mvp.R;
import edu.hanu.todolist_mvp.util.ActivityUtils;

public class StatisticsActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private StatisticsPresenter statisticsPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Statistics");
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        StatisticsFragment statisticFragment = (StatisticsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(statisticFragment == null) {
            statisticFragment = new StatisticsFragment();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), statisticFragment, R.id.contentFrame);
        }

        new StatisticsPresenter(
                Injection.provideTasksRepository(getApplicationContext()), statisticFragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.list_navigation_menu_item:
                                Intent intent =
                                        new Intent(StatisticsActivity.this, MainActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                break;
                            case R.id.statistics_navigation_menu_item:
                                // Do nothing, we're already on that screen
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
}

