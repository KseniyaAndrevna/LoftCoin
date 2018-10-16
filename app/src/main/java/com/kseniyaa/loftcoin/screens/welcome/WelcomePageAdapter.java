package com.kseniyaa.loftcoin.screens.welcome;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kseniyaa.loftcoin.R;

import java.util.ArrayList;
import java.util.List;

public class WelcomePageAdapter extends FragmentPagerAdapter {

    private List <WelcomePage> pages;

    public WelcomePageAdapter(FragmentManager fm) {
        super(fm);

        pages = new ArrayList<>();
        pages.add(new WelcomePage (R.drawable.img_welcome_1, R.string.welcome_title_1, R.string.welcome_subtitle_1));
        pages.add(new WelcomePage (R.drawable.img_welcome_2, R.string.welcome_title_2, R.string.welcome_subtitle_2));
        pages.add(new WelcomePage (R.drawable.img_welcome_3, R.string.welcome_title_3, R.string.welcome_subtitle_3));
    }

    @Override
    public Fragment getItem(int position) {
        return WelcomeFragment.newInstance(pages.get(position));
    }

    @Override
    public int getCount() {
        return pages.size();
    }
}
