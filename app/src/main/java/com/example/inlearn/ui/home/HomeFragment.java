package com.example.inlearn.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.inlearn.R;
import com.example.inlearn.adapter.HomeAdapter;
import com.example.inlearn.adapter.TopOfUserAdapter;
import com.example.inlearn.api.ApiClient;
import com.example.inlearn.api.ApiService;
import com.example.inlearn.data.model.Question;
import com.example.inlearn.data.model.ResponseQuestion;
import com.example.inlearn.data.model.ResponseTopOfUser;
import com.example.inlearn.data.model.ResponseUser;
import com.example.inlearn.data.model.TopOfUser;
import com.example.inlearn.data.model.User;
import com.example.inlearn.data.room.RoomDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    RecyclerView vRecyclerView;
    RecyclerView hRecyclerView;
    HomeAdapter homeAdapter;
    TopOfUserAdapter topOfUserAdapter;
    private List<Question> questionList = new ArrayList<>();
    private List<TopOfUser> topOfUserList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();
    Context mContext;
    ApiService mApiService;
    ProgressDialog progressDialog;
    public static RoomDatabase roomDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mContext = getContext();
        mApiService = ApiClient.getService(getContext());


        hRecyclerView = view.findViewById(R.id.horizontal_rec_top_of_user);
//      addData2();
        topOfUserAdapter = new TopOfUserAdapter(topOfUserList);
        RecyclerView.LayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        hRecyclerView.setHasFixedSize(true);
        hRecyclerView.setLayoutManager(horizontalLayoutManager);
        hRecyclerView.setAdapter(topOfUserAdapter);


        vRecyclerView = view.findViewById(R.id.rec_home);
//      addData();
        homeAdapter = new HomeAdapter(getContext(), questionList);
        RecyclerView.LayoutManager verticalLayoutManager = new LinearLayoutManager(getContext());
        hRecyclerView.setHasFixedSize(true);
        vRecyclerView.setLayoutManager(verticalLayoutManager);
        vRecyclerView.setAdapter(homeAdapter);
        vRecyclerView.setItemAnimator(new DefaultItemAnimator());

        hRecyclerView.setNestedScrollingEnabled(false);
        vRecyclerView.setNestedScrollingEnabled(true);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching Data");
        progressDialog.show();

        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                progressDialog.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 2000);

        if (this.isNetworkAvailable()) {

            mApiService.getQuestionList().enqueue(new Callback<ResponseQuestion>() {
                @Override
                public void onResponse(Call<ResponseQuestion> call, Response<ResponseQuestion> response) {
                    if (response.isSuccessful()) {
                        questionList = response.body().getQuestionList();
//                        Toast.makeText(getContext(), "LOAD SUCCESS", Toast.LENGTH_LONG).show();
                        vRecyclerView.setAdapter(new HomeAdapter(mContext, questionList));
                        homeAdapter.notifyDataSetChanged();

                        roomDatabase = Room.databaseBuilder(mContext,
                                RoomDatabase.class, "db_praktikum_progmob").allowMainThreadQueries().build();
                        Question question = new Question();
                        for (int i = 0; i < questionList.size(); i++) {
                            question.setId(questionList.get(i).getId());
                            question.setUser_name(questionList.get(i).getUser_name());
                            question.setKategori(questionList.get(i).getKategori());
                            question.setPertanyaan(questionList.get(i).getPertanyaan());
                            question.setCreated_at(questionList.get(i).getCreated_at());
                            question.setEdited(questionList.get(i).getEdited());
                            roomDatabase.questionDao().insertAllQuestion(questionList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseQuestion> call, Throwable t) {
//                    Toast.makeText(getContext(), "LOAD Failed", Toast.LENGTH_LONG).show();
                }
            });


            mApiService.getTopOfuser().enqueue(new Callback<ResponseTopOfUser>() {
                @Override
                public void onResponse(Call<ResponseTopOfUser> call, Response<ResponseTopOfUser> response) {
                    if (response.isSuccessful()) {
//                        Toast.makeText(mContext, "Success Load Top of User", Toast.LENGTH_LONG);
                        roomDatabase = Room.databaseBuilder(mContext,
                                RoomDatabase.class, "db_praktikum_progmob").allowMainThreadQueries().build();
                        roomDatabase.topOfUserDao().deleteAll();

                        topOfUserList = response.body().getTopOfUserList();
                        Log.e("DEBUG", "" + topOfUserList);
                        hRecyclerView.setAdapter(new TopOfUserAdapter(topOfUserList));
                        topOfUserAdapter.notifyDataSetChanged();



                        TopOfUser topOfUser = new TopOfUser();
                        for (int i = 0; i < topOfUserList.size(); i++) {
                            topOfUser.setUser_name(topOfUserList.get(i).getUser_name());
                            topOfUser.setLike(topOfUserList.get(i).getLike());
                            roomDatabase.topOfUserDao().insertTopOfUser(topOfUserList);
                        }


                    } else {
                        Log.e("DEBUG", "FAILED" + topOfUserList);
                    }
                }

                @Override
                public void onFailure(Call<ResponseTopOfUser> call, Throwable t) {
                    Log.e("DEBUG", "FAILED" + t.getMessage());
                }
            });

            mApiService.getAllUser().enqueue(new Callback<ResponseUser>() {
                @Override
                public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                    if (response.isSuccessful()) {

                        roomDatabase = Room.databaseBuilder(mContext,
                                RoomDatabase.class, "db_praktikum_progmob").allowMainThreadQueries().build();
                        userList = response.body().getUserList();
                        User user = new User();
                        for (int i = 0; i < userList.size(); i++) {
                            user.setId(userList.get(i).getId());
                            user.setName(userList.get(i).getName());
                            user.setUser_name(userList.get(i).getUser_name());
                            user.setEmail(userList.get(i).getEmail());
                            roomDatabase.userDao().insertAllUser(userList);
                        }

                    } else {
//                        Toast.makeText(mContext, "Failed Load User List", Toast.LENGTH_LONG);
                    }
                }

                @Override
                public void onFailure(Call<ResponseUser> call, Throwable t) {

                }
            });
        }

        else {
            loadFromLocal();
        }



        return view;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void loadFromLocal(){
        roomDatabase = Room.databaseBuilder(mContext,
                RoomDatabase.class, "db_praktikum_progmob").allowMainThreadQueries().build();

        questionList = roomDatabase.questionDao().getAllQuestion();
        vRecyclerView.setAdapter(new HomeAdapter(mContext, questionList));

        topOfUserList = roomDatabase.topOfUserDao().getAllTopOfUser();
        hRecyclerView.setAdapter(new TopOfUserAdapter(topOfUserList));

    }

}
