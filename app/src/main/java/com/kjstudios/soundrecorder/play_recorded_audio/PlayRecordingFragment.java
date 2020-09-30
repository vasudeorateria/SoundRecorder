package com.kjstudios.soundrecorder.play_recorded_audio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.kjstudios.soundrecorder.R;

import java.util.Locale;

public class PlayRecordingFragment extends DialogFragment {

    private PlayRecordingViewModel mViewModel;

    TextView tv_recordingName, currentTime, totalTime;
    SeekBar seekBar;
    ImageButton playPause;

    String recordingName;

    public PlayRecordingFragment() {

    }

    public PlayRecordingFragment(String recordingName) {
        this.recordingName = recordingName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.play_recording_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_recordingName = view.findViewById(R.id.recordingName);
        currentTime = view.findViewById(R.id.currentTime);
        totalTime = view.findViewById(R.id.totalTime);
        seekBar = view.findViewById(R.id.seekBar);
        playPause = view.findViewById(R.id.playPause);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PlayRecordingViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public void onResume() {
        super.onResume();

        init();

        mViewModel.totalTime.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer totalTime_val) {
                String time = String.format(Locale.getDefault(), "%02d:%02d",
                        totalTime_val / 60, totalTime_val % 60);
                totalTime.setText(time);
            }
        });

        mViewModel.currentTime.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer currentTime_val) {
                String time = String.format(Locale.getDefault(), "%02d:%02d",
                        currentTime_val / 60, currentTime_val % 60);
                currentTime.setText(time);
            }
        });

        mViewModel.progress.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer progress_val) {
                seekBar.setProgress(progress_val);
            }
        });


        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewModel.status) {
                    mViewModel.pause();
                    playPause.setImageDrawable(getContext().getDrawable(R.drawable.play_recording));
                } else {
                    if (seekBar.getProgress() == 100) {
                        mViewModel.progress.setValue(0);
                    }
                    mViewModel.play(seekBar.getProgress());
                    playPause.setImageDrawable(getContext().getDrawable(R.drawable.pause_recording));

                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser || progress == 100) {
                    mViewModel.pause();
                    mViewModel.currentTime.setValue(progress * mViewModel.totalTime.getValue() / 100);
                    playPause.setImageDrawable(getContext().getDrawable(R.drawable.play_recording));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    void init() {
        if (mViewModel.mediaPlayer == null) {
            mViewModel.initPlayer(recordingName);
        }

        if (mViewModel.currentTime == null) {
            mViewModel.currentTime.setValue(0);
        }

        if (mViewModel.totalTime == null) {
            mViewModel.totalTime.setValue(0);
        }

        if (mViewModel.progress == null) {
            mViewModel.progress.setValue(0);
        }

        if (mViewModel.status) {
            playPause.setImageDrawable(getContext().getDrawable(R.drawable.pause_recording));
        } else {
            playPause.setImageDrawable(getContext().getDrawable(R.drawable.play_recording));
        }
    }
}
