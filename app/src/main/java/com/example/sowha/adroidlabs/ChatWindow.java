package com.example.sowha.adroidlabs;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sowha.adroidlabs.android.database.sqlite.ChatDatabaseHelper;

import java.util.ArrayList;

import static com.example.sowha.adroidlabs.android.database.sqlite.ChatDatabaseHelper.TABLE_NAME;

public class ChatWindow extends Activity {
    protected static final String ACTIVITY_NAME = "ChatActivity";
    Button sendButton;
    EditText chatText;
    ListView chatBox;
    ArrayList<String> messages = new ArrayList<String>();
    ChatDatabaseHelper dbHelper;
    SQLiteDatabase db;
    FrameLayout chatFrameLayout;
    boolean isLandscape=false;
    Cursor c;
    int requestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        sendButton=(Button)findViewById(R.id.sendbutton);
        chatText=(EditText)findViewById(R.id.textbox);
        chatBox=(ListView)findViewById(R.id.chatBox);

        chatFrameLayout = (FrameLayout) findViewById(R.id.chatFrameLayout);

        if(chatFrameLayout == null){
            isLandscape = false;
            Log.i(ACTIVITY_NAME, "Using Phone layout.");
        } else {
            isLandscape = true;
            Log.i(ACTIVITY_NAME, "Using Tablet layout.");

        }

        dbHelper = new ChatDatabaseHelper(this) ;
        db = dbHelper.getWritableDatabase();

        final ChatAdapter messageAdapter =new ChatAdapter( this );
        chatBox.setAdapter (messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues initialValues = new ContentValues();
                messages.add(chatText.getText().toString());
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView();
                initialValues.put(dbHelper.KEY_MESSAGE,chatText.getText().toString());
                db.insert(TABLE_NAME,null,initialValues);
                chatText.setText("");

             /*   finish();
                Intent intent = getIntent();
                startActivity(intent);*/
            }
        });

        chatBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


                String message = messageAdapter.getItem(position);
                long messageID =  messageAdapter.getItemId(position);
                Log.i(ACTIVITY_NAME, "Cursor’s  column count =" +message +  messageID);

                Bundle bundle = new Bundle();
                bundle.putLong("id",messageID);
                bundle.putString("message", message);
                bundle.putBoolean("isLandscape", isLandscape);

                if(isLandscape == true){
                    MessageFragment messageFragment = new MessageFragment();

                    messageFragment.setArguments(bundle);
                    FragmentManager fragmentManager =getFragmentManager();

                    if (fragmentManager.getBackStackEntryCount() > 0) {
                        FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
                        fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.chatFrameLayout, messageFragment).addToBackStack(null).commit();
                }
                else{
                    Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                    intent.putExtra("bundle", bundle);
                    startActivityForResult(intent,10);
                }


            }
        });

        c = db.rawQuery("select * from " + TABLE_NAME,null);
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
    }

    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && data != null) {
            Long id = data.getLongExtra("id", -1);
            db.delete(ChatDatabaseHelper.TABLE_NAME, ChatDatabaseHelper.KEY_ID + "=" + id, null);

            finish();
            Intent intent = getIntent();
            startActivity(intent);
        }
    }
    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return messages.size();
        }

        public long getItemId (int position)  {
            c.moveToPosition(position);
            return c.getLong(c.getColumnIndex(ChatDatabaseHelper.KEY_ID));
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
