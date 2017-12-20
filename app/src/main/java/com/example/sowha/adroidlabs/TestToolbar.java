package com.example.sowha.adroidlabs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {

    String message = "You selected item 1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "replace with your action", Snackbar.LENGTH_LONG)
                        .setAction("action", null).show();
            }
        });
    }

    public boolean onCreateOptionsMenu (Menu m) {
        getMenuInflater().inflate(R.menu.toolbar_menu, m );
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi) {

        switch (mi.getItemId()) {
            case R.id.action_one:
                Log.d("Toolbar", "Choice 1 selected");
                Snackbar.make(findViewById(R.id.main_content),
                        message, Snackbar.LENGTH_LONG)
                        .setAction("action", null).show();
                break;
            case R.id.action_two:
                Log.d("Toolbar", "Choice 2 selected");
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("Do you want to go back?");
                builder1.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        Intent start = new Intent(TestToolbar.this, StartActivity.class);
                        startActivity(start);
                    }
                });
                builder1.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog dialog1 = builder1.create();

                dialog1.show();

                break;
            case R.id.action_three:
                Log.d("Toolbar", "Choice 3 selected");
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("Set new notification  message");
                LayoutInflater inflater = this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog, null);
                builder2.setView(dialogView);
                builder2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText text = (EditText) dialogView.findViewById(R.id.newMessageText);
                        message = text.getText().toString();
                    }
                });
                builder2.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog dialog2 = builder2.create();

                dialog2.show();
                break;

            case R.id.about:
                Context context = getApplicationContext();
                CharSequence text = "Version 1.0 by Yongdan Wang";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                break;
        }


        return true;
    }


}
