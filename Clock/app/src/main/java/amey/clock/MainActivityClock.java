package amey.clock;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import amey.clock.alarm.ui.AlarmActivity;
import amey.clock.cities.CitiesFragment;
import amey.clock.stopwatch.StopwatchFragment;

public class MainActivityClock extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @SuppressLint("StaticFieldLeak")
    private static Toolbar toolbar_MainActivityClock;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    public static Toolbar getToolbar_MainActivityClock() {
        if (toolbar_MainActivityClock != null)
            return toolbar_MainActivityClock;
        else
            return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_clock);

        toolbar_MainActivityClock = findViewById(R.id.toolbar_MainActivityClock);
        setSupportActionBar(toolbar_MainActivityClock);

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar_MainActivityClock, R.string.navigationOpen, R.string.navigationClose);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if (savedInstanceState == null) {
            toolbar_MainActivityClock.setTitle(R.string.clock_str);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TimeFragment()).commit();
            navigationView.setCheckedItem(R.id.time);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.time:
                toolbar_MainActivityClock.setTitle(R.string.clock_str);
                navigationView.setCheckedItem(R.id.time);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TimeFragment()).commit();
                break;

            case R.id.alarm:
                startActivity(new Intent(MainActivityClock.this, AlarmActivity.class));
                break;

            case R.id.stopwatch:
                toolbar_MainActivityClock.setTitle(R.string.stopwatch_str);
                navigationView.setCheckedItem(R.id.stopwatch);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StopwatchFragment()).commit();
                break;

            case R.id.weather:
                toolbar_MainActivityClock.setTitle(R.string.weather_str);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WeatherFragment()).commit();
                navigationView.setCheckedItem(R.id.weather);
                break;

            case R.id.cities:
                toolbar_MainActivityClock.setTitle(R.string.cities_str);
                navigationView.setCheckedItem(R.id.cities);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CitiesFragment()).commit();
                break;

            case R.id.info:
                toolbar_MainActivityClock.setTitle(R.string.info_str);
                navigationView.setCheckedItem(R.id.info);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InfoFragment()).commit();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public Toolbar getToolbar() {
        return toolbar_MainActivityClock;
    }
}
