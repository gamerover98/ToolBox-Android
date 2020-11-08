package it.uniba.magr.misurapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import it.uniba.magr.misurapp.auth.AuthActivity;
import it.uniba.magr.misurapp.introduction.IntroductionFragment;
import it.uniba.magr.misurapp.loading.LoadingFragment;
import lombok.Getter;

/**
 * The Main Activity layout of the application.
 *
 * This class will manage the interfaces and components
 * of the project.
 */
@SuppressWarnings({"squid:S110", "NotNullFieldNotInitialized"})
public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private static final String HOME_LOG_TAG = "Home";

    private final Map<MenuItem, Runnable> navItemBehaviourMap = new HashMap<>();

    /**
     * Gets the ToolBar View instance.
     * It will be initialized since the activity layout
     * doesn't have an XML default toolbar property.
     */
    @Getter @NotNull
    private Toolbar toolbar;

    /**
     * Gets the layout instance of this activity.
     * The DrawerLayout thought to be the base of the dynamic
     * graph layout managed by the Navigation Controller.
     */
    @Getter @NotNull
    private DrawerLayout drawerLayout;

    /**
     * Gets the NavigationView component located
     * into the DrawerLayout of this activity.
     *
     * <p>You can consider this as the navigation menu.</p>
     */
    @Getter @NotNull
    private NavigationView navigationView;

    /**
     * Gets the Navigation Host Fragment instance.
     */
    @Getter @NotNull
    private NavHostFragment navHostFragment;

    /**
     * Gets the Navigation Controller instance.
     */
    @Getter @NotNull
    private NavController navController;

    /**
     * Application layout and navigation initialization.
     * @param bundle The bundle of this activity.
     */
    @Override
    protected void onCreate(@Nullable Bundle bundle) {

        super.onCreate(bundle);
        setContentView(R.layout.activity_home);

        setupNavigation();

    }

    @Override
    protected void onStart() {

        super.onStart();

        // disable main menu item
        MenuItem menuItem = navigationView.getMenu().findItem(R.id.drawer_menu_main);

        if (menuItem != null) {
            navigationView.getMenu().removeItem(menuItem.getItemId());
        }

    }

    @Override
    protected void onResume() {

        super.onResume();
        reload();

    }

    /**
     * Hamburger and back arrow buttons click event.
     */
    @Override
    public boolean onSupportNavigateUp() {

        toolbar.setTitle(R.string.app_name);
        return NavigationUI.navigateUp(navController, drawerLayout);

    }

    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {

        drawerLayout.closeDrawers();

        Optional<MenuItem> clickedItem = navItemBehaviourMap.keySet()
                .stream().filter(current -> current.getItemId() == item.getItemId()).findAny();

        clickedItem.ifPresent(menuItem -> {

            toolbar.setTitle(menuItem.getTitle());
            Runnable behaviour = navItemBehaviourMap.get(menuItem);

            assert behaviour != null;
            behaviour.run();

        });

        return false;

    }

    public void reload() {

        setupNavBehaviourMap();
        refreshNavigation();

        if (!IntroductionFragment.isCompleted(this)) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.home_frame_layout, new IntroductionFragment());
            fragmentTransaction.commit();

        } else if (!AuthActivity.isAuthenticated() && !AuthActivity.isAnonymousUser(this)) {

            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);

        }

    }

    public void refreshNavigation() {

        MenuItem loginItem = navigationView.getMenu().findItem(R.id.drawer_menu_login);
        MenuItem logoutItem = navigationView.getMenu().findItem(R.id.drawer_menu_logout);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {

            loginItem.setVisible(false);
            logoutItem.setVisible(true);

        } else {

            loginItem.setVisible(true);
            logoutItem.setVisible(false);

        }

    }

    public void setupNavBehaviourMap() {

        MenuItem loginItem        = navigationView.getMenu().findItem(R.id.drawer_menu_login);
        MenuItem logoutItem       = navigationView.getMenu().findItem(R.id.drawer_menu_logout);
        MenuItem secondMenuItem   = navigationView.getMenu().findItem(R.id.drawer_menu_second);
        MenuItem settingsMenuItem = navigationView.getMenu().findItem(R.id.drawer_menu_settings);

        navItemBehaviourMap.clear();

        navItemBehaviourMap.put(loginItem, this :: loginNavClick);
        navItemBehaviourMap.put(logoutItem, this :: logoutNavClick);
        navItemBehaviourMap.put(secondMenuItem, this :: secondItemNavClick);
        navItemBehaviourMap.put(settingsMenuItem, this :: settingsItemNavClick);

    }

    private void secondItemNavClick() {

        navController.navigate(R.id.nav_second_fragment);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

    private void settingsItemNavClick() {

        toolbar.setTitle(R.string.text_settings);
        navController.navigate(R.id.nav_settings_fragment);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

    @SuppressWarnings("squid:S4144")
    private void loginNavClick() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            firebaseAuth.signOut();
        }

        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);

    }

    @SuppressWarnings("squid:S4144")
    private void logoutNavClick() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            firebaseAuth.signOut();
        }

        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);

    }

    private void handleAuthentication() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (!AuthActivity.isAuthenticated() && !AuthActivity.isAnonymousUser(this)) {

            // open authentication activity
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);

        } else {

            LoadingFragment loadingFragment = new LoadingFragment(this :: checkFirebaseUser);
            fragmentTransaction.replace(R.id.home_frame_layout, loadingFragment);
            fragmentTransaction.commit();

        }

    }

    private void checkFirebaseUser(LoadingFragment fragment) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;

        Task<GetTokenResult> resultTask = firebaseUser.getIdToken(true);

        resultTask.addOnCanceledListener(this, () -> {

            Log.w(HOME_LOG_TAG, "Cannot get token result due to a cancellation");
            fragment.close();

        });

        resultTask.addOnFailureListener(this, task -> {

            Log.w(HOME_LOG_TAG, "Cannot get token result due to a failure");
            fragment.close();

        });

        resultTask.addOnCompleteListener(this, task -> {

            if (!task.isSuccessful()) {

                firebaseAuth.signOut();
                handleAuthentication();

            }

            fragment.close();

        });

    }

    /**
     * Setup the ActionBar (ToolBar) with the hamburger button and
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

        drawerLayout   = findViewById(R.id.home_drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navHostFragment = (NavHostFragment) Objects.requireNonNull(getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment));

        navController = navHostFragment.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);

    }

}