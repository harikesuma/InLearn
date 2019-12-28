package com.example.inlearn.ui.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.inlearn.EditProfileActivity;
import com.example.inlearn.FullScreenImageActivity;
import com.example.inlearn.LoginActivity;
import com.example.inlearn.R;
import com.example.inlearn.api.ApiClient;
import com.example.inlearn.api.ApiService;
import com.example.inlearn.utils.SharedPreferencedHelper;
import com.squareup.picasso.Picasso;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    Button btnLogOut;
    ImageButton ibEdit;
    Context mContext;
    ApiService mApiService;
    TextView tvName;
    TextView tvEmail;
    ImageView ivProfile;
    SharedPreferencedHelper sharedPreferencedHelper;
    private ProfileViewModel profileViewModel;
    ProgressDialog progressDialog;
    String fcm;
    String id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mContext = getContext();
        mApiService = ApiClient.getService(getContext());
        sharedPreferencedHelper = new SharedPreferencedHelper(getContext());

        fcm = sharedPreferencedHelper.getFcmToken();
        id = sharedPreferencedHelper.getSpId();

        ivProfile = root.findViewById(R.id.iv_profile);
        tvName = root.findViewById(R.id.tv_name);
        tvEmail = root.findViewById(R.id.tv_email);

        tvName.setText(sharedPreferencedHelper.getSpName());
        tvEmail.setText(sharedPreferencedHelper.getSpEmail());

        String spPict = sharedPreferencedHelper.getSpPict();
        String spPictUrl = "https://inlearnmobileapp.000webhostapp.com/storage/user/"+spPict;
        Picasso.get()
                .load(spPictUrl)
                .resize(50,50)
                .into(ivProfile);


        btnLogOut = root.findViewById(R.id.btn_log_out);
        ibEdit = root.findViewById(R.id.ib_edit);


        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivProfile.buildDrawingCache();
                Bitmap image = ivProfile.getDrawingCache();

                Bundle extras = new Bundle();

                Intent i = new Intent(getContext(), FullScreenImageActivity.class);
                String spPict = sharedPreferencedHelper.getSpPict();
                Log.e("SP PICT 1"," " + spPict);
                i.putExtra("activity","EditProfileActivity");
//                extras.putParcelable("imagebitmap", image);
////                i.putExtras(extras);
                startActivity(i);
            }

        });


        ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), EditProfileActivity.class);
                startActivity(i);
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    deleteToken(fcm,id);
                    Thread.sleep(100);
                }
                catch(InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }

                logOut();

            }
        });

        return root;
    }

    public void logOut(){
        sharedPreferencedHelper.saveSPBoolean(SharedPreferencedHelper.SP_SUDAH_LOGIN, false);
        sharedPreferencedHelper.deleteSharedPreferenced();
        startActivity(new Intent(getContext(), LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));

        getActivity().finish();

    }

    public void deleteToken(String fcm,String id){
        mApiService.deleteToken(fcm,id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Log.e("DEBUG", "DELETED");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("DEBUG", "Error " +t.getMessage());
            }
        });
    }
}