package com.example.echorecord;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView t_msg;
    private Button btn_start_rec;
    private Button btn_stop_rec;
    private boolean flagA;
    private boolean isCallActive;
    private String fileName;
    private MediaRecorder recorder;
    //private Integer countRecord = 1;
    private TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File folder = new File(getExternalFilesDir(null) + "/Records");
        if (!folder.exists()) {
            boolean success = folder.mkdirs();
            if (success) {
                Toast.makeText(this, "Folder created", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to create folder", Toast.LENGTH_SHORT).show();
            }
        }

        btn_start_rec = findViewById(R.id.start_rec);
        btn_stop_rec = findViewById(R.id.stop_rec);
        t_msg = findViewById(R.id.show_status);
        flagA = false; // false запись не идет
        isCallActive = false;

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        btn_start_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flagA) {
                    if (isCallActive) {
                        makeNotificationStart();
                        t_msg.setText("Запись идет");
                        flagA = true;
                        startRecording();
                    } else {
                        Toast.makeText(MainActivity.this, "Запись возможна только во время звонка", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_stop_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagA) {
                    makeNotificationStop();
                    t_msg.setText("Запись не идет");
                    flagA = false;
                    stopRecording();
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissionsNeeded = new ArrayList<>();

            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
            }

            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.READ_CALL_LOG);
            }

            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.PROCESS_OUTGOING_CALLS);
            }

            if (!permissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        permissionsNeeded.toArray(new String[0]), 101);
            }
        }
    }

    private final PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    isCallActive = false;
                    if (flagA) {
                        makeNotificationStop();
                        t_msg.setText("Запись не идет");
                        flagA = false;
                        stopRecording();
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    isCallActive = true;
                    if (!flagA) {
                        makeNotificationStart();
                        t_msg.setText("Запись идет");
                        flagA = true; // пошла запись
                        startRecording();
                    }
                    break;
                    /*
                case TelephonyManager.CALL_STATE_RINGING:
                    isCallActive = true;
                    makeNotificationStart();
                    t_msg.setText("Запись идет");
                    flagA = true; // пошла запись
                    startRecording();
                    break;

                     */
            }
        }
    };

    public void makeNotificationStart() {
        String chanelID = "CHANEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), chanelID);
        builder.setSmallIcon(R.drawable.is_notifications)
                .setContentTitle("Запись началась")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    notificationManager.getNotificationChannel(chanelID);
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(chanelID,
                        "Some description", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(0, builder.build());
    }

    public void makeNotificationStop() {
        String chanelID = "CHANEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), chanelID);
        builder.setSmallIcon(R.drawable.is_notifications)
                .setContentTitle("Запись завершена")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    notificationManager.getNotificationChannel(chanelID);
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(chanelID,
                        "Some description", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(0, builder.build());
    }

    private void startRecording() {
        // Текущее время
        Date currentDate = new Date();
        // Форматирование времени как "день.месяц.год"
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        // Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        fileName = getExternalFilesDir(null) + "/Records/" + dateText + " " + timeText + ".mp3";
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(fileName);

        try {
            recorder.prepare();
            recorder.start();
            Log.d("MainActivity", "Recording started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
            Log.d("MainActivity", "Recording stopped and saved to " + fileName);
            //countRecord = countRecord + 1;
        }
    }
}