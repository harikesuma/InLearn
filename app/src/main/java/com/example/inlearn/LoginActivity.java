package com.example.inlearn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inlearn.api.ApiClient;
import com.example.inlearn.api.ApiService;
import com.example.inlearn.utils.SharedPreferencedHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import me.anwarshahriar.calligrapher.Calligrapher;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText etUserName;
    TextInputEditText etPassword;
    Button btnLogIn;
    Button btnSignUp;
    Context mContext;
    ApiService mApiService;
    ProgressDialog progressDialog;
    SharedPreferencedHelper sharedPreferencedHelper;
    boolean isError = false;
    JSONObject jsonRESULTS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        mApiService = ApiClient.getService(getApplicationContext()); // meng-init yang ada di package apihelper


        sharedPreferencedHelper = new SharedPreferencedHelper(this);

        etUserName = findViewById(R.id.tv_user_name);
        etPassword = findViewById(R.id.tv_password);
        btnLogIn = findViewById(R.id.btn_log_in);
        btnSignUp = findViewById(R.id.btn_sign_up);

        if (sharedPreferencedHelper.getSPSudahLogin()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));

            finish();
        }


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });


        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etUserName.getText().toString().equals("")) {
                    etUserName.setError("Username is required.");
                    Toast.makeText(LoginActivity.this, "Failed Authentication", Toast.LENGTH_LONG).show();
                    isError = true;
                } else if (etPassword.getText().toString().equals("")) {
                    etPassword.setError("password is required.");
                    isError = true;
                }

                else {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Logging in.. Please wait!");
                progressDialog.show();


                mApiService.loginRequest(etUserName.getText().toString(), etPassword.getText().toString())
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                progressDialog.dismiss();
                                progressDialog.hide();
                                if (response.isSuccessful()) {
                                    try {
                                        jsonRESULTS = new JSONObject(response.body().string());
                                        if (jsonRESULTS.getString("status").equals("true")) {

                                            String userName = jsonRESULTS.getString("user_name");
                                            String name = jsonRESULTS.getString("name");
                                            String email = jsonRESULTS.getString("email");
                                            String token = jsonRESULTS.getString("token");
                                            String id = jsonRESULTS.getString("id");
                                            String pict = jsonRESULTS.getString("pict");
                                            String fcmToken = FirebaseInstanceId.getInstance().getToken();
                                            Log.e("TOKEN", fcmToken);


                                            sharedPreferencedHelper.saveSPString(sharedPreferencedHelper.SP_USERNAME, userName);
                                            sharedPreferencedHelper.saveSPString(sharedPreferencedHelper.SP_TOKEN, token);
                                            sharedPreferencedHelper.saveSPString(sharedPreferencedHelper.SP_NAME, name);
                                            sharedPreferencedHelper.saveSPString(sharedPreferencedHelper.SP_EMAIL, email);
                                            sharedPreferencedHelper.saveSPString(sharedPreferencedHelper.SP_ID,id);
                                            sharedPreferencedHelper.saveSPString(sharedPreferencedHelper.SP_PICT,pict);
                                            sharedPreferencedHelper.saveSPBoolean(SharedPreferencedHelper.SP_SUDAH_LOGIN, true);
                                            sharedPreferencedHelper.saveSPString(sharedPreferencedHelper.FCM_TOKEN, fcmToken);
                                            Toast.makeText(mContext, "Login Success", Toast.LENGTH_SHORT).show();

                                            loginSuccess(fcmToken, id);
                                            Intent intent = new Intent(mContext, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(mContext, "Unauthorised", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("debug", "onFailure: ERROR > " + t.toString());
                            }
                        });
            }


        }

        });


        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Roboto-Regular.ttf", true);
    }

    public void loginSuccess(String fcm, String id){
        mApiService.insertToken(fcm,id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Log.e("DEBUG", "Berhasil Insert");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }
}
