package com.example.sowha.adroidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
    protected static final String ACTIVITY_NAME = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        Button loginButton = (Button)findViewById(R.id.button2);
        SharedPreferences prefs = getSharedPreferences("DefaultEmail",Context.MODE_PRIVATE);
        String email = prefs.getString("DefaultEmail", "email@domain.com");
        final EditText textBox = (EditText)findViewById(R.id.email);
        textBox.setText(email);

        loginButton.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                String newEmail = textBox.getText().toString();
                SharedPreferences prefs = getSharedPreferences("DefaultEmail",Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("DefaultEmail",newEmail);
                edit.commit();
                //String email = prefs.getString("DefaultEmail", "email@domain.com");
                //final EditText textBox = (EditText)findViewById(R.id.email);
                //textBox.setText(email+email);
                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);

            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }



}
