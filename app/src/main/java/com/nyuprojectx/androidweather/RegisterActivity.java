package com.nyuprojectx.androidweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nyuprojectx.androidweather.user.GetUserCallback;
import com.nyuprojectx.androidweather.user.ServerRequests;
import com.nyuprojectx.androidweather.user.User;

public class RegisterActivity extends AppCompatActivity {
    EditText emailEditText, dateEditText, usernameEditText, passwordEditText, locationEditText;
    Button registerButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        dateEditText = (EditText) findViewById(R.id.dateEditText);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        locationEditText = (EditText) findViewById(R.id.locationEditText);
        registerButton = (Button) findViewById(R.id.registerButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = emailEditText.getText().toString();
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                int date = Integer.parseInt(dateEditText.getText().toString());
                String location = locationEditText.getText().toString();

                User user = new User(name, date, username, password, location);
                registerUser(user);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void registerUser(User user) {
        ServerRequests serverRequest = new ServerRequests(this);
        serverRequest.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                finish();
            }
        });
    }
}
