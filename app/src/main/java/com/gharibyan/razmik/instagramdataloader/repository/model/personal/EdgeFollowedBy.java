package com.gharibyan.razmik.instagramdataloader.repository.model.personal;

import com.google.gson.annotations.SerializedName;

public class EdgeFollowedBy {

    @SerializedName("count")
    private Long followersCount;

    public Long getFollowersCount() {
        return followersCount;
    }
}
