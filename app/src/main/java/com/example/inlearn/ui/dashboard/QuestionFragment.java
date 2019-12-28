package com.example.inlearn.ui.dashboard;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.inlearn.R;
import com.example.inlearn.adapter.CommentAdapter;
import com.example.inlearn.adapter.HomeAdapter;
import com.example.inlearn.api.ApiClient;
import com.example.inlearn.api.ApiService;
import com.example.inlearn.data.model.Comment;
import com.example.inlearn.data.model.Question;
import com.example.inlearn.data.model.ResponseQuestion;
import com.example.inlearn.utils.SharedPreferencedHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionFragment extends Fragment {
    SharedPreferencedHelper sharedPreferencedHelper;
    ApiService mApiService;
    Context mContext;
    RecyclerView recyclerView;
    HomeAdapter homeAdapter;
    private List<Question> questionList = new ArrayList<>();


    private QuestionViewModel mViewModel;

    public static QuestionFragment newInstance() {
        return new QuestionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.question_fragment, container, false);

        mContext = getContext();
        mApiService = ApiClient.getService(mContext);
        sharedPreferencedHelper = new SharedPreferencedHelper(mContext);


        recyclerView= view.findViewById(R.id.rv_question);
//      addData();
        homeAdapter = new HomeAdapter(mContext,questionList);
        RecyclerView.LayoutManager verticalLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(homeAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        String id = sharedPreferencedHelper.getSpId();
//        Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();

        mApiService.getUserQuestionHistory(id).enqueue(new Callback<ResponseQuestion>() {
            @Override
            public void onResponse(Call<ResponseQuestion> call, Response<ResponseQuestion> response) {
                if (response.isSuccessful()){
//                    Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    questionList = response.body().getQuestionList();
                    recyclerView.setAdapter(new HomeAdapter(mContext,questionList));
                    homeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseQuestion> call, Throwable t) {

            }
        });

        return view;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(QuestionViewModel.class);
        // TODO: Use the ViewModel
    }

}
