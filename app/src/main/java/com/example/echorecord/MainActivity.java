package com.example.echorecord;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView t_2;
    private TextView t_1;
    private static final int PERMISSION_REQ_CODE = 100;
    // PERMISSION_REQ_CODE задает код запроса для разрешения. Этот код используется для
    // идентификации запроса разрешения и обработки результата
    private static String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    // PERMISSION_RECORD_AUDIO строка, представляющая разрешение на запись аудио, взятая из Manifest.permission

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        t_1 = findViewById(R.id.Text1);
        t_1.setText("Приложение не работает");
        t_1.setTextSize(30);
        t_2 = findViewById(R.id.Text2);
        t_2.setText("Разрешения не получены");
        t_2.setTextSize(30);
    }

    public void onClickStart(View view) {

        t_1.setText("Приложение работает");
    }

    public void onClickFinish(View view) {

        t_1.setText("Приложение не работает");
    }

    public void onClickGet(View view) {
        requestRuntimePermission();
    }


    private void requestRuntimePermission()
    {
        // PackageManager.PERMISSION_GRANTED константа, используемая для проверки, было ли
        // предоставлено разрешение
        // checkSelfPermission(PERMISSION_RECORD_AUDIO) проверяет, предоставлено ли приложению
        // разрешение на запись аудио. Метод возвращает PackageManager.PERMISSION_GRANTED,
        // если разрешение предоставлено, и другую константу, если нет.
        if(checkSelfPermission(PERMISSION_RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
        {
            t_2.setText("Разрешения получены");
            t_2.setTextSize(30);
        }
        // Если разрешение не предоставлено, вызывается
        // ActivityCompat.requestPermissions(this, new String[]{PERMISSION_RECORD_AUDIO}, PERMISSION_REQ_CODE).
        // Этот метод запрашивает разрешение у пользователя. this представляет текущую активность,
        // new String[]{PERMISSION_RECORD_AUDIO} - массив с необходимыми разрешениями,
        // PERMISSION_REQ_CODE - код запроса.
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{PERMISSION_RECORD_AUDIO}, PERMISSION_REQ_CODE);
        }
    }

}