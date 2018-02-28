package com.oritmalki.quizapp.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

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

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        QuestionFragment createdFragment = (QuestionFragment) super.instantiateItem(container, position);


        return super.instantiateItem(container, position);
    }
}
