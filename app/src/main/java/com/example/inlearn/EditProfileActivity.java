package com.example.inlearn;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inlearn.api.ApiClient;
import com.example.inlearn.api.ApiService;
import com.example.inlearn.data.model.ResponseEditProfile;
import com.example.inlearn.utils.SharedPreferencedHelper;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    TextView tvUserName;
    TextView tvName;
    TextView tvEmail;
    ImageView ivProfile;
    SharedPreferencedHelper sharedPreferencedHelper;
    Button btnChoosePict;
    final static int  REQUEST_GALLERY = 1;
    String mediaPath;
    Button btnUpdate;
    String access_token;
    Context mContext;
    ApiService mApiService;
    File imageFile;
    Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        sharedPreferencedHelper = new SharedPreferencedHelper(this);
        mContext = this;
        mApiService = ApiClient.getService(getApplicationContext());


        tvEmail    = findViewById(R.id.et_email);
        tvUserName = findViewById(R.id.et_user_name);
        tvName     = findViewById(R.id.et_name);
        btnChoosePict = findViewById(R.id.btn_choose_pict);
        ivProfile = findViewById(R.id.iv_profile);
        btnUpdate = findViewById(R.id.btn_update);

        tvEmail.setText(sharedPreferencedHelper.getSpEmail());
        tvUserName.setText(sharedPreferencedHelper.getSpUserName());
        tvName.setText(sharedPreferencedHelper.getSpName());

        String id = sharedPreferencedHelper.getSpId();
        Log.e("TEST","image " + id);

        String spPict = sharedPreferencedHelper.getSpPict();
        String spPictUrl = "https://inlearnmobileapp.000webhostapp.com/storage/user/"+spPict;
        Picasso.get()
                .load(spPictUrl)
                .resize(200, 200)
                .into(ivProfile, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
//                        Toast.makeText(EditProfileActivity.this, "Image is loaded successfully", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Exception e) {
//                        Toast.makeText(EditProfileActivity.this, "Failed to load", Toast.LENGTH_LONG).show();
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

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ivProfile.buildDrawingCache();
//                Bitmap image = ivProfile.getDrawingCache();
//
//                Bundle extras = new Bundle();

                Intent i = new Intent(EditProfileActivity.this, FullScreenImageActivity.class);
//                extras.putParcelable("imagebitmap", image);
//                i.putExtras(extras);
                i.putExtra("activity","EditProfileActivity");
                startActivity(i);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = tvName.getText().toString();
                final String userName = tvUserName.getText().toString();
                final String id = sharedPreferencedHelper.getSpId();


                Log.e("ID"," " + id);

                if (mediaPath != null){
                    imageFile = new File(mediaPath);
                }
                else {
//                    imageFile = new File(sharedPreferencedHelper.getSpPictPathInternal());
                    imageFile = new File(sharedPreferencedHelper.getSpPict());
                    Log.e("TEST","image " + imageFile );
                }

                access_token = sharedPreferencedHelper.getSpToken();
                RequestBody requestBodyPict = RequestBody.create(MediaType.parse("image/*"),imageFile);
                RequestBody requestBodyId = RequestBody.create(okhttp3.MultipartBody.FORM, id);
                RequestBody requestBodyName = RequestBody.create(okhttp3.MultipartBody.FORM, name);
                RequestBody requestBodyUserName = RequestBody.create(okhttp3.MultipartBody.FORM, userName);
                MultipartBody.Part partImage = MultipartBody.Part.createFormData("imageUpload", imageFile.getName(),requestBodyPict);
                Call<ResponseEditProfile> editProfile = mApiService.editProfileRequest(partImage,requestBodyId,requestBodyUserName,requestBodyName);
                editProfile.enqueue(new Callback<ResponseEditProfile>() {
                    @Override
                    public void onResponse(Call<ResponseEditProfile> call, Response<ResponseEditProfile> response) {
                        String msg = response.body().getStatus();
                        if(msg.equals("true")){
                            Toast.makeText(EditProfileActivity.this,"Success Update Profile", Toast.LENGTH_LONG).show();

                            String pictUpdated = response.body().getPict();
                            String nameUpdated = response.body().getName();
                            String userNameUpdated = response.body().getUser_name();
                            sharedPreferencedHelper.saveSPString(sharedPreferencedHelper.SP_PICT,pictUpdated);
                            sharedPreferencedHelper.saveSPString(sharedPreferencedHelper.SP_NAME,nameUpdated);
                            sharedPreferencedHelper.saveSPString(sharedPreferencedHelper.SP_USERNAME,userNameUpdated);
                            String pictUri = "https://inlearnmobileapp.000webhostapp.com/storage/user/"+pictUpdated;
                            Picasso.get().load(pictUri).into(ivProfile);


                        }
                        else {
//                            Toast.makeText(EditProfileActivity.this,"Failed Update Profile", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseEditProfile> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
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
                sharedPreferencedHelper.saveSPString(sharedPreferencedHelper.SP_PICT_PATH_INTERNAL,mediaPath);
                // Set the Image in ImageView for Previewing the Media
                ivProfile.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                cursor.close();

            } else {
                Toast.makeText(this, "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
//            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }


}
