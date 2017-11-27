package com.example.sowha.adroidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sowha.adroidlabs.android.database.sqlite.ChatDatabaseHelper;

import java.util.ArrayList;

public class ChatWindow extends Activity {
    protected static final String ACTIVITY_NAME = "ChatActivity";
    Button sendButton;
    EditText chatText;
    ListView chatBox;
    ArrayList<String> messages = new ArrayList<String>();
    ChatDatabaseHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        sendButton=(Button)findViewById(R.id.sendbutton);
        chatText=(EditText)findViewById(R.id.textbox);
        chatBox=(ListView)findViewById(R.id.chatBox);

        dbHelper = new ChatDatabaseHelper(this) ;
        db = dbHelper.getWritableDatabase();

        Cursor c = db.rawQuery("select * from " + dbHelper.TABLE_NAME,null);
        c.moveToFirst();
        while(!c.isAfterLast()) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE " + c.getString(c.getColumnIndex(dbHelper.KEY_MESSAGE)));
            messages.add(c.getString(1));
            c.moveToNext();
        }

        Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + c.getColumnCount() );

        for (int i=0; i< c.getColumnCount(); i++){
            System.out.println(c.getColumnName(i));
        }

        final ChatAdapter messageAdapter =new ChatAdapter( this );
        chatBox.setAdapter (messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues initialValues = new ContentValues();
                messages.add(chatText.getText().toString());
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView();
                initialValues.put(dbHelper.KEY_MESSAGE,chatText.getText().toString());
                db.insert(dbHelper.TABLE_NAME,null,initialValues);
                chatText.setText("");
            }
        });
    }

    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return messages.size();
        }

        public String getItem (int position) {
            return messages.get(position);
        }

        public View getView (int position, View converView, ViewGroup parent){

            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null ;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView)result.findViewById(R.id.messageText);
            message.setText( getItem(position)); // get the string at position
            return result;

        }
    }


}
