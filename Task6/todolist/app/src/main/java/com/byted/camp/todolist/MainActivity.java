package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.byted.camp.todolist.beans.Note;
import com.byted.camp.todolist.beans.State;
import com.byted.camp.todolist.db.TodoContract;
import com.byted.camp.todolist.db.TodoDbHelper;
import com.byted.camp.todolist.debug.DebugActivity;
import com.byted.camp.todolist.ui.NoteListAdapter;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD = 1002;

    private RecyclerView recyclerView;
    private NoteListAdapter notesAdapter;
    private SQLiteDatabase db;
    private TodoDbHelper todoDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        new Intent(MainActivity.this, NoteActivity.class),
                        REQUEST_CODE_ADD);
            }
        });

        recyclerView = findViewById(R.id.list_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        notesAdapter = new NoteListAdapter(new NoteOperator() {
            @Override
            public void deleteNote(Note note) {
                MainActivity.this.deleteNote(note);
            }

            @Override
            public void updateNote(Note note) {
                MainActivity.this.updateNode(note);
            }
        });
        recyclerView.setAdapter(notesAdapter);

        notesAdapter.refresh(loadNotesFromDatabase());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_debug:
                startActivity(new Intent(this, DebugActivity.class));
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD
                && resultCode == Activity.RESULT_OK) {
            notesAdapter.refresh(loadNotesFromDatabase());
        }
    }

    private List<Note> loadNotesFromDatabase() {
        // TODO 从数据库中查询数据，并转换成 JavaBeans
        todoDbHelper=new TodoDbHelper(this);
        db=todoDbHelper.getReadableDatabase();
        List<Note> noteList=new LinkedList<>();
        String[]projection={//提取出要使用的数据的“格式”
                BaseColumns._ID,
                TodoContract.TodoEntry.COLUMN_NAME_TEXT,
                TodoContract.TodoEntry.COLUMN_NAME_STATE,
                TodoContract.TodoEntry.COLUMN_NAME_CREATED_TIME
        };

        String sortOrder= TodoContract.TodoEntry.COLUMN_NAME_CREATED_TIME+ " DESC";

        Cursor cursor=null;
        try{
            cursor=db.query(
                    TodoContract.TodoEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder
            );
            while(cursor.moveToNext()){
                int id=cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
                String Text=cursor.getString(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_NAME_TEXT));
                State state=State.from(cursor.getInt(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_NAME_STATE)));
                String time=cursor.getString(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_NAME_CREATED_TIME));

                Note note=new Note(id);
                note.setContent(Text);
                note.setDate(new Date(Long.valueOf(time)));
                System.out.println("DATEDATE33"+time);
                note.setState(state);

                noteList.add(note);
            }
        }catch (Exception e){
            Log.e("fefef:", "loadNotesFromDatabase: ", e);
        }
        db=null;
        todoDbHelper.close();
        return noteList;
    }

    private void deleteNote(Note note) {
        // TODO 删除数据
        todoDbHelper=new TodoDbHelper(this);
        db=todoDbHelper.getReadableDatabase();
        String selection= TodoContract.TodoEntry._ID+" LIKE?";
        String[] selectionArgs={Long.toString(note.id)};
        int id=db.delete(TodoContract.TodoEntry.TABLE_NAME, selection,selectionArgs);
        //return id is delete id.
        db=null;
        todoDbHelper.close();
        notesAdapter.refresh(loadNotesFromDatabase());
    }

    private void updateNode(Note note) {
        // 更新数据
        State state=note.getState();
        todoDbHelper=new TodoDbHelper(this);
        db=todoDbHelper.getWritableDatabase();
        int stat=state.intValue;
        ContentValues contentValues=new ContentValues();
        contentValues.put(TodoContract.TodoEntry.COLUMN_NAME_STATE,stat);

        String selection = TodoContract.TodoEntry._ID+" LIKE ?";
        String[] selectionArgs={Long.toString(note.id)};
        db.update(TodoContract.TodoEntry.TABLE_NAME,contentValues,selection,selectionArgs);
        db=null;
        todoDbHelper.close();
        notesAdapter.refresh(loadNotesFromDatabase());
    }

}
