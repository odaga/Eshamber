package com.ugtechie.eshamber.api;

import com.ugtechie.eshamber.models.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {

    @GET("auth/users/{id}")
    Call<UserModel> getUser(@Path("id") String id);

    @POST("auth/signup")
    Call<UserModel> saveProfile(@Body UserModel userModel);

}
