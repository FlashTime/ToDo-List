package com.example.djokica.execom_hackaton;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.example.djokica.execom_hackaton.db.Task;
import com.example.djokica.execom_hackaton.db.TaskDbHelper;

import java.util.List;

/**
 * Created by Danijel Sudar on 10/26/2016.
 */

public class AdapterListView extends ArrayAdapter<Model> {

    private final List<Model> list;
    private final Activity context;
    private TaskDbHelper mHelper;

    public AdapterListView(Activity context, List<Model> list) {
        super(context, R.layout.item_list_todo, list);
        this.context = context;
        this.list = list;
        mHelper = new TaskDbHelper(context);
    }


    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.item_list_todo, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.title_task);
            viewHolder.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent pass = new Intent(context, SetItemActivity.class);
                    pass.putExtra("Title", viewHolder.text.getText());
                    pass.putExtra("isDone", viewHolder.checkbox.isChecked());
                    pass.putExtra("key", true);
                    context.startActivity(pass);
                }
            });
            viewHolder.checkbox = (CheckBox) view.findViewById(R.id.isDone_task);
            viewHolder.checkbox
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            SQLiteDatabase db = mHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();

                            if(isChecked) {
                                values.put(Task.TaskEntry.COL_TASK_ISDONE, "yes");
                            }else{
                                values.put(Task.TaskEntry.COL_TASK_ISDONE, "no");
                            }

                            final int update = db.update(Task.TaskEntry.TABLE,
                                    values,
                                    Task.TaskEntry._ID + " = ?",
                                    new String[]{String.valueOf(list.get(position).getId())});

                            db.close();

                        }
                    });
            view.setTag(viewHolder);
            viewHolder.checkbox.setTag(list.get(position));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(list.get(position).getName());
        holder.checkbox.setChecked(!list.get(position).isSelected());
        return view;
    }
}