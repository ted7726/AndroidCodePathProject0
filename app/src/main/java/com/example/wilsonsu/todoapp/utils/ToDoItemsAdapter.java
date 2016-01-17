package com.example.wilsonsu.todoapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wilsonsu.todoapp.R;

import java.util.ArrayList;

/**
 * Created by wilsonsu on 1/11/16.
 */
public class ToDoItemsAdapter extends ArrayAdapter<ToDoItem> {
        private static String[] priorities;
        private static int[] prioritiesColor = new int[]{
                R.color.colorPriorityCritical,
                R.color.colorPriorityHigh,
                R.color.colorPriorityMid,
                R.color.colorPriorityLow,
        };

        public ToDoItemsAdapter(Context context, ArrayList<ToDoItem> todoItems) {
            super(context, 0, todoItems);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            ToDoItem item = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_type_1, parent, false);
            }
            // Lookup view for data population
            TextView itemTitle = (TextView) convertView.findViewById(R.id.list_item_title);
            TextView itemDescription = (TextView) convertView.findViewById(R.id.list_item_description);
            TextView itemPriority = (TextView) convertView.findViewById(R.id.list_item_priority);
            TextView itemDate = (TextView) convertView.findViewById(R.id.list_item_date);
            // Populate the data into the template view using the data object
            itemTitle.setText(item.title);
            itemDescription.setText(item.description);
            itemDate.setText(item.dueDate);

            Resources res = getContext().getResources();
            if (priorities == null) {
                priorities = res.getStringArray(R.array.priority_array);
            }
            itemPriority.setText(priorities[item.priority]);
            itemPriority.setTextColor(res.getColor(prioritiesColor[item.priority]));
            // Return the completed view to render on screen
            return convertView;
        }

}
