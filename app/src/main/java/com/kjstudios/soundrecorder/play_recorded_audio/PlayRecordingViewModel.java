package com.kjstudios.soundrecorder.play_recorded_audio;

import android.app.Application;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class PlayRecordingViewModel extends AndroidViewModel {

    public PlayRecordingViewModel(@NonNull Application application) {
        super(application);
    }

    // not started
    // playing
    // not playing or paused
    // completed

    boolean status;
    MutableLiveData<Integer> progress = new MutableLiveData<>();
    MutableLiveData<Integer> currentTime = new MutableLiveData<>();
    MutableLiveData<Integer> totalTime = new MutableLiveData<>();
    int increment = 0;

    MediaPlayer mediaPlayer = null;
    Handler handler = new Handler();


    void initPlayer(String recordingName) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(getApplication().getApplicationContext()
                    .getExternalFilesDir("Recordings") + "/" + recordingName);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    currentTime.setValue(0);
                    totalTime.setValue(mediaPlayer.getDuration() / 1000);
                    progress.setValue(0);
                    increment = (int) Math.ceil(100 / totalTime.getValue());
                }
            });
        } catch (Exception e) {
            Log.e("media player exception", e.toString());
        }
    }

    void play(int position) {
        status = true;
        currentTime.setValue(position * totalTime.getValue() / 100);
        progress.setValue(position);
        mediaPlayer.seekTo(currentTime.getValue() * 1000);
        mediaPlayer.start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentTime.getValue() < totalTime.getValue() && status) {

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            pause();
                            currentTime.setValue(totalTime.getValue());
                            progress.setValue(100);
                        }
                    });
                    currentTime.setValue(currentTime.getValue() + 1);
                    progress.setValue(progress.getValue() + increment);
                    handler.postDelayed(this, 1000);

                }
            }
        }, 1000);
    }

    void pause() {
        handler.removeCallbacksAndMessages(null);
        mediaPlayer.pause();
        status = false;
    }
}
