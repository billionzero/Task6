package com.byted.camp.todolist.db;

import android.provider.BaseColumns;

/**
 * Created on 2019/1/22.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
public final class TodoContract {

    // TODO 定义表结构和 SQL 语句常量

    private TodoContract() {
    }

    public static class TodoEntry implements BaseColumns{
        public static final String TABLE_NAME="Todo";
        public static final String COLUMN_NAME_CREATED_TIME="Created_Time";//创建时间
        public static final String COLUMN_NAME_TEXT="Text";//保存的文本
        public static final String COLUMN_NAME_STATE="STATE";//状态
    }

    private static final String SQL_CREATE_ENTRIES="CREATE TABLE "+ TodoContract.TodoEntry.TABLE_NAME
            +"("+ TodoContract.TodoEntry._ID+" INTEGER PRIMARY KEY,"
            + TodoContract.TodoEntry.COLUMN_NAME_CREATED_TIME+ " TEXT,"
            + TodoContract.TodoEntry.COLUMN_NAME_TEXT+" TEXT,"
            + TodoContract.TodoEntry.COLUMN_NAME_STATE+" TEXT)";


}
