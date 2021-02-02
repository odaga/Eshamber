package com.ugtechie.eshamber.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.elyeproj.loaderviewlibrary.LoaderTextView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;
import com.ugtechie.eshamber.R;

import java.text.DecimalFormat;

public class FarmDetailsActivity extends AppCompatActivity {
    private static final String TAG = "FarmDetailsActivity";

    private Toolbar mActionBarToolbar;
    //private TextView textViewFarmTitle;
    private LoaderTextView textViewFarmDescription;
    private TextView textViewFarmDetailAmount;
    private TextView textViewFarmDetailROI;
    private Button cardViewDetailsInvestCard;


    private ImageView farmImage;
    private String farmImageUrl;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    //Calculator widgets
    private EditText calculatorInPut;
    private TextView calculatorOutput;
    private Button buttonCalculate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_details);


        mActionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(null);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FarmDetailsActivity.this, AvailableFarmsActivity.class));
            }
        });

        //Setting up widgets
        textViewFarmDescription = findViewById(R.id.detail_farm_description);
        DecimalFormat decim = new DecimalFormat("#,###.##"); //Adds thousands number format
        textViewFarmDetailAmount = findViewById(R.id.text_view_detail_amount);
        textViewFarmDetailROI = findViewById(R.id.text_view_detail_roi);
        cardViewDetailsInvestCard = findViewById(R.id.card_view_detail_invest);
        collapsingToolbarLayout = findViewById(R.id.farm_details_collapsing_tool_bar);
        textViewFarmDescription = findViewById(R.id.detail_farm_description);

        //Calculator widgets
        calculatorInPut = findViewById(R.id.calculator_input);
        calculatorOutput = findViewById(R.id.text_view_calculator_output);
        buttonCalculate = findViewById(R.id.button_calculate);

        cardViewDetailsInvestCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FarmDetailsActivity.this, "Investment added", Toast.LENGTH_SHORT).show();
                //TODO GO TO SELECT PAYMENT ACTIVITY TO INVEST IN THE FARM
            }
        });

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateReturns();
            }
        });

        fetchSingleFarmDetails();


    }

    private void fetchSingleFarmDetails() {
        collapsingToolbarLayout.setTitle(getIntent().getStringExtra("available_farm_card_title"));
        farmImage = findViewById(R.id.image_view_details_image);
        farmImageUrl = getIntent().getStringExtra("available_farm_card_image_url");
        textViewFarmDetailROI.setText(getIntent().getStringExtra("available_farm_card_roi") + "%");
        //textViewFarmDescription.setText(getIntent().getStringExtra("available_farm_card_description"));
        textViewFarmDetailAmount.setText(getIntent().getStringExtra("available_farm_card_amount"));
        try {
            Picasso.get().load(Uri.parse(farmImageUrl)).into(farmImage);
        } catch (Exception e) {
            Log.d(TAG, "fetchSingleFarmDetails: " + e.getMessage());
            farmImage.setImageResource(R.drawable.farm_image_placeholder);
        }
    }

    public void calculateReturns() {
        Float result;
        Float input = Float.parseFloat(calculatorInPut.getText().toString().trim());
        result = (input * 20) + input;
        calculatorOutput.setText(result.toString());
    }
}
