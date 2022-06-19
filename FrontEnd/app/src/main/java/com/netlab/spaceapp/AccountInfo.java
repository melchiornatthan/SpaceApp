package com.netlab.spaceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity untuk menampilkan akun yang sudah LOGIN
 */
public class AccountInfo extends AppCompatActivity {
    TextView title;
    Button logoutButton;
    Context Content;
    private String username = MainActivity.usernamePars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        title = (TextView) findViewById(R.id.titleAcc);
        Content = this;
        logoutButton = (Button) findViewById(R.id.logoutButton);
        title.setText("Hello There\n" + username);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Content, MainActivity.class));
            }
        });
    }
}