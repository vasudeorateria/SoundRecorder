package com.kjstudios.soundrecorder.show_recorded_audio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kjstudios.soundrecorder.R;
import com.kjstudios.soundrecorder.play_recorded_audio.PlayRecordingFragment;

import java.io.File;
import java.util.ArrayList;

public class SavedRecordingFragment extends Fragment {

    private SavedRecordingViewModel mViewModel;
    RecyclerView recyclerView;
    ShowRecordingsAdapter showRecordingsAdapter = null;
    ArrayList<SavedRecodingModel> recordings = null;

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
        if (menuVisible) {
            recordings = mViewModel.get_recordings();
            showRecordingsAdapter = new ShowRecordingsAdapter(recordings,
                    new ShowRecordingsAdapter.itemClicked() {
                        @Override
                        public void itemclicked(String recordingName) {

                            new PlayRecordingFragment(recordingName)
                                    .show(getChildFragmentManager(), "play recording");

                        }
                    },
                    new ShowRecordingsAdapter.action() {
                        @Override
                        public void action_clicked(String audioname, int position, int id) {
                            switch (id) {
                                case R.id.edit:
                                    edit(audioname, position);
                                    break;
                                case R.id.share:
                                    share(audioname);
                                    break;
                                default:
                                    delete(audioname, position);
                            }
                        }
                    });
            recyclerView.setAdapter(showRecordingsAdapter);
        }
    }

    void edit(final String old_audioName, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter New File Name");
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String new_audioName = input.getText().toString();
                if(!new_audioName.equals(old_audioName)){
                    renaming(old_audioName , new_audioName , position);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


    }

    void renaming( String old_audioName , String new_audioName , int position) {

        String old_path = getContext().getExternalFilesDir("Recordings") + "/" + old_audioName;
        File old_file = new File(old_path);

        String new_path = getContext().getExternalFilesDir("Recordings") + "/" + new_audioName;
        File new_file = new File(new_path);

        old_file.renameTo(new_file);
        recordings.get(position).recordingName = new_audioName;
        showRecordingsAdapter.notifyItemChanged(position);

    }

    void share(String audioName) {

        String path = getContext().getExternalFilesDir("Recordings") + "/" + audioName;
        Uri uri = Uri.parse(path);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("audio/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(shareIntent);

    }

    void delete(String audioName, int position) {

        String path = getContext().getExternalFilesDir("Recordings") + "/" + audioName;
        File file = new File(path);
        file.delete();
        recordings.remove(position);
        showRecordingsAdapter.notifyItemRemoved(position);

    }

}