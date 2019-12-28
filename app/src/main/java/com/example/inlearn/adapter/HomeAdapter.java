package com.example.inlearn.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inlearn.CommentActivity;
import com.example.inlearn.EditProfileActivity;
import com.example.inlearn.PostQuestionActivity;
import com.example.inlearn.R;
import com.example.inlearn.api.ApiClient;
import com.example.inlearn.api.ApiService;
import com.example.inlearn.data.model.Question;
import com.example.inlearn.data.model.ResponseComment;
import com.example.inlearn.data.model.ResponseDetailQuestion;
import com.example.inlearn.data.model.ResponseQuestion;
import com.example.inlearn.utils.SharedPreferencedHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private List<Question> questionList;
    private Context context;
    SharedPreferencedHelper sharedPreferencedHelper;
    ApiService mApiService;
    public int id;

    public HomeAdapter(Context context,List<Question> questionList) {
        this.questionList = questionList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        id = questionList.get(position).getId();

        sharedPreferencedHelper = new SharedPreferencedHelper(context);
        String userIdSP = sharedPreferencedHelper.getSpId();
        String userId = questionList.get(position).getUser_id();

        if (questionList.get(position).getEdited().equals("1")){
            holder.tvEdited.setVisibility(View.VISIBLE);
        }
        else {
            holder.tvEdited.setVisibility(View.GONE);
        }


        if (userId.equals(userIdSP)){
            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    id = questionList.get(position).getId();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Post");
                    builder.setMessage("What you want to do?");

                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                           onClickDelete(id);
                        }
                    });

                    builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            dialog.dismiss();
                        }
                    });


                    builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                           onClickEdit(id);
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                    return true;
                }
            });
        }

        else {
            holder.ibComment.setVisibility(ImageButton.VISIBLE);
        }

        Log.e("DEBUG", "ID USER = "+ userId +" SP ID USER = "+ userIdSP);

         String createdDate = questionList.get(position).getCreated_at();
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

         String path = questionList.get(position).getPict();

         String imagePath =  "https://inlearnmobileapp.000webhostapp.com/storage/pertanyaan/"+ path;

        Picasso.get()
                .load(imagePath)
                .fit()
                .centerCrop()
                .into(holder.ivPict, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
//                        Toast.makeText(context, "Image is loaded successfully", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Exception e) {
//                        Toast.makeText(context, "Failed to load", Toast.LENGTH_LONG).show();
                    }
                });

        String pathUser = questionList.get(position).getUser_pict();

        String imagePathUser = "https://inlearnmobileapp.000webhostapp.com/storage/user/"+ pathUser;

        Picasso.get()
                .load(imagePathUser)
                .resize(200, 200)
                .into(holder.ivProfile, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
//                        Toast.makeText(context, "Image is loaded successfully", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Exception e) {
//                        Toast.makeText(context, "Failed to load", Toast.LENGTH_LONG).show();
                    }
                });

            holder.tvName.setText(questionList.get(position).getUser_name());
            holder.tvkategori.setText(questionList.get(position).getKategori());
            holder.tvQuestion.setText(questionList.get(position).getPertanyaan());
            holder.tvDate.setText(date5);
            holder.tvComment.setText(questionList.get(position).getTotal_jawaban());


        holder.ibComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = questionList.get(position).getId();
                Intent i = new Intent(context, CommentActivity.class);
                i.putExtra("ID", id);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (questionList != null) ? questionList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProfile;
        private TextView tvName;
        private TextView tvkategori;
        private TextView tvDate;
        private TextView tvQuestion;
        private ImageView ivPict;
        private ImageButton ibComment;
        private TextView tvComment;
        private CardView cardView;
        private TextView tvEdited;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvName = itemView.findViewById(R.id.tv_name);
            tvkategori = itemView.findViewById(R.id.tv_kategori);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvQuestion = itemView.findViewById(R.id.tv_question);
            ivPict = itemView.findViewById(R.id.iv_pict);
            ibComment = itemView.findViewById(R.id.ib_comment);
            tvComment = itemView.findViewById(R.id.tv_count_comment);
            cardView = itemView.findViewById(R.id.cv_question);
            tvEdited = itemView.findViewById(R.id.tv_edited);

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
                mApiService.deleteQuestion(id).enqueue(new Callback<ResponseQuestion>() {
                    @Override
                    public void onResponse(Call<ResponseQuestion> call, Response<ResponseQuestion> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(context, response.body().getMsg(),Toast.LENGTH_LONG).show();
                    }
                    }

                    @Override
                    public void onFailure(Call<ResponseQuestion> call, Throwable t) {

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

    public void onClickEdit(final int id){
        mApiService = ApiClient.getService(context);
        mApiService.showEditQuestion(id).enqueue(new Callback<ResponseDetailQuestion>() {
            @Override
            public void onResponse(Call<ResponseDetailQuestion> call, Response<ResponseDetailQuestion> response) {
                if (response.isSuccessful()){
//                    Toast.makeText(context, response.body().getMsg(),Toast.LENGTH_LONG).show();
                    int id = response.body().getId();
                    String question = response.body().getPertanyaan();
                    int kategori_id = response.body().getKategori_id();
                    String pict = response.body().getPict();

                    Intent i = new Intent(context, PostQuestionActivity.class);
                    i.putExtra("id",id);
                    i.putExtra("pict", pict);
                    i.putExtra("question",question);
                    i.putExtra("kategori_id", kategori_id);
                    context.startActivity(i);

                }
            }

            @Override
            public void onFailure(Call<ResponseDetailQuestion> call, Throwable t) {
//                Toast.makeText(context, t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
}
