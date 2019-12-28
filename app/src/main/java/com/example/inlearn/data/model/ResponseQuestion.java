package com.example.inlearn.data.model;

import java.util.List;

public class ResponseQuestion {
    List<Question> pertanyaanList;
    String msg;

    public List<Question> getQuestionList() {
        return pertanyaanList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.pertanyaanList = questionList;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString(){
        return
                "ResponsePertanyaan{" +
                        "pertanyaanList = '" + pertanyaanList + '\'' +
                        ",msg = '" + msg + '\'' +
                        "}";
    }
}
