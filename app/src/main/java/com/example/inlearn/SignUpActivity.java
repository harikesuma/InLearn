package com.example.inlearn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.inlearn.api.ApiClient;
import com.example.inlearn.api.ApiService;
import com.example.inlearn.utils.SharedPreferencedHelper;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    Button btnSignUp;
    Button btnLogIn;
    TextInputEditText etName;
    TextInputEditText etUserName;
    TextInputEditText etEmail;
    TextInputEditText etPassword;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    String name;
    String userName;
    String email;
    String password;
    String jenisKelamin = "not selected";
    int valOfJenisKelamin;
    Boolean isError;
    Context mContext;
    ApiService mApiService;
    SharedPreferencedHelper sharedPreferencedHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sharedPreferencedHelper = new SharedPreferencedHelper(this);

        mContext = this;
        mApiService = ApiClient.getService(getApplicationContext());

        etName = findViewById(R.id.et_name);
        etUserName = findViewById(R.id.et_user_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        radioGroup = findViewById(R.id.radio);

        btnLogIn = findViewById(R.id.btn_log_in);
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        btnSignUp = findViewById(R.id.btn_sign_up);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etName.getText().toString();
                userName = etUserName.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                valOfJenisKelamin = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(valOfJenisKelamin);
                jenisKelamin = radioButton.getText().toString();
                if (jenisKelamin.equals("Male")) {
                    jenisKelamin = "M";
                } else {
                    jenisKelamin = "F";
                }

                Log.d("debug", "onFailure: ERROR > " + name + "," + userName + "," + email +  ","  + password + "," + jenisKelamin);


                isError = false;

                if (name.equals("")) {
                    etName.setError("Name is required.");
                    isError = true;
                } else if (userName.equals("")) {
                    etUserName.setError("User Name is required.");
                    isError = true;
                } else if (email.equals("")) {
                    etEmail.setError("Email is required.");
                    isError = true;
                } else if (password.equals("")) {
                    etPassword.setError("Password is required.");
                    isError = true;
                } else if (radioGroup.getCheckedRadioButtonId()==0) {
                    radioButton.setError("Please select gender");
                    isError = true;

                } else if (isError) {
                    Toast.makeText(SignUpActivity.this, "Failed Authetication", Toast.LENGTH_LONG).show();

                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
                    progressDialog.setMessage("Logging in.. Please wait!");
                    progressDialog.show();
                    progressDialog.setMessage("Authenticating...");

                    mApiService.registerRequest(name,userName,email,password,jenisKelamin)
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    progressDialog.dismiss();
                                    progressDialog.hide();
                                    if (response.isSuccessful()) {
                                        try {
                                            JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                            if (jsonRESULTS.getString("status").equals("true")) {

                                                String userName = jsonRESULTS.getString("user_name");
                                                String name = jsonRESULTS.getString("name");
                                                String email = jsonRESULTS.getString("email");
                                                String token = jsonRESULTS.getString("token");
                                                String id = jsonRESULTS.getString("id");

                                                sharedPreferencedHelper.saveSPString(sharedPreferencedHelper.SP_USERNAME, userName);
                                                sharedPreferencedHelper.saveSPString(sharedPreferencedHelper.SP_TOKEN, token);
                                                sharedPreferencedHelper.saveSPString(sharedPreferencedHelper.SP_NAME, name);
                                                sharedPreferencedHelper.saveSPString(sharedPreferencedHelper.SP_EMAIL, email);
                                                sharedPreferencedHelper.saveSPBoolean(sharedPreferencedHelper.SP_SUDAH_LOGIN, true);
                                                sharedPreferencedHelper.saveSPString(sharedPreferencedHelper.SP_ID, id);

                                                Toast.makeText(mContext, "Register Sucsess", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(mContext, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                // Jika login gagal
                                                String error_message = jsonRESULTS.getString("status");
                                                Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
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

    }
}

