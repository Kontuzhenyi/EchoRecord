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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView t_2;
    private TextView t_1;
    //private static final int PERMISSION_REQ_CODE = 1;
    // PERMISSION_REQ_CODE задает код запроса для разрешения. Этот код используется для
    // идентификации запроса разрешения и обработки результата
    //private static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    // PERMISSION_WRITE_EXTERNAL_STORAGE строка, представляющая разрешение на запись аудио, взятая из Manifest.permission

    private Button btn_start_rec;
    private Button btn_stop_rec;
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

        // Создаем объект File, представляющий папку, которую мы хотим создать.
        // Путь к папке - это внешний каталог хранения устройства + "/Records" (имя папки).
        // Полный путь: storage/files/Records
        File folder = new File(getExternalFilesDir(null) + "/Records");

        // Проверяем, существует ли уже папка.
        if (!folder.exists()) {
            // Пытаемся создать папку.
            boolean success = folder.mkdirs();
            if (success) {
                // Папка успешно создана.
                Toast.makeText(this, "Folder created", Toast.LENGTH_SHORT).show();
            } else {
                // Не удалось создать папку.
                Toast.makeText(this, "Failed to create folder", Toast.LENGTH_SHORT).show();
            }
        }

        btn_start_rec = findViewById(R.id.start_rec);
        btn_stop_rec = findViewById(R.id.stop_rec);

        btn_start_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // какой-то прослушиватель
                // он будет вызывать функцию которая будет отправлять уведомления
                makeNotificationStart();
            }
        });

        btn_stop_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // какой-то прослушиватель
                // он будет вызывать функцию которая будет отправлять уведомления
                makeNotificationStop();
            }
        });



/*
        t_1 = findViewById(R.id.Text1);
        t_1.setText("Приложение не работает");
        t_1.setTextSize(30);
        t_2 = findViewById(R.id.Text2);
        t_2.setText("Разрешения не получены");
        t_2.setTextSize(30);



        b_1 = findViewById(R.id.btnNtf); // находим кнопку
*/
        // запрашиваем разрешения на показ уведомлений
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.RECORD_AUDIO) !=
                    PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, 101);
            }
        }

/*

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

            public void createFolder(View v) throws IOException {
                // Создаем объект File, представляющий папку, которую мы хотим создать.
                // Путь к папке - это внешний каталог хранения устройства + "/Records" (имя папки).
                // Полный путь: storage/files/Records
                File folder = new File(getExternalFilesDir(null) + "/Records");

                // Проверяем, существует ли уже папка.
                if (!folder.exists()) {
                    // Пытаемся создать папку.
                    boolean success = folder.mkdirs();
                    if (success) {
                        // Папка успешно создана.
                        Toast.makeText(this, "Folder created", Toast.LENGTH_SHORT).show();
                    } else {
                        // Не удалось создать папку.
                        Toast.makeText(this, "Failed to create folder", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Папка уже существует.
                    Toast.makeText(this, "Folder already exists", Toast.LENGTH_SHORT).show();
                }

                // Проверяем, существует ли папка.
                if (folder.exists()) {
                    // Создаем объект File для нового MP3 файла внутри папки.
                    File mp3File = new File(folder, "sample.mp3");

                    // Проверяем, существует ли уже файл.
                    if (!mp3File.exists()) {
                        // Пытаемся создать новый файл.
                        boolean success = mp3File.createNewFile();
                        if (success) {
                            // Файл успешно создан.
                            Toast.makeText(this, "File created", Toast.LENGTH_SHORT).show();
                            // Пишем данные в файл.
                            try (FileOutputStream fos = new FileOutputStream(mp3File)) {
                                byte[] mp3Data = new byte[]{(byte) 0xFF, (byte) 0xFB, 0x50, 0x40};
                                fos.write(mp3Data);
                                Toast.makeText(this, "MP3 file created and written", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Не удалось создать файл.
                            Toast.makeText(this, "Failed to create File", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Файл уже существует.
                        Toast.makeText(this, "File already exists", Toast.LENGTH_SHORT).show();
                    }
                }
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

 */
    }
    public void makeNotificationStart() {
        String chanelID = "CHANEL_ID_NOTIFICATION"; // создали идентификатор канала
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), chanelID);
        // настраиваем конструктор уведомления
        builder.setSmallIcon(R.drawable.is_notifications) // настроили значек уведомления
                .setContentTitle("Запись началась")
                //.setContentText("Some text for notification here")
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
                    //notificationChannel.enableVibration(true); // устанавливаем вибрацию
                    notificationManager.createNotificationChannel(notificationChannel); // самое важное
                    // диспетчер уведомлений создаёт канал для уведомлений. Мы бередаем это объект
                }
            }

        // вызываем диспетчер уведомлений
        notificationManager.notify(0, builder.build()); // для того чтобы это работало нужно попросить разрешение на уведомления
    }

    public void makeNotificationStop() {
        String chanelID = "CHANEL_ID_NOTIFICATION"; // создали идентификатор канала
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), chanelID);
        // настраиваем конструктор уведомления
        builder.setSmallIcon(R.drawable.is_notifications) // настроили значек уведомления
                .setContentTitle("Запись завершена")
                //.setContentText("Some text for notification here")
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
                //notificationChannel.enableVibration(true); // устанавливаем вибрацию
                notificationManager.createNotificationChannel(notificationChannel); // самое важное
                // диспетчер уведомлений создаёт канал для уведомлений. Мы бередаем это объект
            }
        }

        // вызываем диспетчер уведомлений
        notificationManager.notify(0, builder.build()); // для того чтобы это работало нужно попросить разрешение на уведомления
    }

    public void createFolder(View v) throws IOException {
        // Создаем объект File, представляющий папку, которую мы хотим создать.
        // Путь к папке - это внешний каталог хранения устройства + "/Records" (имя папки).
        // Полный путь: storage/files/Records
        File folder = new File(getExternalFilesDir(null) + "/Records");

        // Проверяем, существует ли уже папка.
        if (!folder.exists()) {
            // Пытаемся создать папку.
            boolean success = folder.mkdirs();
            if (success) {
                // Папка успешно создана.
                Toast.makeText(this, "Folder created", Toast.LENGTH_SHORT).show();
            } else {
                // Не удалось создать папку.
                Toast.makeText(this, "Failed to create folder", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Папка уже существует.
            Toast.makeText(this, "Folder already exists", Toast.LENGTH_SHORT).show();
        }
    }


}