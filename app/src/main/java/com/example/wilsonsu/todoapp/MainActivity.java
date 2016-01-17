package com.example.wilsonsu.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import com.example.wilsonsu.todoapp.utils.ToDoItem;
import com.example.wilsonsu.todoapp.utils.ToDoItemsAdapter;
import com.example.wilsonsu.todoapp.utils.TodoItemDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    ArrayList<ToDoItem> todoItems;
    ToDoItemsAdapter aToDoAdapter;
    ListView lvItems;
    int editPosition;
    private final int EDIT_REQUEST_CODE = 20;
    private TodoItemDatabase databaseHelper;

    private static final String KEY_TITLE = "editItemTitle";
    private static final String KEY_DESCRIPTION = "editItemDescription";
    private static final String KEY_PRIORITY= "editItemPriority";
    private static final String KEY_DATE = "editItemDate";

    private static final int CODE_ADD = 0;
    private static final int CODE_UPDATE = 1;
    private int edit_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        init();
    }

    private void init() {
        populateArrayItems();

        // setup listener
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position <= todoItems.size()) {
                    removeItem(position);
                    return true;
                }
                return false;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                editPosition = position;
                ToDoItem item = todoItems.get(position);
                intent.putExtra(KEY_TITLE, item.title);
                intent.putExtra(KEY_DESCRIPTION, item.description);
                intent.putExtra(KEY_PRIORITY, item.priority);
                intent.putExtra(KEY_DATE, item.dueDate);
                edit_type = CODE_UPDATE;
//                showEditDialog();
                startActivityForResult(intent, EDIT_REQUEST_CODE);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddItem();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    public void populateArrayItems() {
        todoItems = new ArrayList<>();
        databaseHelper = TodoItemDatabase.getInstance(this);
        readItemsFromSQL();
        aToDoAdapter = new ToDoItemsAdapter(this, todoItems);
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_sort) {
            Collections.sort(todoItems);
            aToDoAdapter.notifyDataSetChanged();

        }


        return super.onOptionsItemSelected(item);
    }

    private void readItemsFromSQL() {
        // Get all posts from database
        todoItems = databaseHelper.getAllTodos();
    }

    private void updateItem(ToDoItem item, int updatePosition) {
        ToDoItem updateItem = todoItems.get(updatePosition);
        updateItem.title = item.title;
        updateItem.description = item.description;
        updateItem.priority = item.priority;
        updateItem.dueDate = item.dueDate;
        todoItems.set(updatePosition, updateItem);
        aToDoAdapter.notifyDataSetChanged();
        databaseHelper.updateItem(updateItem);
    }

    private void addItem(ToDoItem item) {
        aToDoAdapter.add(item);
        databaseHelper.addItem(item);
    }

    private void removeItem(int position) {
        if (position < todoItems.size()) {
            ToDoItem item = todoItems.get(position);
            todoItems.remove(position);
            aToDoAdapter.notifyDataSetChanged();
            databaseHelper.deleteItem(item);

        }
    }

    //events:
    private void onAddItem() {
        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
        intent.putExtra(KEY_TITLE, "");
        intent.putExtra(KEY_DESCRIPTION, "");
        intent.putExtra(KEY_PRIORITY, 1);
        intent.putExtra(KEY_DATE, getDefaultDate());
        edit_type = CODE_ADD;
        startActivityForResult(intent, EDIT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            String editItemTitle= data.getStringExtra(KEY_TITLE);
            String editItemDescription = data.getStringExtra(KEY_DESCRIPTION);
            String editItemDate = data.getStringExtra(KEY_DATE);

            int editItemPriority = data.getIntExtra(KEY_PRIORITY, 1);
            ToDoItem item = new ToDoItem(editItemTitle, editItemDescription, editItemPriority, editItemDate);
            if (edit_type == CODE_UPDATE) {
                updateItem(item, editPosition);
            } else {
                addItem(item);
            }
        }
    }

    private String getDefaultDate() {
//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//        return sdf.format(date);
        return "2015/12/31";
    }




}
