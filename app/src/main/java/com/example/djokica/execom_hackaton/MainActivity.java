package com.example.djokica.execom_hackaton;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.djokica.execom_hackaton.db.Task;
import com.example.djokica.execom_hackaton.db.TaskDbHelper;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TaskDbHelper mHelper;
    private ListView mListView;
    private ArrayAdapter<Model> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHelper = new TaskDbHelper(this);
        mListView = (ListView) findViewById(R.id.list_todo);

        /*
        * funkcija za update-ovanje liste odnosno u ovom slucaju postavljanje na startne vrednosti
        * */
        update();

        
        /*
        * Dugmic za dodavanje novih stavki u listu
        * */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pass = new Intent(MainActivity.this, SetItemActivity.class);
                pass.putExtra("Title", "");
                pass.putExtra("isDone", false);
                pass.putExtra("key", false);
                startActivity(pass);
            }
        });

    }

    /*
    * funkcija sluzi za ubacivanje stvari u listu
    * */
    private void update() {
        List<Model> taskList = new ArrayList<Model>();

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(
                Task.TaskEntry.TABLE,
                new String[]{
                        Task.TaskEntry._ID,
                        Task.TaskEntry.COL_TASK_TITLE,
                        Task.TaskEntry.COL_TASK_ISDONE},
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(Task.TaskEntry.COL_TASK_TITLE);
            int idx1 = cursor.getColumnIndex(Task.TaskEntry.COL_TASK_ISDONE);
            int idx2 = cursor.getColumnIndex(Task.TaskEntry._ID);

            taskList.add(new Model(cursor.getString(idx),cursor.getString(idx1).equals("no"),cursor.getString(idx2)));
        }

        if (mAdapter == null) {
            mAdapter = new AdapterListView(this,taskList);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter = new AdapterListView(this,taskList);
            mListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
