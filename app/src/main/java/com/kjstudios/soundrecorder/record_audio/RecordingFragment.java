package com.kjstudios.soundrecorder.record_audio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.kjstudios.soundrecorder.R;

import java.util.Locale;

public class RecordingFragment extends Fragment {

    private RecordingViewModel mViewModel;

    TextView recordingTimer;
    ImageButton recordStartStop;



    public static RecordingFragment newInstance() {
        return new RecordingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recording_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RecordingViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recordingTimer = view.findViewById(R.id.recordingTimer);
        recordStartStop = view.findViewById(R.id.recordStartStop);

    }

    @Override
    public void onResume() {
        super.onResume();
        recordStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.timerStartStop();
            }
        });

        mViewModel.recordingTime.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer recordingTime) {
                String formatedTime = String.format(Locale.getDefault(), "%02d:%02d",
                        recordingTime/60, recordingTime%60);
                recordingTimer.setText(formatedTime);
            }
        });
    }
}