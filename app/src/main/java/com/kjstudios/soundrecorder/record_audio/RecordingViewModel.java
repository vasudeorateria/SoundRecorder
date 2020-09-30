package com.kjstudios.soundrecorder.record_audio;

import android.app.Application;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.io.File;

public class RecordingViewModel extends AndroidViewModel {

    MediaRecorder audioRecorder = new MediaRecorder();

    MutableLiveData<Integer> recordingTime = new MutableLiveData<>();
    boolean status = false;
    int count = 0;
    String recording = null;

    public RecordingViewModel(@NonNull Application application) {
        super(application);
    }

    public void timerStartStop() {
        if (!status) {
            startTimer();
        } else {
            stopTimer();
        }
    }

    public void startTimer() {
        status = true;
        Toast.makeText(getApplication().getApplicationContext(), "Recording Has Started", Toast.LENGTH_SHORT).show();
        startRecording();
        recordingTime.setValue(0);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status) {
                    recordingTime.setValue(recordingTime.getValue() + 1);
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);

    }

    public void stopTimer() {
        status = false;
        Toast.makeText(getApplication().getApplicationContext(), "Recording Stopped", Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplication().getApplicationContext(), "Recording saved", Toast.LENGTH_SHORT).show();
        recordingTime.setValue(0);
        stopRecording();
    }


    public void startRecording() {

        try {

            File recordingsFolder = getApplication().getApplicationContext().getExternalFilesDir("Recordings");
            if (!recordingsFolder.exists()) {
                if (recordingsFolder.mkdir()) ; //directory is created;
            }
            String file_name = "My_Recording_" + (recordingsFolder.listFiles().length +1) + ".mp3";


            recording = recordingsFolder.getPath() + "/" + file_name;

            audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            audioRecorder.setOutputFile(recording);
            audioRecorder.setAudioSamplingRate(44100);
            audioRecorder.setAudioEncodingBitRate(192000);
            audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            audioRecorder.prepare();
            audioRecorder.start();

        } catch (Exception e) {
            Log.e("Recorder exception", e.toString());
        }
    }

    public void stopRecording() {
        try {
            audioRecorder.stop();
            audioRecorder.reset();
        }catch (Exception e){
            Log.e("recording exception" , e.toString());
        }
    }


}