package com.example.inlearn.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inlearn.R;
import com.example.inlearn.data.model.Comment;
import com.example.inlearn.data.model.Notification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<Notification> notificationList;
    private Context context;

    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.notificationList = notificationList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        String createdDate = notificationList.get(position).getCreatedAt();
        createdDate = createdDate.substring(0,10);

        SimpleDateFormat formatter4= new SimpleDateFormat("yyyy-MM-dd");
        Date date4 = null;
        try {
            date4 = formatter4.parse(createdDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("DATE","tanggal"+ date4);

        SimpleDateFormat target = new SimpleDateFormat("E, dd-MM-yyyy");
        String date5 = target.format(date4);

        holder.tvNotif.setText(notificationList.get(position).getNotification());
        holder.tvDate.setText(date5);
    }

    @Override
    public int getItemCount() {
        return (notificationList != null) ? notificationList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNotif;
        private TextView tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNotif = itemView.findViewById(R.id.tv_notif);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}
