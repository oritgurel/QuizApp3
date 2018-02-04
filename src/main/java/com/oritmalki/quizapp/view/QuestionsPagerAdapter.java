package com.oritmalki.quizapp.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Orit on 1.2.2018.
 */

public class QuestionsPagerAdapter extends FragmentStatePagerAdapter {

    private List<QuestionFragment> fragments;

    public QuestionsPagerAdapter(FragmentManager fm, List<QuestionFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
