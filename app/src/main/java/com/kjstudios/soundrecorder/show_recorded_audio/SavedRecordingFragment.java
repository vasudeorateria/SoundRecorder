package com.kjstudios.soundrecorder.show_recorded_audio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kjstudios.soundrecorder.R;
import com.kjstudios.soundrecorder.play_recorded_audio.PlayRecordingFragment;

public class SavedRecordingFragment extends Fragment {

    private SavedRecordingViewModel mViewModel;
    RecyclerView recyclerView;

    public static SavedRecordingFragment newInstance() {
        return new SavedRecordingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.saved_recording_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SavedRecordingViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recordingsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if(menuVisible){
            ShowRecordingsAdapter showRecordingsAdapter = new ShowRecordingsAdapter(mViewModel.get_recordings(), new ShowRecordingsAdapter.itemClicked() {
                @Override
                public void itemclicked(String recordingName) {

                    new PlayRecordingFragment(recordingName)
                            .show(getChildFragmentManager() , "play recording");
                }
            });
            recyclerView.setAdapter(showRecordingsAdapter);
        }
    }

}