package com.ugtechie.eshamber.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.ugtechie.eshamber.R;
import com.ugtechie.eshamber.api.UserService;
import com.ugtechie.eshamber.models.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private String userId = "5f8855dc76daf7a6101a1d1c";

    private Button buttonAvailableFarmsButton;
    private CardView cardViewActiveInvestments;
    private CardView cardViewFarmUpdates;
    private TextView userName;
    private ConstraintLayout homeRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        buttonAvailableFarmsButton = findViewById(R.id.button_available_farms_activity);
        cardViewActiveInvestments = findViewById(R.id.ard_active_investment);
        cardViewFarmUpdates = findViewById(R.id.card_Farm_updates);
        userName = findViewById(R.id.name_user);
        homeRootLayout = findViewById(R.id.home_root_layout);

        buttonAvailableFarmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, AvailableFarmsActivity.class);
                startActivity(i);
            }
        });

        cardViewActiveInvestments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ActiveInvestmentsActivity.class);
                startActivity(i);
            }
        });

        cardViewFarmUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ViewFarmUpdatesActivity.class);
                startActivity(i);
            }
        });

      //  fetchUserProfile();
    }

    private void fetchUserProfile() {
        //====================Set up Retrofit =================================//
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://warm-bayou-20128.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService userService = retrofit.create(UserService.class);
        Call<UserModel> call = userService.getUser(userId);

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(HomeActivity.this,  "Failed with status" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                //Update the UI with user data
                userName.setText("Hello, "+response.body().getFirstName());
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d(TAG, "onFailure: Could not fetch user data");
                Toast.makeText(HomeActivity.this, "Could not fetch user profile data", Toast.LENGTH_SHORT).show();

                Snackbar snackbar = Snackbar
                        .make(homeRootLayout, "Connection Error", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Run action to retry
                            }
                        });

                snackbar.show();
            }
        });
    }
}
