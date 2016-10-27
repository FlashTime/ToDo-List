package com.example.djokica.execom_hackaton.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.djokica.execom_hackaton.db.Task.*;

/**
 * Created by Danijel Sudar on 10/26/2016.
 */

public class TaskDbHelper extends SQLiteOpenHelper {


    public TaskDbHelper(Context context) {
        super(context, Task.DB_NAME, null, Task.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + Task.TaskEntry.TABLE + " ( " +
                Task.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Task.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL, "+
                Task.TaskEntry.COL_TASK_DESCRIPTION + " TEXT, "+
                Task.TaskEntry.COL_TASK_ISDONE + " TEXT  "+
                ");";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Task.TaskEntry.TABLE);
        onCreate(db);
    }

}
