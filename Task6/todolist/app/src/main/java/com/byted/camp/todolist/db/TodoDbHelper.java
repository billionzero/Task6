package com.byted.camp.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created on 2019/1/22.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
public class TodoDbHelper extends SQLiteOpenHelper {

    // TODO 定义数据库名、版本；创建数据库

    public TodoDbHelper(Context context) {
        super(context, "todo.db", null, 1);
    }

    private static final String SQL_CREATE_ENTRIES="CREATE TABLE "+ TodoContract.TodoEntry.TABLE_NAME
            +"("+ TodoContract.TodoEntry._ID+" INTEGER PRIMARY KEY,"+ TodoContract.TodoEntry.COLUMN_NAME_CREATED_TIME+
            " TEXT,"+ TodoContract.TodoEntry.COLUMN_NAME_TEXT+" TEXT," + TodoContract.TodoEntry.COLUMN_NAME_STATE+" TEXT)";

    private static final String SQL_DELETE_ENTRIES="DROP TABLE IF EXISTS "+ TodoContract.TodoEntry.TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i =oldVersion;i<newVersion;i++){
            //...
        }
    }

}
