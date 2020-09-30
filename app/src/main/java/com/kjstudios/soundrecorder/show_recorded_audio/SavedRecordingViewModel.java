package com.kjstudios.soundrecorder.show_recorded_audio;

import android.app.Application;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SavedRecordingViewModel extends AndroidViewModel {

    public SavedRecordingViewModel(@NonNull Application application) {
        super(application);
    }

    public ArrayList<SavedRecodingModel> get_recordings() {

        ArrayList<SavedRecodingModel> savedRecordings = new ArrayList<>();

        String[] recordings = getApplication().getApplicationContext().getExternalFilesDir("Recordings").list();
        for (String recording : recordings) {
            SavedRecodingModel savedRecording = new SavedRecodingModel();
            try {

                File file = new File(getApplication().getApplicationContext()
                        .getExternalFilesDir("Recordings").getPath()+'/'+recording);

                savedRecording.setRecordingName(recording);

                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(file.getPath());
                mediaPlayer.prepare();
                int duration = mediaPlayer.getDuration()/1000;
                String formatDuration = String.format(Locale.getDefault(), "%02d:%02d",
                        duration/ 60, duration % 60);
                savedRecording.setDuration(formatDuration);
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                savedRecording.setLastModified(formatter.format(file.lastModified()));

                savedRecordings.add(savedRecording);

            } catch (Exception e) {
                Log.e("exception", e.toString());
            }
        }
        return savedRecordings;
    }

}