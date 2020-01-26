package com.gharibyan.razmik.instagramdataloader.repository.model.personal;

import com.google.gson.annotations.SerializedName;

public class ObjectResponse {

    @SerializedName("graphql")
    private Graphql graphql;

    public Graphql getGraphql() {
        return graphql;
    }
}
