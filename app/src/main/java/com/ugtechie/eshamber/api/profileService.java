package com.ugtechie.eshamber.api;

import com.ugtechie.eshamber.models.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface profileService {
   @POST("auth/signup")
   Call<UserModel> saveProfile(@Body UserModel userModel);
}
