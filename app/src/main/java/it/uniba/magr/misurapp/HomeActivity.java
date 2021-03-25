package it.uniba.magr.misurapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import it.uniba.magr.misurapp.auth.AuthActivity;
import it.uniba.magr.misurapp.database.DatabaseManager;
import it.uniba.magr.misurapp.introduction.IntroductionFragment;
import it.uniba.magr.misurapp.loading.LoadingFragment;
import it.uniba.magr.misurapp.navigation.Navigable;
import it.uniba.magr.misurapp.navigation.NavigationFragment;
import it.uniba.magr.misurapp.navigation.tools.list.ListToolsNavigation;
import it.uniba.magr.misurapp.util.GenericUtil;
import it.uniba.magr.misurapp.util.LocaleUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * The Main Activity layout of the application.
 *
 * This class will manage the interfaces and components
 * of the project.
 */
@SuppressWarnings("squid:S110")
public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    /**
     * The logcat activity prefix.
     */
    @SuppressWarnings("unused")
    private static final String HOME_LOG_TAG = "Home";

    /**
     * This is the same permission that you can find at Manifest.ACTIVITY_RECOGNITION.
     * The reason of its hard-coded implementation is that this class field doesn't exists
     * on android versions <= 28 (PIE).
     */
    private static final String ACTIVITY_RECOGNITION_PERMISSION =
            "android.permission.ACTIVITY_RECOGNITION";

    /**
     * The recognition request code to check a result.
     */
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    /**
     * This map contains the behaviour of each navigation menu item.
     * It will be execute during the onNavigationItemSelected event.
     */
    private final Map<MenuItem, Runnable> navItemBehaviourMap = new HashMap<>();

    /**
     * Gets the ToolBar View instance.
     * It will be initialized since the activity layout
     * doesn't have an XML default toolbar property.
     */
    @Getter
    private Toolbar toolbar;

    /**
     * Gets the layout instance of this activity.
     * The DrawerLayout thought to be the base of the dynamic
     * graph layout managed by the Navigation Controller.
     */
    @Getter
    private DrawerLayout drawerLayout;

    /**
     * Gets the NavigationView component located
     * into the DrawerLayout of this activity.
     *
     * <p>You can consider this as the navigation menu.</p>
     */
    @Getter
    private NavigationView navigationView;

    /**
     * Gets the Navigation Host Fragment instance.
     */
    @Getter
    private NavHostFragment navHostFragment;

    /**
     * Gets the Navigation Controller instance.
     */
    @Getter
    private NavController navController;

    /**
     * Set or get the navigation button behaviour click that will be executed
     * during the click of the hamburger or back arrow button of the navbar.
     */
    @Getter @Setter @Nullable
    private Runnable navigationButtonClick = null;

    /**
     * If true, the update settings fragment will be updated.
     */
    private boolean updateSettingsFragment;

    /**
     * Gets the SQLite database manager of this application.
     */
    @Getter
    private DatabaseManager databaseManager;

    /**
     * The loading fragment that will be replaced
     * when the activity is starting.
     */
    @Getter
    private LoadingFragment loadingFragment;

    /**
     * Application layout and navigation initialization.
     * @param bundle The bundle of this activity.
     */
    @Override
    protected void onCreate(@Nullable Bundle bundle) {

        super.onCreate(bundle);
        setContentView(R.layout.activity_home);

        databaseManager = Room.databaseBuilder(getApplicationContext(),
                DatabaseManager.class, "MisurApp").build();

        LocaleUtil.onActivityCreated();
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

        attachLoadingFragment();

    }

    @Override
    protected void onResume() {

        super.onResume();

        LocaleUtil.onActivityCreated();
        reload();

        requestRequiredPermissions();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        databaseManager.close();

    }

    /**
     * Hamburger and back arrow buttons click event.
     */
    @Override
    public boolean onSupportNavigateUp() {

        MaterialTextView displayNameTextView = findViewById(R.id.header_display_name);
        MaterialTextView emailTextView = findViewById(R.id.header_email);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {

            String displayName = firebaseUser.getDisplayName() == null ?
                    "" : firebaseUser.getDisplayName().trim();
            String mail = firebaseUser.getEmail() == null ?
                    "" : firebaseUser.getEmail().trim();

            if (displayName.isEmpty()) {
                displayNameTextView.setVisibility(View.GONE);
            } else {

                displayNameTextView.setVisibility(View.VISIBLE);
                displayNameTextView.setText(displayName);

            }

            if (mail.isEmpty()) {
                emailTextView.setVisibility(View.GONE);
            } else {

                emailTextView.setVisibility(View.VISIBLE);
                emailTextView.setText(mail);

            }

        } else {

            displayNameTextView.setVisibility(View.GONE);
            emailTextView.setVisibility(View.GONE);

        }

        if (navigationButtonClick != null) {
            navigationButtonClick.run();
        }

        return NavigationUI.navigateUp(navController, drawerLayout);

    }

    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {

        drawerLayout.closeDrawers();

        Optional<MenuItem> clickedItem = navItemBehaviourMap.keySet()
                .stream().filter(current -> current.getItemId() == item.getItemId()).findAny();

        clickedItem.ifPresent(menuItem -> {

            Runnable behaviour = navItemBehaviourMap.get(menuItem);

            assert behaviour != null;
            behaviour.run();

        });

        return false;

    }

    /**
     * Invoked when the application ends.
     */
    @Override
    public void finish() {

        super.finish();

        // complete pending transactions
        ActivityNavigator.applyPopAnimationsToPendingTransition(this);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleUtil.onAttach(newBase));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        // gets fragment manager from the navigation host and not from the activity.
        FragmentManager fragmentManager = navHostFragment.getChildFragmentManager();
        List<Fragment> fragmentList = fragmentManager.getFragments();

        if (!fragmentList.isEmpty()) {

            Fragment fragment = fragmentList.get(0);

            if (fragment instanceof NavigationFragment) {

                NavigationFragment navigationFragment = (NavigationFragment) fragment;
                Navigable navigable = navigationFragment.getNavigable();

                navigable.onTouchEvent(event);

            }

        }

        return super.dispatchTouchEvent(event);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PERMISSIONS_REQUEST_CODE) {

            for (int i = 0 ; i < permissions.length ; i++) {

                String permission = permissions[i];
                int grantResult   = grantResults[i];

                if (permission.equalsIgnoreCase(ACTIVITY_RECOGNITION_PERMISSION)
                        && grantResult != PackageManager.PERMISSION_GRANTED) {
                    requestRequiredPermissions();
                }

            }

        }

    }

    //
    // PUBLIC METHODS
    //

    /**
     * This method is called on onResume().
     * It will refresh some components like navigation view
     * and check the authentication.
     */
    public void reload() {

        setupNavBehaviourMap();
        refreshNavigation();

        if (!IntroductionFragment.isCompleted(this)) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.home_frame_layout, new IntroductionFragment());
            fragmentTransaction.commit();

        } else if (!AuthActivity.isAuthenticated() && !AuthActivity.isAnonymousUser(this)) {

            attachLoadingFragment();

            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);

        } else {

            loadingFragment.close();
            loadingFragment = null;

        }

    }

    /**
     * Refresh the navigation view.
     */
    public void refreshNavigation() {

        MenuItem loginItem  = navigationView.getMenu().findItem(R.id.drawer_menu_login);
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

        if (updateSettingsFragment) {

            updateSettingsFragment = false;
            toolbar.setTitle(R.string.text_settings);

        }

    }

    /**
     * Add the respective behaviour of ech navigation menu item
     * to the local navItemBehaviourMap map.
     */
    public void setupNavBehaviourMap() {

        MenuItem loginItem          = navigationView.getMenu().findItem(R.id.drawer_menu_login);
        MenuItem logoutItem         = navigationView.getMenu().findItem(R.id.drawer_menu_logout);
        MenuItem addMeasureMenuItem = navigationView.getMenu().findItem(R.id.drawer_menu_add_measure);
        MenuItem settingsMenuItem   = navigationView.getMenu().findItem(R.id.drawer_menu_settings);

        navItemBehaviourMap.clear();

        navItemBehaviourMap.put(loginItem,          this :: loginNavClick);
        navItemBehaviourMap.put(logoutItem,         this :: logoutNavClick);
        navItemBehaviourMap.put(addMeasureMenuItem, this :: addMeasureItemNavClick);
        navItemBehaviourMap.put(settingsMenuItem,   this :: settingsItemNavClick);

    }

    /**
     * Update the settings fragment.
     *
     * <p>
     *     Useful after its creation or recreation.
     * </p>
     */
    public void updateSettingsFragment() {

        if (toolbar != null) {
            toolbar.setTitle(R.string.text_settings);
        } else {
            updateSettingsFragment = true;
        }

    }

    /**
     * Handle the grid item click of the tool's menu.
     * @param view The not null view instance.
     */
    public void handleToolGridItemClick(@NotNull View view) {

        ListToolsNavigation listToolsNavigation = ListToolsNavigation.getInstance();
        assert listToolsNavigation != null;

        listToolsNavigation.performToolItemClick(view);

    }

    //
    // PRIVATE METHODS
    //

    /**
     * Setup the ActionBar (ToolBar) with the hamburger button and
     * the navigation menu from left side.
     */
    private void setupNavigation() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // ActionBar is initialized by toolbar.
        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null; // ActionBar cannot be null
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        drawerLayout   = findViewById(R.id.home_drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navHostFragment = (NavHostFragment) Objects.requireNonNull(getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment));

        navController = navHostFragment.getNavController();

        navController.addOnDestinationChangedListener(this ::onNavigationDestinationChange);

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);

        Resources.Theme theme = new ContextThemeWrapper(getBaseContext(),
                R.style.LightTheme).getTheme();
        int iconColor = getResources().getColor(R.color.icons, theme);
        toolbar.setTitleTextColor(iconColor);

        assert toolbar.getNavigationIcon() != null;
        Drawable navigationIcon = toolbar.getNavigationIcon().mutate();
        navigationIcon.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);

        toolbar.setNavigationIcon(navigationIcon);

    }

    private void onNavigationDestinationChange(@NonNull NavController controller,
                                               @NonNull NavDestination destination,
                                               @Nullable Bundle arguments) {

        // we need any view to close the keyboard.
        View view = toolbar.getRootView();
        GenericUtil.closeKeyboard(view, this);

    }

    private void loginNavClick() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            firebaseAuth.signOut();
        }

        AuthActivity.setAnonymousUser(this, false);
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);

    }

    private void logoutNavClick() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            firebaseAuth.signOut();
        }

        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);

    }

    private void addMeasureItemNavClick() {

        navController.navigate(R.id.nav_list_tools_fragment);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

    private void settingsItemNavClick() {

        navController.navigate(R.id.nav_settings_fragment);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

    /**
     * Create and attach the loading fragment.
     * The loading fragment is a placeholder to keep hide the main application interface.
     */
    private void attachLoadingFragment() {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (loadingFragment != null && loadingFragment.isAdded()) {

            loadingFragment.close();
            loadingFragment = null;

        }

        loadingFragment = new LoadingFragment();
        fragmentTransaction.replace(R.id.home_frame_layout, loadingFragment);
        fragmentTransaction.commit();

    }

    /**
     * Ask to the user the required permissions.
     */
    private void requestRequiredPermissions() {

        // systems with sdk under or equals the 28 doesn't require this permission.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            return;
        }

        int permissionsAssert = ContextCompat.checkSelfPermission(this,
                ACTIVITY_RECOGNITION_PERMISSION);

        if (permissionsAssert == PackageManager.PERMISSION_GRANTED) {
            return;
        }

        boolean result = shouldShowRequestPermissionRationale(ACTIVITY_RECOGNITION_PERMISSION);

        if (!result) {

            String[] permissions = {
                    ACTIVITY_RECOGNITION_PERMISSION
            };

            ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_CODE);

        }

    }

}