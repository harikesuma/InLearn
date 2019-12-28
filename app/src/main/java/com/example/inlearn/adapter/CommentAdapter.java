package com.example.inlearn.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inlearn.R;
import com.example.inlearn.api.ApiClient;
import com.example.inlearn.api.ApiService;
import com.example.inlearn.data.model.Comment;
import com.example.inlearn.data.model.Question;
import com.example.inlearn.data.model.ResponseComment;
import com.example.inlearn.data.model.ResponseCommentLike;
import com.example.inlearn.utils.SharedPreferencedHelper;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<Comment> commentList;
    private Context context;
    ApiService mApiService;
    public int id;
    SharedPreferencedHelper sharedPreferencedHelper;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.commentList = commentList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_answer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, final int position) {
        id = commentList.get(position).getId();

        sharedPreferencedHelper = new SharedPreferencedHelper(context);
        String userIdSP = sharedPreferencedHelper.getSpId();
        String userId = commentList.get(position).getUser_id();



        if (userId.equals(userIdSP)){
            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    id = commentList.get(position).getId();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Delete Comment");
                    builder.setMessage("What you want to do?");

                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog

                                onClickDelete(id);
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                    return true;
                }
            });
        }

        else {
            holder.ibLike.setVisibility(ImageButton.VISIBLE);
        }

            String createdDate = commentList.get(position).getCreated_at();
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

            String path = commentList.get(position).getPict();

            String imagePath =  "https://inlearnmobileapp.000webhostapp.com/storage/user/"+ path;

            Picasso.get()
                    .load(imagePath)
                    .fit()
                    .centerCrop()
                    .into(holder.ivProfile, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
//                            Toast.makeText(context, "Image is loaded successfully", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(Exception e) {
//                            Toast.makeText(context, "Failed to load", Toast.LENGTH_LONG).show();
                        }
                    });
            holder.tvName.setText(commentList.get(position).getName());
            holder.tvComment.setText(commentList.get(position).getComment());
            holder.tvDate.setText(date5);
            holder.tvLike.setText(commentList.get(position).getLike());

            holder.ibLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mApiService = ApiClient.getService(context);
                    String userId = sharedPreferencedHelper.getSpId();
                    int id = commentList.get(position).getId();
//                Toast.makeText(context, "ID "+ id , Toast.LENGTH_LONG).show();

                    mApiService.commentLike(id,userId).enqueue(new Callback<ResponseCommentLike>() {
                        @Override
                        public void onResponse(Call<ResponseCommentLike> call, Response<ResponseCommentLike> response) {
                            if (response.isSuccessful()){
                                String msg = response.body().getMsg();
                                Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseCommentLike> call, Throwable t) {

                        }
                    });
                }
            });
        }

    @Override
    public int getItemCount() {
        return (commentList != null) ? commentList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProfile;
        private TextView tvName;
        private TextView tvDate;
        private TextView tvComment;
        private ImageButton ibLike;
        private TextView tvLike;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvName = itemView.findViewById(R.id.tv_name);
            tvComment = itemView.findViewById(R.id.tv_answer);
            tvDate = itemView.findViewById(R.id.tv_date);
            ibLike = itemView.findViewById(R.id.ib_like);
            tvLike = itemView.findViewById(R.id.tv_like);
            cardView = itemView.findViewById(R.id.cv_answer);
        }
    }

    private void onClickDelete(final int id){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                mApiService = ApiClient.getService(context);
                mApiService.deleteComment(id).enqueue(new Callback<ResponseComment>() {
                    @Override
                    public void onResponse(Call<ResponseComment> call, Response<ResponseComment> response) {
                        if (response.isSuccessful()){
//                            Toast.makeText(context, response.body().getMsg(),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseComment> call, Throwable t) {

//                        Toast.makeText(context, t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

}
