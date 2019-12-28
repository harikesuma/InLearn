package com.example.inlearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.inlearn.utils.SharedPreferencedHelper;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class FullScreenImageActivity extends AppCompatActivity {
    SharedPreferencedHelper sharedPreferencedHelper;
    String spPictUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        PhotoView photoView = (PhotoView) findViewById(R.id.pv_full_screen);
        Intent i = getIntent();
        String activityFrom = i.getExtras().getString("activity");

//        Toast.makeText(this, activityFrom, Toast.LENGTH_SHORT).show();

        if (activityFrom.equals("CommentActivity")){
           spPictUrl = i.getExtras().getString("path");
        }

        else {
            sharedPreferencedHelper = new SharedPreferencedHelper(this);
//        Intent i = getIntent();
//        Bitmap bmp = (Bitmap) i.getParcelableExtra("imagebitmap");
//        photoView.setImageBitmap(bmp);

            String spPict = sharedPreferencedHelper.getSpPict();
            Log.e("SP PICT", " " + spPict);
            spPictUrl = "https://inlearnmobileapp.000webhostapp.com/storage/user/"+spPict;
        }



        Picasso.get()
                .load(spPictUrl)
                .resize(50,50)
                .into(photoView);

    }
}
