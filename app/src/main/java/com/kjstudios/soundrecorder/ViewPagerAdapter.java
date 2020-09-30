package com.kjstudios.soundrecorder;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kjstudios.soundrecorder.record_audio.RecordingFragment;
import com.kjstudios.soundrecorder.show_recorded_audio.SavedRecordingFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new RecordingFragment();
        } else {
            return new SavedRecordingFragment();
        }
    }


    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if(position ==0 ){
            title = "RECORD";
        }else{
            title = "SAVED RECORDINGS";
        }

        return title;
    }
}
