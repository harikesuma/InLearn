package com.example.inlearn;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.inlearn.ui.profile.ProfileFragment;
import com.example.inlearn.utils.SharedPreferencedHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    SharedPreferencedHelper sharedPreferencedHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencedHelper = new SharedPreferencedHelper(this);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        if (EasyPermissions.hasPermissions(this, galleryPermissions)) {

        } else {
            EasyPermissions.requestPermissions(this, "Access for storage",
                    101, galleryPermissions);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        final MenuItem icon = menu.findItem(R.id.icon_profile);
        Picasso.get()
                .load("https://inlearnmobileapp.000webhostapp.com/storage/user/"+sharedPreferencedHelper.getSpPict())
                .into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Drawable d = new BitmapDrawable(getResources(), bitmap);
                icon.setIcon(d);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.e("DEBUG","FAILED");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.e("DEBUG","MASUK");
            }
        });
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== R.id.icon_profile){
            Intent i = new Intent(this,EditProfileActivity.class);
            startActivity(i);
        }
        else {
            Intent i = new Intent(this, NotificationActivity.class);
            startActivity(i);
        }
        return true;
    }


}
