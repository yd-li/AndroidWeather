package com.nyuprojectx.androidweather;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nyuprojectx.androidweather.user.User;
import com.nyuprojectx.androidweather.user.UserLocalStore;

public class UserInfoActivity extends AppCompatActivity {

    TextView info1TextView, info2TextView, info3TextView;
    Button logOutButton, cancelButton;

    UserLocalStore userLocalStore;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        info1TextView = (TextView)findViewById(R.id.infoTextView1);
        info2TextView = (TextView)findViewById(R.id.infoTextView2);
        info3TextView = (TextView)findViewById(R.id.infoTextView3);

        logOutButton = (Button)findViewById(R.id.logOutButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);
        userLocalStore = new UserLocalStore(this);

        user = userLocalStore.getLoggedInUser();
        if (userLocalStore.getLoggedInUser() == null) {
            Toast.makeText(this, "An error occurred on login status.", Toast.LENGTH_SHORT).show();
            finish();
        }

        info1TextView.setText("Username: " + user.uname);
        info2TextView.setText("UID: " + String.valueOf(user.uid));
        info3TextView.setText("Bio: " + user.bio);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
