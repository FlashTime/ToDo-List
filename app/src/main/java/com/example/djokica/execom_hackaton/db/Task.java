package com.example.djokica.execom_hackaton.db;

/**
 * Created by Danijel Sudar on 10/26/2016.
 */

import android.provider.BaseColumns;

public class Task {
    public static final String DB_NAME = "dbI";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns{
        public static final String TABLE = "tasks";

        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_DESCRIPTION = "description";
        public static final String COL_TASK_ISDONE = "isDone";
    }
}
