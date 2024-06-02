package com.example.echorecord;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView t_2;
    private TextView t_1;
    private static final int PERMISSION_REQ_CODE = 1;
    // PERMISSION_REQ_CODE задает код запроса для разрешения. Этот код используется для
    // идентификации запроса разрешения и обработки результата
    private static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    // PERMISSION_WRITE_EXTERNAL_STORAGE строка, представляющая разрешение на запись аудио, взятая из Manifest.permission

    private Button b_1;
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

        b_1 = findViewById(R.id.btnNtf); // находим кнопку

        // запрашиваем разрешения на показ уведомлений
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // если версия больше 13
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        b_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // какой-то прослушиватель
                // он будет вызывать функцию которая будет отправлять уведомления
                makeNotification();
            }
        });
    }

    public void makeNotification() {
        String chanelID = "CHANEL_ID_NOTIFICATION"; // создали идентификатор канала
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), chanelID);
        // настраиваем конструктор уведомления
        builder.setSmallIcon(R.drawable.is_notifications) // настроили значек уведомления
                .setContentTitle("Notification Tittle")
                .setContentText("Some text for notification here")
                .setAutoCancel(true) // включили какаю-то автоматическую отмену
                .setPriority(NotificationCompat.PRIORITY_DEFAULT); // настроили приоритет

        //Intent intent = new Intent(getApplicationContext(), Notifi)

        // Создаем объект для менеджера уведомлений
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Это необходимо если версия андроид выше Oreo
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) { // Проверяем является ли версия лучше чем Oreo
            NotificationChannel notificationChannel =
                    notificationManager.getNotificationChannel(chanelID); // передаем идентификатор канала
            if (notificationChannel == null) { // если канал null, то мы инициализируем его значениями
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(chanelID,
                        "Some description", importance); // передаем идентификатор, описание и важность
                notificationChannel.setLightColor(Color.GREEN); // устанавливаем цвет
                notificationChannel.enableVibration(true); // устанавливаем вибрацию
                notificationManager.createNotificationChannel(notificationChannel); // самое важное
                // диспетчер уведомлений создаёт канал для уведомлений. Мы бередаем это объект
            }
        }

        // вызываем диспетчер уведомлений
        notificationManager.notify(0, builder.build()); // для того чтобы это работало нужно попросить разрешение на уведомления
    }

    public void onClickStart(View view) {

        t_1.setText("Приложение работает");
    }

    public void onClickFinish(View view) {

        t_1.setText("Приложение не работает");
    }

    public void onClickGet1(View view) {
        requestRuntimePermission();
    }


    private void requestRuntimePermission()
    {
        // PackageManager.PERMISSION_GRANTED константа, используемая для проверки, было ли
        // предоставлено разрешение
        // checkSelfPermission(PERMISSION_WRITE_EXTERNAL_STORAGE) проверяет, предоставлено ли приложению
        // разрешение на запись аудио. Метод возвращает PackageManager.PERMISSION_GRANTED,
        // если разрешение предоставлено, и другую константу, если нет.
        if(checkSelfPermission(PERMISSION_WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            t_2.setText("Разрешения получены");
            t_2.setTextSize(30);
        }
        // Если разрешение не предоставлено, вызывается
        // ActivityCompat.requestPermissions(this, new String[]{PERMISSION_WRITE_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE).
        // Этот метод запрашивает разрешение у пользователя. this представляет текущую активность,
        // new String[]{PERMISSION_WRITE_EXTERNAL_STORAGE} - массив с необходимыми разрешениями,
        // PERMISSION_REQ_CODE - код запроса.
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{PERMISSION_WRITE_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
        }
    }

}