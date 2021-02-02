package com.ugtechie.eshamber.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ugtechie.eshamber.R;
import com.ugtechie.eshamber.adapters.FarmsAdapter;
import com.ugtechie.eshamber.api.FarmService;
import com.ugtechie.eshamber.models.Farm;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActiveInvestmentsActivity extends AppCompatActivity {
    private static final String TAG = "ActiveInvestmentsActivity";

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private FarmsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_investments);

        //Setting up toolbar
        Toolbar mActionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Active Investments");

        //Setting up widgets
        recyclerView = findViewById(R.id.active_investment_recyclerview);
        progressBar = findViewById(R.id.active_investment_progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        getActiveInvestment();
    }

    private void getActiveInvestment() {


        progressBar.setVisibility(View.VISIBLE);


        //========================= Setting up Retrofit ==========================//
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://warm-bayou-20128.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FarmService farmService = retrofit.create(FarmService.class);

        Call<List<Farm>> call = farmService.getAvailableFarms();

        call.enqueue(new Callback<List<Farm>>() {
            @Override
            public void onResponse(Call<List<Farm>> call, Response<List<Farm>> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(ActiveInvestmentsActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Farm> availableFarmsList = response.body();
                buildRecyclerview(availableFarmsList);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<Farm>> call, Throwable t) {
                //Remove progress widget when the loading failed
                progressBar.setVisibility(View.INVISIBLE);
            }
        });




        /*
        //========================= Setting up Retrofit ==========================//
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://warm-bayou-20128.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FarmService farmService = retrofit.create(FarmService.class);
        Call<List<Farm>> call = farmService.getMyInvestment(FirebaseAuth.getInstance().getCurrentUser().getUid());

        call.enqueue(new Callback<List<Farm>>() {
            @Override
            public void onResponse(Call<List<Farm>> call, Response<List<Farm>> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (!response.isSuccessful()) {
                    Toast.makeText(ActiveInvestmentsActivity.this, " Failed with code: "+response.code(), Toast.LENGTH_SHORT).show();
                }

                if(response.code() == 404)
                    Toast.makeText(ActiveInvestmentsActivity.this, "No investment found", Toast.LENGTH_SHORT).show();

                List<Farm> availableFarmsList = response.body();
                buildRecyclerview(availableFarmsList);
            }

            @Override
            public void onFailure(Call<List<Farm>> call, Throwable t) {
                //Remove progress widget when the loading failed
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ActiveInvestmentsActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });

        */

    }

    private void buildRecyclerview(List<Farm> availableFarmsList) {
        recyclerView.setHasFixedSize(true);
        adapter = new FarmsAdapter(availableFarmsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }
}
