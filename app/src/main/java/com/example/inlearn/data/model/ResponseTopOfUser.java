package com.example.inlearn.data.model;

import java.util.List;

public class ResponseTopOfUser {
    List<TopOfUser> topOfUserList;

    public List<TopOfUser> getTopOfUserList() {
        return topOfUserList;
    }

    public void setTopOfUserList(List<TopOfUser> topOfUserList) {
        this.topOfUserList = topOfUserList;
    }

    @Override
    public String toString(){
        return
                "ResponseTopOfUser{" +
                        "topOfUserList = '" + topOfUserList + '\'' +
                        "}";
    }
}
