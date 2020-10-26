package it.uniba.magr.misurapp.introduction;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import it.uniba.magr.misurapp.introduction.page.FirstPage;
import it.uniba.magr.misurapp.introduction.page.SecondPage;
import it.uniba.magr.misurapp.introduction.page.ThirdPage;

public class IntroPagerAdapter extends FragmentStatePagerAdapter {

    private static final int PAGES = 3;

    private final Fragment[] pages = new Fragment[PAGES];

    public IntroPagerAdapter(@NonNull FragmentManager fragmentManager) {

        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        pages[0] = new FirstPage();
        pages[1] = new SecondPage();
        pages[2] = new ThirdPage();

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return pages[position];
    }

    @Override
    public int getCount() {
        return pages.length;
    }

}
