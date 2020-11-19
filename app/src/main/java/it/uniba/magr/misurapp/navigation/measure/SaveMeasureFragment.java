package it.uniba.magr.misurapp.navigation.measure;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.R;

public class SaveMeasureFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             @Nullable ViewGroup parent,
                             @Nullable Bundle bundle) {
        return inflater.inflate(R.layout.fragment_save_measure, parent, false);
    }

}
