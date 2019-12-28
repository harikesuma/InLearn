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
import com.example.inlearn.data.model.ResponseComment;
import com.example.inlearn.data.model.ResponseQuestion;
import com.example.inlearn.utils.SharedPreferencedHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnswerFragment extends Fragment {
    SharedPreferencedHelper sharedPreferencedHelper;
    ApiService mApiService;
    Context mContext;
    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    private List<Comment> commentList = new ArrayList<>();

    private AnswerViewModel mViewModel;

    public static AnswerFragment newInstance() {
        return new AnswerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.answer_fragment, container, false);
        mContext = getContext();
        mApiService = ApiClient.getService(mContext);
        sharedPreferencedHelper = new SharedPreferencedHelper(mContext);


        recyclerView= view.findViewById(R.id.rv_comment);
//      addData();
        commentAdapter = new CommentAdapter(mContext,commentList);
        RecyclerView.LayoutManager verticalLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(commentAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        String id = sharedPreferencedHelper.getSpId();
//        Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();

        mApiService.getUserCommentHistory(id).enqueue(new Callback<ResponseComment>() {
            @Override
            public void onResponse(Call<ResponseComment> call, Response<ResponseComment> response) {
                if (response.isSuccessful()){
//                    Toast.makeText(getContext(), "Answer "+response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    commentList = response.body().getCommentList();
                    recyclerView.setAdapter(new CommentAdapter(mContext,commentList));
                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseComment> call, Throwable t) {

            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AnswerViewModel.class);
        // TODO: Use the ViewModel
    }

}
