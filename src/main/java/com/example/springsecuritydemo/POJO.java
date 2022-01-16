package com.example.springsecuritydemo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class POJO {

    @JsonProperty(value = "user_id")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "POJO{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
