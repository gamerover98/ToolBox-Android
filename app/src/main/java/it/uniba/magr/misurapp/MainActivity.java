package it.uniba.magr.misurapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.internal.NavigationMenuItemView;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.introduction.IntroductionActivity;
import lombok.Getter;

@SuppressWarnings({"squid:S110", "NotNullFieldNotInitialized"})
public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    @Getter @NotNull
    private Toolbar toolbar;

    @Getter @NotNull
    private DrawerLayout drawerLayout;

    @Getter @NotNull
    private NavController navController;

    @Getter @NotNull
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupNavigation();

    }

    @Override
    protected void onStart() {

        super.onStart();

        if (!IntroductionActivity.isAlreadyCompleted(this)) {

            Intent intent = new Intent(this, IntroductionActivity.class);
            startActivity(intent);

        }

    }

    /**
     * Setup the ActionBar with the hamburger button and
     * the navigation menu from left side.
     */
    private void setupNavigation() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ActionBar is initialized by toolbar.
        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null; // ActionBar cannot be null
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        drawerLayout   = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);

    }

    /*
     * Hamburger button click event.
     */
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, drawerLayout);
    }

    /*
     * Back arrow button click event.
     * The suppression will be when it will be filled with its implementation.
     */
    @Override
    @SuppressWarnings("squid:S1185")
    public void onBackPressed() {
        super.onBackPressed(); // unused for now.
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        item.setChecked(true);
        drawerLayout.closeDrawers();
        int id = item.getItemId();

        NavigationMenuItemView mainMenuItem = findViewById(R.id.drawer_menu_main);
        NavigationMenuItemView secondMenuItem = findViewById(R.id.drawer_menu_second);

        if (id == mainMenuItem.getId()) {
            navController.navigate(R.id.nav_main_fragment);
        } else if (id == secondMenuItem.getId()) {
            navController.navigate(R.id.nav_second_fragment);
        }

        return false;

    }

}