package com.nyuprojectx.androidweather;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nyuprojectx.androidweather.user.GetUserCallback;
import com.nyuprojectx.androidweather.service.ServerRequests;
import com.nyuprojectx.androidweather.user.User;
import com.nyuprojectx.androidweather.user.UserLocalStore;

public class LoginActivity extends AppCompatActivity {

    Button loginButton, cancelButton, registerButton;
    EditText unameEditText, passwdEditText;
    TextView loginInfoTextView;

    UserLocalStore userLocalStore;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        unameEditText = (EditText) findViewById(R.id.unameEditText);
        passwdEditText = (EditText) findViewById(R.id.passwdEditText);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);
        loginInfoTextView = (TextView)findViewById(R.id.loginInfoTextView);

        userLocalStore = new UserLocalStore(this);

        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, UserInfoActivity.class);
            startActivity(intent);
            finish();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = unameEditText.getText().toString();
                String passwd = passwdEditText.getText().toString();

                User user = new User(uname, passwd);

                authenticate(user);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userLocalStore = new UserLocalStore(this);

    }

    private void authenticate(User user) {
        ServerRequests serverRequest = new ServerRequests(this);
        serverRequest.fetchUserDataAsyncTask(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage();
                } else {
                    logUserIn(returnedUser);
                }
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        dialogBuilder.setMessage("Incorrect user details");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    private void logUserIn(User returnedUser) {
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);
        // startActivity(new Intent(this, MainActivity.class));
        // change here

        StringBuffer sb = new StringBuffer("Welcome ");
        sb.append(returnedUser.uname);
        loginInfoTextView.setText(sb);
        Toast.makeText(getBaseContext(), "Login successfully.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, UserInfoActivity.class);
        startActivity(intent);
    }
}
