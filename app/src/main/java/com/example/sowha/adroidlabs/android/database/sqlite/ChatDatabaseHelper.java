package com.example.sowha.adroidlabs.android.database.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sowha on 2017-11-27.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    static String DATABASE_NAME="Lab5_test.db";
    static int VERSION_NUM=2;
    public final static String TABLE_NAME="tablelab5";
    public final static String KEY_ID="_id";
    public final static String KEY_MESSAGE="MESSAGE";

    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("ChatDatabaseHelper", "Calling onCreate");
        String CREATE_TABLE_MSG = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_MESSAGE + " TEXT )";
        db.execSQL(CREATE_TABLE_MSG);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer)
    {
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVer + "newVersion=" + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
