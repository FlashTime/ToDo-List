package com.example.djokica.execom_hackaton;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Switch;

import com.example.djokica.execom_hackaton.db.Task;
import com.example.djokica.execom_hackaton.db.TaskDbHelper;

public class SetItemActivity extends AppCompatActivity {

    private TaskDbHelper mHelper;
    private Boolean isDone = false;
    /*
    * ako je true key to znaci da je usao da izmeni ako je false to znaci da dodaje novog korisnika
    * */
    private Boolean key = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_item);

        mHelper = new TaskDbHelper(this);

        Bundle info = getIntent().getExtras();
        String title = info.getString("Title");
        isDone = info.getBoolean("isDone");
        key = info.getBoolean("key");
        String id = null;

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(
                Task.TaskEntry.TABLE,
                new String[]{
                        Task.TaskEntry._ID,
                        Task.TaskEntry.COL_TASK_TITLE,
                        Task.TaskEntry.COL_TASK_ISDONE,
                        Task.TaskEntry.COL_TASK_DESCRIPTION},
                null,
                null,
                null,
                null,
                null
        );

        final EditText etTitle = (EditText) this.findViewById(R.id.title_set);
        final MultiAutoCompleteTextView etDesciption =(MultiAutoCompleteTextView) this.findViewById(R.id.description_set);

        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(Task.TaskEntry.COL_TASK_TITLE);
            if(cursor.getString(idx).equals(title)) {
                etTitle.setText(title);
                etDesciption.setText(cursor.getString(cursor.getColumnIndex(Task.TaskEntry.COL_TASK_DESCRIPTION)));
                id = cursor.getString(cursor.getColumnIndex(Task.TaskEntry._ID));
            }
        }


        Button btnSave = (Button) this.findViewById(R.id.save_button);
        final String finalId = id;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                if(key) {
                    values.put(Task.TaskEntry.COL_TASK_TITLE, String.valueOf(etTitle.getText()));
                    values.put(Task.TaskEntry.COL_TASK_DESCRIPTION, String.valueOf(etDesciption.getText()));
                    if (isDone) {
                        values.put(Task.TaskEntry.COL_TASK_ISDONE, "yes");
                    } else {
                        values.put(Task.TaskEntry.COL_TASK_ISDONE, "no");
                    }

                    final int update = db.update(Task.TaskEntry.TABLE,
                            values,
                            Task.TaskEntry._ID + " = ?",
                            new String[]{String.valueOf(finalId)});
                }else{
                    values.put(Task.TaskEntry.COL_TASK_TITLE, String.valueOf(etTitle.getText()));
                    values.put(Task.TaskEntry.COL_TASK_DESCRIPTION, String.valueOf(etDesciption.getText()));
                    values.put(Task.TaskEntry.COL_TASK_ISDONE, "no");
                    db.insertWithOnConflict(
                            Task.TaskEntry.TABLE,
                            null,
                            values,
                            SQLiteDatabase.CONFLICT_REPLACE);
                }
                db.close();

                Intent pass = new Intent(SetItemActivity.this, MainActivity.class);
                startActivity(pass);
            }
        });

        Button btnCancel = (Button) this.findViewById(R.id.cancel_button);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pass = new Intent(SetItemActivity.this, MainActivity.class);
                startActivity(pass);
            }
        });

        Button btnObrisi = (Button) this.findViewById(R.id.obrisi_button);
        btnObrisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mHelper.getWritableDatabase();
                db.delete(
                        Task.TaskEntry.TABLE,
                        Task.TaskEntry._ID + " = ?",
                        new String[]{String.valueOf(finalId)});
                db.close();
                Intent pass = new Intent(SetItemActivity.this, MainActivity.class);
                startActivity(pass);
            }
        });

        final Switch swIsDone = (Switch) this.findViewById(R.id.switch_done);
        swIsDone.setChecked(isDone);
        swIsDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDone = isChecked;
                if(!key){
                    isDone = false;
                    swIsDone.setChecked(false);
                }
            }
        });
    }


}
