package com.nyuprojectx.androidweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nyuprojectx.androidweather.user.GetUserCallback;
import com.nyuprojectx.androidweather.service.ServerRequests;
import com.nyuprojectx.androidweather.user.User;

public class RegisterActivity extends AppCompatActivity {
    EditText emailEditText, unameEditText, passwdEditText, bioEditText;
    Button registerButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        unameEditText = (EditText) findViewById(R.id.unameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwdEditText = (EditText) findViewById(R.id.passwdEditText);
        bioEditText = (EditText) findViewById(R.id.bioEditText);
        registerButton = (Button) findViewById(R.id.registerButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = unameEditText.getText().toString();
                String email= emailEditText.getText().toString();
                String passwd = passwdEditText.getText().toString();
                String bio = bioEditText.getText().toString();
                Toast.makeText(getBaseContext(), uname + email + passwd + bio, Toast.LENGTH_SHORT).show();
                User user = new User(-1, uname, email, passwd, bio);
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
