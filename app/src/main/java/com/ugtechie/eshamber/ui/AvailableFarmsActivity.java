package com.ugtechie.eshamber.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
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

public class AvailableFarmsActivity extends AppCompatActivity {
    private static final String TAG = "AvailableFarmsActivity";

    //Farm Id constant to be passed to farmDetailsActivity
    public static final String RECYCLERVIEW_SINGLE_ID = "com.ugtechie.eshamber.ui.farm.id.extra";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference submittedFarmRef = db.collection("Submitted Farms");
    // private AvailableFarmsAdapter adapter;
    private FarmsAdapter adapter;
    ;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar availableFarmProgress;
    private LinearLayout availableFarmsLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_farms);

        Toolbar mActionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Available Farms");
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AvailableFarmsActivity.this, AvailableFarmsActivity.class));
            }
        });

        availableFarmProgress = findViewById(R.id.available_farms_progress);
        availableFarmsLinearLayout = findViewById(R.id.available_farms_linear_layout);

        fetchFarmFromApi();
    }

    private void fetchFarmFromApi() {
        //Show Progress widget before farms load
        availableFarmProgress.setVisibility(View.VISIBLE);
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
                    Toast.makeText(AvailableFarmsActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Farm> availableFarmsList = response.body();
                buildRecyclerview(availableFarmsList);
            }

            @Override
            public void onFailure(Call<List<Farm>> call, Throwable t) {
                //Remove progress widget when the loading failed
                availableFarmProgress.setVisibility(View.INVISIBLE);

                Snackbar snackbar = Snackbar
                        .make(availableFarmsLinearLayout, "Connection Error", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                fetchFarmFromApi();
                            }
                        });

                snackbar.show();
            }
        });
    }

    private void buildRecyclerview(List<Farm> availableFarmsList) {
        RecyclerView recyclerView = findViewById(R.id.available_farms_recyclerview);
        recyclerView.setHasFixedSize(true);
        adapter = new FarmsAdapter(availableFarmsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        //Remove progress widget when the loading is done
        availableFarmProgress.setVisibility(View.INVISIBLE);
        adapter.setOnItemClickListener(new FarmsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                availableFarmsList.get(position);
                Intent intent = new Intent(AvailableFarmsActivity.this, FarmDetailsActivity.class);
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
