package com.gharibyan.razmik.instagramdataloader.repository.model.personal;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("profile_pic_url")
    private String profilePicUrlHD;

    @SerializedName("edge_followed_by")
    private EdgeFollowedBy edgeFollowedBy;

    @SerializedName("is_private")
    private Boolean isPrivate;

    @SerializedName("is_verified")
    private Boolean isVerified;

    public String getProfilePicUrlHD() {
        return profilePicUrlHD;
    }

    public EdgeFollowedBy getEdgeFollowedBy() {
        return edgeFollowedBy;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public Boolean getVerified() {
        return isVerified;
    }
}
