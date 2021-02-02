package com.ugtechie.eshamber.ui;

import android.content.Intent;
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

public class ViewFarmUpdatesActivity extends AppCompatActivity {
    private static final String TAG = "ViewFarmUpdatesActivity";

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private FarmsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_farm_updates);

        Toolbar mActionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Farm Updates");

        //Setting up widgets
        recyclerView = findViewById(R.id.updates_recyclerview);
        progressBar = findViewById(R.id.updates_progressbar);
        // progressBar.setVisibility(View.INVISIBLE);

        fetchFarmFromApi();
    }

    private void fetchFarmFromApi() {
        //Show Progress widget before farms load
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
                    Toast.makeText(ViewFarmUpdatesActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Farm> availableFarmsList = response.body();
                buildRecyclerview(availableFarmsList);
            }

            @Override
            public void onFailure(Call<List<Farm>> call, Throwable t) {
                //Remove progress widget when the loading failed
                progressBar.setVisibility(View.INVISIBLE);


            }
        });
    }

    private void buildRecyclerview(List<Farm> availableFarmsList) {
        recyclerView.setHasFixedSize(true);
        adapter = new FarmsAdapter(availableFarmsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        //Remove progress widget when the loading is done
        progressBar.setVisibility(View.INVISIBLE);
        adapter.setOnItemClickListener(new FarmsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                availableFarmsList.get(position);
                Intent intent = new Intent(ViewFarmUpdatesActivity.this, FarmDetailsActivity.class);
                intent.putExtra("available_farm_card_id", availableFarmsList.get(position).get_id());
                intent.putExtra("available_farm_card_title", availableFarmsList.get(position).getFarmName());
                intent.putExtra("available_farm_card_image_url", availableFarmsList.get(position).getFarmImageUrl());
                intent.putExtra("available_farm_card_roi", availableFarmsList.get(position).getFarmROI());
                intent.putExtra("available_farm_card_amount", availableFarmsList.get(position).getFarmAmount());
                intent.putExtra("available_farm_card_description", availableFarmsList.get(position).getFarmDescription());
                //Toast.makeText(AvailableFarmsActivity.this, availableFarmsList.get(position).getFarmImageUri(), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}

