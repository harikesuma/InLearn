package com.example.inlearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.inlearn.adapter.CommentAdapter;
import com.example.inlearn.adapter.NotificationAdapter;
import com.example.inlearn.api.ApiClient;
import com.example.inlearn.api.ApiService;
import com.example.inlearn.data.model.Comment;
import com.example.inlearn.data.model.Notification;
import com.example.inlearn.data.model.ResponseNotification;
import com.example.inlearn.utils.SharedPreferencedHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {
    ApiService mApiService;
    NotificationAdapter notificationAdapter;
    RecyclerView recyclerView;
    private List<Notification> notificationList = new ArrayList<>();
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setTitle("Notification");

        mContext = this;
        mApiService = ApiClient.getService(mContext);

//        addData();
        getAllNotification();

        notificationAdapter = new NotificationAdapter(mContext, notificationList);
        recyclerView = findViewById(R.id.rv_notif);
        RecyclerView.LayoutManager verticalLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(notificationAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    public void getAllNotification(){
        mApiService.getAllNotification().enqueue(new Callback<ResponseNotification>() {
            @Override
            public void onResponse(Call<ResponseNotification> call, Response<ResponseNotification> response) {
                if (response.isSuccessful()){
                    Log.e("DEBUG","SUCESS GET Notif");
                    notificationList = response.body().getNotificationList();
                    recyclerView.setAdapter(new NotificationAdapter(mContext, notificationList));
                    notificationAdapter.notifyDataSetChanged();
                }
                else {
                    Log.e("DEBUG","tidak");
                }
            }

            @Override
            public void onFailure(Call<ResponseNotification> call, Throwable t) {

            }
        });
    }

    void addData(){
        notificationList = new ArrayList<>();
        notificationList.add(new Notification("Dimas Maulana", "1414370309"));
        notificationList.add(new Notification("Fadly Yonk", "1214234560"));
        notificationList.add(new Notification("Dimas Maulana", "1414370309"));
        notificationList.add(new Notification("Fadly Yonk", "1214234560"));
        notificationList.add(new Notification("Dimas Maulana", "1414370309"));
        notificationList.add(new Notification("Fadly Yonk", "1214234560"));

    }
}
