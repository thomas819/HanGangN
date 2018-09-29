package com.example.thomas.hangangn.model;

import com.example.thomas.hangangn.domain.ApiDomain;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HangangService {

    @GET("75444b447674686f38357a784a7769/json/{apiKind}/1/5/")
    Call<ApiDomain> test(@Path("apiKind")String kind);
}
