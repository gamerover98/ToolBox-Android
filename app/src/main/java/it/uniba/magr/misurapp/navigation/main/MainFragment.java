package it.uniba.magr.misurapp.navigation.main;

import android.os.Bundle;

import org.jetbrains.annotations.Nullable;
import it.uniba.magr.misurapp.navigation.NavigationFragment;

/**
 * The main fragment of the main layout of the application.
 */
public class MainFragment extends NavigationFragment {

    public MainFragment() {
        super(new MainNavigation());
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {

        super.onCreate(bundle);
        getNavigable().onCreate(bundle);

    }

    @Override
    protected void performTransactions() {
        // no enter and exit transactions.
    }

}
