package it.uniba.magr.misurapp.introduction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import it.uniba.magr.misurapp.R;
import lombok.RequiredArgsConstructor;

public class IntroPagerAdapter extends FragmentStatePagerAdapter {

    private static final int PAGES = 3;

    private final Fragment[] pagesArray = new Fragment[PAGES];

    @SuppressWarnings("squid:S1874")
    public IntroPagerAdapter(@NonNull FragmentManager fragmentManager) {

        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        pagesArray[0] = new PageFragment(R.layout.intro_first_page);
        pagesArray[1] = new PageFragment(R.layout.intro_second_page);
        pagesArray[2] = new PageFragment(R.layout.intro_third_page);

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return pagesArray[position];
    }

    @Override
    public int getCount() {
        return pagesArray.length;
    }

    @RequiredArgsConstructor
    public static class PageFragment extends Fragment {

        private final int layoutID;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup parent,
                                 @Nullable Bundle bundle) {
            return inflater.inflate(layoutID, parent, false);
        }

    }

}
