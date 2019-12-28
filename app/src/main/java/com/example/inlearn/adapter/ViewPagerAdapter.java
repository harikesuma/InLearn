package com.example.inlearn.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.inlearn.ui.dashboard.AnswerFragment;
import com.example.inlearn.ui.dashboard.QuestionFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new QuestionFragment(); //ChildFragment1 at position 0
            case 1:
                return new AnswerFragment(); //ChildFragment2 at position 1
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        if (position == 0){
            title = "Question";
        }
        else {
            title = "Comment";
        }
        return title;
    }
}
