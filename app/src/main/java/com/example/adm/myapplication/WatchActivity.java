package com.example.adm.myapplication;

import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Properties;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WatchActivity extends AppCompatActivity {

    private static final String Tag = "WatchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch);

        AssetsPropertyReader reader = new AssetsPropertyReader(this);
        Properties properties = reader.getProperties("application.properties");

        String channelId  = getString(R.string.default_notification_channel_id);
        String channelName = getString(R.string.default_notification_channel_name);
        NotificationManager notificationManager =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        String item = getIntent().getStringExtra("selectedItem");

        String token = FirebaseInstanceId.getInstance().getToken();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(properties.getProperty("apiUrl"))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TornadoAPI api = retrofit.create(TornadoAPI.class);

        api.bindApplication(token, item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((data) -> {
                    Log.d(Tag, "Application " + token + " watch " + item);
                }, error -> {
                    Log.e(Tag, error.getMessage());
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationEvent event) {
        Log.d(Tag, "Get new notification");

        ImageView view = findViewById(R.id.imageView);

        view.setImageBitmap(event.getBody());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
