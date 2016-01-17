package com.example.wilsonsu.todoapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wilsonsu on 1/11/16.
 */
public class TodoItemDatabase extends SQLiteOpenHelper {

    private static final String TAG = "ToDoItemDatabase";

    private static final String DATABASE_NAME = "todoItemDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_TODOS = "TODOS";
    // Todos Table Columns
    private static final String KEY_TODO_ID = "_id";
    private static final String KEY_TODO_TITLE = "title";
    private static final String KEY_TODO_DESCRIPTION = "description";
    private static final String KEY_TODO_PRIORITY = "priority";
    private static final String KEY_TODO_DUE = "date";

    private static TodoItemDatabase sInstance;

    public static synchronized TodoItemDatabase getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new TodoItemDatabase(context.getApplicationContext());
        }
        return sInstance;
    }

    public TodoItemDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // These is where we need to write create table statements.
    // This is called when database is created.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL for creating the tables

        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODOS +
                "(" +
                KEY_TODO_ID + " integer primary key," +
                KEY_TODO_TITLE + " TEXT," +
                KEY_TODO_DESCRIPTION + " TEXT," +
                KEY_TODO_PRIORITY + " INTEGER," +
                KEY_TODO_DUE + " TEXT" +
                ")";

        db.execSQL(CREATE_TODO_TABLE);
    }

    // This method is called when database is upgraded like
    // modifying the table structure,
    // adding constraints to database, etc
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        // SQL for upgrading the tables
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
            onCreate(db);
        }
    }

    public ArrayList<ToDoItem> getAllTodos() {
        ArrayList<ToDoItem> todoList = new ArrayList<>();

        // SELECT * FROM TABLE_TODOS
        String TODOS_SELECT_QUERY = "SELECT * FROM " +TABLE_TODOS;

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TODOS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    ToDoItem toDoItem = new ToDoItem();
                    toDoItem.id = cursor.getLong(cursor.getColumnIndex(KEY_TODO_ID));
                    toDoItem.title = cursor.getString(cursor.getColumnIndex(KEY_TODO_TITLE));
                    toDoItem.description = cursor.getString(cursor.getColumnIndex(KEY_TODO_DESCRIPTION));
                    toDoItem.priority = cursor.getInt(cursor.getColumnIndex(KEY_TODO_PRIORITY));
                    toDoItem.dueDate = cursor.getString(cursor.getColumnIndex(KEY_TODO_DUE));

                    todoList.add(toDoItem);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get todos from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return todoList;
    }

    // Update the Todos
    public int updateItem(ToDoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TODO_ID, item.id);
        values.put(KEY_TODO_TITLE, item.title);
        values.put(KEY_TODO_DESCRIPTION, item.description);
        values.put(KEY_TODO_PRIORITY, item.priority);
        values.put(KEY_TODO_DUE, item.dueDate);

        // Updating profile picture url for user with that userName
        return db.update(TABLE_TODOS, values, KEY_TODO_ID + " = ?",
                new String[] { String.valueOf(item.id) });
    }

    public long addItem(ToDoItem item) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long itemId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TODO_TITLE, item.title);
            values.put(KEY_TODO_DESCRIPTION, item.description);
            values.put(KEY_TODO_PRIORITY, item.priority);
            values.put(KEY_TODO_DUE, item.dueDate);
            itemId = db.insertOrThrow(TABLE_TODOS, null, values);
            item.id = itemId;
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add item");
        } finally {
            db.endTransaction();
        }
        return itemId;
    }

    public Integer deleteItem (ToDoItem item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_TODOS,
                KEY_TODO_ID +" = ? ",
                new String[] { String.valueOf(item.id) });
    }

    public void deleteAllTodos() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_TODOS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all Todos");
        } finally {
            db.endTransaction();
        }
    }
}
