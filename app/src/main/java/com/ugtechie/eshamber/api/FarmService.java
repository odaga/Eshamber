package com.ugtechie.eshamber.api;

import com.ugtechie.eshamber.models.Farm;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FarmService {

    @GET("farms")
    Call<List<Farm>> getAvailableFarms();
}
