package com.example.sowha.adroidlabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class ChatWindow extends Activity {

    Button sendButton;
    EditText chatText;
    ListView chatBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        sendButton=(Button)findViewById(R.id.sendbutton);
        chatText=(EditText)findViewById(R.id.textbox);
        chatBox=(ListView)findViewById(R.id.chatBox);

        final ArrayList<String> messages = new ArrayList<String>();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messages.add(chatText.getText().toString());
            }
        });

    }

    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return super.getCount();
        }

        public String getItem (int position){
            return super.getItem(position);
        }
    }


}
