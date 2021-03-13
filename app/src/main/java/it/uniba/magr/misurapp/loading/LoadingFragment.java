package it.uniba.magr.misurapp.loading;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import it.uniba.magr.misurapp.R;
import lombok.Getter;

/**
 * A fragment used as SplashScreen.
 *
 * This prevents that when the application starts, the main layout
 * is shown.
 */
@SuppressWarnings("squid:S110")
public class LoadingFragment extends Fragment {

    /**
     * The consumer that will be accepted when the fragment is resumed.
     */
    @Getter @Nullable
    private final Consumer<LoadingFragment> consumer;

    public LoadingFragment() {
        this(null);
    }

    public LoadingFragment(@Nullable Consumer<LoadingFragment> consumer) {
        this.consumer = consumer;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle bundle) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    public void onStart() {

        super.onStart();

        Activity activity = getActivity();
        assert activity != null;

        ConstraintLayout constraintLayout = activity.findViewById(R.id.loading_fragment_layout);
        constraintLayout.setOnClickListener(view -> {});

    }

    @Override
    public void onResume() {

        super.onResume();

        if (consumer != null) {
            consumer.accept(this);
        }

    }

    /**
     * Close the loading fragment.
     */
    public void close() {

        FragmentActivity activity = getActivity();

        if (activity != null) {

            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.remove(this).commit();

        }

    }

}