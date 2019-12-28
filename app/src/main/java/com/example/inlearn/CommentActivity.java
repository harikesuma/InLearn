package com.example.inlearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inlearn.adapter.CommentAdapter;
import com.example.inlearn.adapter.HomeAdapter;
import com.example.inlearn.api.ApiClient;
import com.example.inlearn.api.ApiService;
import com.example.inlearn.data.model.Comment;
import com.example.inlearn.data.model.Question;
import com.example.inlearn.data.model.ResponseComment;
import com.example.inlearn.data.model.ResponseDetailQuestion;
import com.example.inlearn.data.model.ResponsePostComment;
import com.example.inlearn.data.model.ResponseQuestion;
import com.example.inlearn.utils.SharedPreferencedHelper;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    Context mContext;
    ApiService mApiService;
    ProgressDialog progressDialog;
    private ImageView ivProfile;
    private TextView tvName;
    private TextView tvkategori;
    private TextView tvDate;
    private TextView tvQuestion;
    private ImageView ivPict;
    private TextView tvEdited;
    String imagePath = "TAG_IMAGEPATH";
    private List<Comment> commentList = new ArrayList<>();
    private EditText etComment;
    SharedPreferencedHelper sharedPreferencedHelper;
    private Button btnPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        mContext = this;
        mApiService = ApiClient.getService(mContext);

        commentAdapter = new CommentAdapter(CommentActivity.this, commentList);
        recyclerView = findViewById(R.id.rv_comment);
        RecyclerView.LayoutManager verticalLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(commentAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ivProfile = findViewById(R.id.iv_profile);
        tvName = findViewById(R.id.tv_name);
        tvkategori = findViewById(R.id.tv_kategori);
        tvDate = findViewById(R.id.tv_date);
        tvQuestion = findViewById(R.id.tv_question);
        ivPict = findViewById(R.id.iv_pict);
        tvEdited = findViewById(R.id.tv_edited);
        etComment = findViewById(R.id.et_comment);
        btnPost = findViewById(R.id.btn_post);

        Intent i = getIntent();
        final int id = i.getExtras().getInt("ID");
//        Toast.makeText(this, "ID "+id, Toast.LENGTH_LONG).show();


        mApiService.getDetailQuestion(id).enqueue(new Callback<ResponseDetailQuestion>() {
            @Override
            public void onResponse(Call<ResponseDetailQuestion> call, Response<ResponseDetailQuestion> response) {
                if(response.isSuccessful()){
//                    Toast.makeText(CommentActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                    String createdDate = response.body().getCreated_at();
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


                    String pathUser = response.body().getUser_pict();
                    String imagePathUser = "https://inlearnmobileapp.000webhostapp.com/storage/user/"+ pathUser;

                    Picasso.get()
                            .load(imagePathUser)
                            .resize(200, 200)
                            .into(ivProfile, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
//                                    Toast.makeText(CommentActivity.this, "Image is loaded successfully", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onError(Exception e) {
//                                    Toast.makeText(CommentActivity.this, "Failed to load", Toast.LENGTH_LONG).show();
                                }
                            });

                    tvName.setText(response.body().getUser_name());
                    tvkategori.setText(response.body().getKategori());
                    tvDate.setText(date5);
                    tvQuestion.setText(response.body().getPertanyaan());

                    String path = response.body().getPict();
                    imagePath =  "https://inlearnmobileapp.000webhostapp.com/storage/pertanyaan/"+ path;

                    Picasso.get()
                            .load(imagePath)
                            .fit()
                            .centerCrop()
                            .into(ivPict, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
//                                    Toast.makeText(CommentActivity.this, "Image is loaded successfully", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onError(Exception e) {
//                                    Toast.makeText(CommentActivity.this, "Failed to load", Toast.LENGTH_LONG).show();
                                }
                            });

                }
            }

            @Override
            public void onFailure(Call<ResponseDetailQuestion> call, Throwable t) {

            }
        });

        mApiService.getComment(id).enqueue(new Callback<ResponseComment>() {
            @Override
            public void onResponse(Call<ResponseComment> call, Response<ResponseComment> response) {
                if (response.isSuccessful()){
//                    Toast.makeText(CommentActivity.this, "Berhasil Comment "+ response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    commentList = response.body().getCommentList();
                    recyclerView.setAdapter(new CommentAdapter(mContext,commentList));
                    commentAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<ResponseComment> call, Throwable t) {

            }
        });


        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferencedHelper = new SharedPreferencedHelper(CommentActivity.this);
                String userId = sharedPreferencedHelper.getSpId();
                String jawaban = etComment.getText().toString();

                if (jawaban.equals("")){
                    etComment.setError("please fill comment section!");
                }

                else {
                    mApiService.postingCommentRequest(id,userId,jawaban).enqueue(new Callback<ResponsePostComment>() {
                        @Override
                        public void onResponse(Call<ResponsePostComment> call, Response<ResponsePostComment> response) {
                            if (response.isSuccessful()){
//                            Toast.makeText(CommentActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                CommentActivity.this.onClick();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponsePostComment> call, Throwable t) {

                        }
                    });
                }


            }
        });


        ivPict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i = new Intent(CommentActivity.this, FullScreenImageActivity.class);
            i.putExtra("activity","CommentActivity");
            i.putExtra("path", imagePath);
            startActivity(i);
            }
        });
    }

    public void onClick (){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
