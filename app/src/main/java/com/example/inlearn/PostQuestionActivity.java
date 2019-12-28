package com.example.inlearn;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inlearn.adapter.KategoriAdapter;
import com.example.inlearn.api.ApiClient;
import com.example.inlearn.api.ApiService;
import com.example.inlearn.data.model.Kategori;
import com.example.inlearn.data.model.ResponseKategori;
import com.example.inlearn.data.model.ResponsePostQuestion;
import com.example.inlearn.data.model.ResponseQuestion;
import com.example.inlearn.data.model.User;
import com.example.inlearn.data.room.RoomDatabase;
import com.example.inlearn.utils.SharedPreferencedHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostQuestionActivity extends AppCompatActivity {
    EditText etQuestion;
    Spinner spKategori;
    Button  btnChoosePict;
    Button btnPost;
    ImageView ivPict;
    final static int  REQUEST_GALLERY = 1;
    String mediaPath;
    SharedPreferencedHelper sharedPreferencedHelper;
    String access_token;
    Context mContext;
    ApiService mApiService;
    List<Kategori> kategoriList = new ArrayList<>();
    KategoriAdapter kategoriAdapter;
    String kategoriId;
    int newKategori;
    public static RoomDatabase roomDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_question);

        sharedPreferencedHelper = new SharedPreferencedHelper(this);
        mContext = this;
        mApiService = ApiClient.getService(getApplicationContext());

        ivPict = findViewById(R.id.iv_pict_holder);
        btnChoosePict = findViewById(R.id.btn_choose_pict);
        etQuestion = findViewById(R.id.et_question);
        spKategori = findViewById(R.id.sp_kategori);
        btnPost = findViewById(R.id.btn_post);


        kategoriAdapter = new KategoriAdapter(this, kategoriList);
        spKategori.setAdapter(kategoriAdapter);

        spKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Kategori kategori = (Kategori) parent.getItemAtPosition(position);
                String kategoriName = kategori.getKategori();
                kategoriId = String.valueOf(kategori.getId());

//                Toast.makeText(PostQuestionActivity.this, kategoriName, Toast.LENGTH_LONG).show();
//                Toast.makeText(PostQuestionActivity.this, kategoriId, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Intent i = getIntent();

        if(i.getExtras() != null){
            Toast.makeText(mContext, "INTENT EXIST", Toast.LENGTH_LONG).show();
            final int id =i.getExtras().getInt("id");
            String question = i.getExtras().getString("question");
            final int kategori_id =  i.getExtras().getInt("kategori_id");
            String path =  i.getExtras().getString("pict");

            btnChoosePict.setVisibility(View.GONE);
            String imagePath = "https://inlearnmobileapp.000webhostapp.com/storage/pertanyaan/"+ path;

            Picasso.get()
                    .load(imagePath)
                    .resize(200, 200)
                    .into(ivPict, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(mContext, "Image is loaded successfully", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(mContext, "Failed to load", Toast.LENGTH_LONG).show();
                        }
                    });
            etQuestion.setText(question);

            btnPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    spKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            Kategori kategori = (Kategori) parent.getItemAtPosition(position);
//                            newKategori = kategori.getId();
//                            Toast.makeText(mContext, "NEW KATEGORI = "+ newKategori, Toast.LENGTH_LONG).show();
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//                        }
//                    });

                    String newQuestion = etQuestion.getText().toString();

                    mApiService.editQuestion(id,newQuestion,kategoriId).enqueue(new Callback<ResponseQuestion>() {
                        @Override
                        public void onResponse(Call<ResponseQuestion> call, Response<ResponseQuestion> response) {
                            if (response.isSuccessful()){
                                Toast.makeText(mContext,response.body().getMsg(), Toast.LENGTH_LONG).show();
                                Intent i = new Intent(mContext, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseQuestion> call, Throwable t) {
                            Toast.makeText(mContext,"UPDATE FAILED", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            });


        }

        else {
            btnPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String question = etQuestion.getText().toString();

                    if(mediaPath == null){
                        Toast.makeText(PostQuestionActivity.this, "Please Select photos", Toast.LENGTH_LONG).show();
                    }

                    else if (question.equals("")){
                        etQuestion.setError("Please fill the question section");
                    }

                    else {
                        String userId = sharedPreferencedHelper.getSpId();
                        File imageFile = new File(mediaPath);

                        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"),imageFile);
                        RequestBody requestBodyUserId = RequestBody.create(okhttp3.MultipartBody.FORM, userId);
                        RequestBody requestBodyQuestion = RequestBody.create(okhttp3.MultipartBody.FORM, question);
                        RequestBody requestBodyKategoriId = RequestBody.create(okhttp3.MultipartBody.FORM, kategoriId);
                        MultipartBody.Part partImage = MultipartBody.Part.createFormData("imageUpload", imageFile.getName(),requestBody);
                        Call<ResponsePostQuestion> postQuestion = mApiService.postingQuestionRequest(partImage, requestBodyUserId, requestBodyQuestion, requestBodyKategoriId);

                        Log.e("DEBUG", "HASIL"+ postQuestion);

                        postQuestion.enqueue(new Callback<ResponsePostQuestion>() {
                            @Override
                            public void onResponse(Call<ResponsePostQuestion> call, Response<ResponsePostQuestion> response) {
                                if(response.isSuccessful()){
                                    Toast.makeText(PostQuestionActivity.this,response.body().getMsg(), Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(PostQuestionActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponsePostQuestion> call, Throwable t) {
                                Toast.makeText(PostQuestionActivity.this,t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        }


        mApiService.getKategori().enqueue(new Callback<ResponseKategori>() {
            @Override
            public void onResponse(Call<ResponseKategori> call, Response<ResponseKategori> response) {
                if (response.isSuccessful()){
                    kategoriList = response.body().getKategoriList();

                    spKategori.setAdapter(new KategoriAdapter(mContext,kategoriList));
                    kategoriAdapter.notifyDataSetChanged();

                    roomDatabase = Room.databaseBuilder(mContext,
                            RoomDatabase.class,"db_praktikum_progmob").allowMainThreadQueries().build();
                    Kategori kategori = new Kategori();
                    for (int i = 0; i<kategoriList.size(); i++){
                        kategori.setId(kategoriList.get(i).getId());
                        kategori.setKategori(kategoriList.get(i).getKategori());
                        roomDatabase.kategoriDao().insertAllKategori(kategoriList);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseKategori> call, Throwable t) {

            }
        });


        btnChoosePict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_GALLERY);
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            // When an Image is picked
            if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && data != null) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);

                // Set the Image in ImageView for Previewing the Media
               ivPict.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                cursor.close();

            } else {
                Toast.makeText(this, "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }
}

