package com.ugtechie.eshamber.ui;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ugtechie.eshamber.R;
import com.ugtechie.eshamber.api.UserService;
import com.ugtechie.eshamber.models.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private TextView textViewLogin;
    private TextView textViewPhoneNumber;
    private String userId;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //initializing Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        editTextFirstName = findViewById(R.id.edit_text_first_name);
        editTextLastName = findViewById(R.id.edit_text_last_name);
        textViewPhoneNumber = findViewById(R.id.edit_text_phone_number);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        textViewLogin = findViewById(R.id.text_view_login);
        buttonRegister = findViewById(R.id.button_register);

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Adds a new user to Firebase
                SignUpNewUser();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
    }

    private void SignUpNewUser() {
        //Sign up with firebase logic goes here
        //Extracting strings from the editText
        final String firstName = editTextFirstName.getText().toString().trim();
        final String lastName = editTextLastName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String phoneNumber = textViewPhoneNumber.getText().toString().trim();

        //Validating the user input
        if (firstName.isEmpty()) {
            editTextFirstName.setError("First name is required");
        } else if (lastName.isEmpty()) {

            editTextLastName.setError("Last name is required");
        } else if (phoneNumber.isEmpty()) {
            editTextPassword.setError("Valid Phone Number is required");
        } else if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
        } else if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
        } else if (password.length() < 8) {
            editTextPassword.setError("Password must be at least 8 characters");

        } else {
            //Initializing the progressDialog
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Registering user...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                          //  progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                //Sign in success
                                Log.d(TAG, "onComplete: createUSerWith Email successful");
                                FirebaseUser user = task.getResult().getUser();
                                userId = mAuth.getCurrentUser().getUid();

                                //Add profile data to Firestore users collection
                                //CreateUserProfile(firstName, lastName, email, userId);
                                //TODO add function to save user profile via Eshamber API
                                saveUserProfile(firstName, lastName, email, password, phoneNumber);
                                progressDialog.dismiss();
                                //Go to home activity
                                startHomeActivity(user);


                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, "Creating Account failed", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }

    private void saveUserProfile(String firstName, String lastName, String email, String password, String phoneNumber) {
        //TODO send a POST request to the /auth/signup route on the API

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://warm-bayou-20128.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserModel newUserModelProfile = new UserModel(
                firstName,
                lastName,
                phoneNumber,
                email,
                password,
                null
        );
        UserService userService = retrofit.create(UserService.class);
        Call<UserModel> call = userService.saveProfile(newUserModelProfile);

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Error saving user profile", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "profile Not save", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startHomeActivity (FirebaseUser user){
        if (user != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}