package com.gharibyan.razmik.instagramdataloader.repository.model.personal;

import com.google.gson.annotations.SerializedName;

public class Graphql {

    @SerializedName("user")
    private User user;

    public User getUser() {
        return user;
    }
}
