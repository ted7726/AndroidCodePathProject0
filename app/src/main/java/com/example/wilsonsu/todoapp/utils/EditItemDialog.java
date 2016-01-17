package com.example.wilsonsu.todoapp.utils;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wilsonsu.todoapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by wilsonsu on 1/12/16.
 */
public class EditItemDialog extends DialogFragment {
    private static final String KEY_TODO_DUE = "dueDate";

    public interface EditNameDialogListener {
        void onFinishEditDialog(String date);
    }
    private Button okBtn;

    public EditItemDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditItemDialog newInstance(String date) {
        EditItemDialog frag = new EditItemDialog();
        Bundle args = new Bundle();
        args.putString(KEY_TODO_DUE, date);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        return inflater.inflate(R.layout.fragment_edit_item, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
//        mEditText = (EditText) view.findViewById(R.id.txt_your_name);

//        String title = getArguments().getString("title", "Enter Name");
//        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
//        mEditText.requestFocus();
//        getDialog().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // Fetch arguments from bundle and set date
        String dateString = getArguments().getString(KEY_TODO_DUE, "2015/12/31");
        String[] splitedDate = dateString.split("/");


        int year = Integer.parseInt(splitedDate[0]);
        int month = Integer.parseInt(splitedDate[1]) - 1;
        int day  = Integer.parseInt(splitedDate[2]);
        DatePicker datePicker = (DatePicker) getView().findViewById(R.id.datePicker);
        datePicker.updateDate(year, month, day);
        okBtn = (Button) view.findViewById(R.id.ok_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);




    }

    private void dismissDialog(){
        EditNameDialogListener listener = (EditNameDialogListener) getActivity();
        DatePicker datePicker = (DatePicker) getView().findViewById(R.id.datePicker);
        String dateString = datePicker.getYear() + "/" + (datePicker.getMonth()+1) + "/" + (datePicker.getDayOfMonth());
        listener.onFinishEditDialog(dateString);
        dismiss();
    }


}
