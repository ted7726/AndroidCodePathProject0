package com.example.wilsonsu.todoapp;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wilsonsu.todoapp.utils.EditItemDialog;

import java.util.Date;

public class EditItemActivity extends AppCompatActivity implements EditItemDialog.EditNameDialogListener {
    private static final String KEY_TITLE = "editItemTitle";
    private static final String KEY_DESCRIPTION = "editItemDescription";
    private static final String KEY_PRIORITY= "editItemPriority";
    private static final String KEY_DATE= "editItemDate";
    private int selectedPriority;

    private static int[] priorityColors = new int[]{
        R.color.colorPriorityCritical,
        R.color.colorPriorityHigh,
        R.color.colorPriorityMid,
        R.color.colorPriorityLow,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // get Data from intent
        String title = getIntent().getStringExtra(KEY_TITLE);
        String description = getIntent().getStringExtra(KEY_DESCRIPTION);
        String date = getIntent().getStringExtra(KEY_DATE);
        selectedPriority = getIntent().getIntExtra(KEY_PRIORITY, 1);

        // find all the View
        EditText etTitle = (EditText) findViewById(R.id.editToDoItemTitle);
        EditText etDescription = (EditText) findViewById(R.id.editToDoItemDescription);
        TextView tvDate = (TextView) findViewById(R.id.editToDoItemDate);
        Spinner spinner = (Spinner) findViewById(R.id.editToDoItemSpinner);

        // loading Text to EditText
        etTitle.setText(title);
        etDescription.setText(description);
        tvDate.setText(date);

        // setup dropdown list

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(selectedPriority);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(getResources().getColor(priorityColors[position]));
                selectedPriority = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onSaveItem(View view) {
        // Prepare data intent
        Intent intent = new Intent();
        // Pass relevant data back as a result

        EditText etTitle = (EditText) findViewById(R.id.editToDoItemTitle);
        EditText etDescription = (EditText) findViewById(R.id.editToDoItemDescription);
        TextView tvDate = (TextView) findViewById(R.id.editToDoItemDate);

        intent.putExtra(KEY_TITLE, etTitle.getText().toString());
        intent.putExtra(KEY_DESCRIPTION, etDescription.getText().toString());
        intent.putExtra(KEY_DATE, tvDate.getText().toString());
        intent.putExtra(KEY_PRIORITY, selectedPriority);

        // Activity finished ok, return the data
        setResult(RESULT_OK, intent); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }

    public void onDatePick(View view) {
        showEditDialog();
    }

    @Override
    public void onFinishEditDialog(String date) {
        TextView tvDate = (TextView) findViewById(R.id.editToDoItemDate);
        tvDate.setText(date);
    }

    private void showEditDialog() {
        FragmentManager fm = (android.support.v4.app.FragmentManager)getSupportFragmentManager();
        TextView tvDate = (TextView) findViewById(R.id.editToDoItemDate);
        EditItemDialog editItemDialog = EditItemDialog.newInstance(tvDate.getText().toString());
        editItemDialog.show(fm, "fragment_edit_name");
    }
}
