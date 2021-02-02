package com.ugtechie.eshamber.api;

import com.ugtechie.eshamber.models.Farm;
import com.ugtechie.eshamber.models.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FarmService {

    @GET("farms")
    Call<List<Farm>> getAvailableFarms();

    @GET("farm/investor/{investorId}")
    Call<List<Farm>> getMyInvestment(@Path("investorId") String investorId);

    @GET("auth/users/{id}")
    Call<UserModel> getUser(@Path("id") String id);
    
}
