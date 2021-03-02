package it.uniba.magr.misurapp.introduction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import it.uniba.magr.misurapp.R;

/**
 * The adapter to add pages for the introduction view.
 */
public class IntroPagerAdapter extends FragmentStatePagerAdapter {

    /**
     * List of pages.
     */
    private final List<Fragment> pagesArray = new ArrayList<>();

    @SuppressWarnings("squid:S1874")
    public IntroPagerAdapter(@NonNull FragmentManager fragmentManager) {

        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        pagesArray.add(new PageFragment(R.layout.intro_first_page));
        pagesArray.add(new PageFragment(R.layout.intro_second_page));
        pagesArray.add(new PageFragment(R.layout.intro_third_page));

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return pagesArray.get(position);
    }

    @Override
    public int getCount() {
        return pagesArray.size();
    }

    /**
     * The Page (represented by a fragment) of each introduction view.
     */
    public static class PageFragment extends Fragment {

        /**
         * The layout ID.
         */
        private final int layoutID;

        /**
         * @param layoutID The resource ID of the page layout.
         */
        public PageFragment(@LayoutRes int layoutID) {
            this.layoutID = layoutID;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup parent,
                                 @Nullable Bundle bundle) {
            return inflater.inflate(layoutID, parent, false);
        }

    }

}
