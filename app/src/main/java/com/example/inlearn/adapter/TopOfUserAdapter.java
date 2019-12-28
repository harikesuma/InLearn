package com.example.inlearn.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inlearn.R;
import com.example.inlearn.data.model.ResponseTopOfUser;
import com.example.inlearn.data.model.TopOfUser;

import java.util.List;

public class TopOfUserAdapter extends RecyclerView.Adapter<TopOfUserAdapter.ViewHolder> {
    List<TopOfUser> responseTopOfUserList;

    public TopOfUserAdapter(List<TopOfUser> responseTopOfUserList) {
        this.responseTopOfUserList = responseTopOfUserList;
    }

    @NonNull
    @Override
    public TopOfUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_top_of_user, parent, false);
        return new TopOfUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopOfUserAdapter.ViewHolder holder, int position) {
        holder.name.setText(responseTopOfUserList.get(position).getUser_name());
        holder.badge.setText(responseTopOfUserList.get(position).getLike());
    }

    @Override
    public int getItemCount() {
        return (responseTopOfUserList != null) ? responseTopOfUserList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView badge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_name);
            badge = itemView.findViewById(R.id.tv_badge);
        }
    }
}
