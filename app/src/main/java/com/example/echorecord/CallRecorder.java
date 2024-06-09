package com.example.echorecord;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CallRecorder {

    private MediaRecorder recorder;
    private boolean isRecording = false;
    private String outputFilePath;

    public void startRecording() {
        if (isRecording) {
            return;
        }

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL); // VOICE_CALL for recording both sides
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        File outputDir = new File(Environment.getExternalStorageDirectory(), "CallRecordings");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        outputFilePath = outputDir.getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".3gp";
        recorder.setOutputFile(outputFilePath);

        try {
            recorder.prepare();
            recorder.start();
            isRecording = true;
            Log.d("CallRecorder", "Recording started.");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("CallRecorder", "Recording failed.");
        }
    }

    public void stopRecording() {
        if (isRecording) {
            recorder.stop();
            recorder.release();
            isRecording = false;
            Log.d("CallRecorder", "Recording stopped. Saved to: " + outputFilePath);
        }
    }
}

