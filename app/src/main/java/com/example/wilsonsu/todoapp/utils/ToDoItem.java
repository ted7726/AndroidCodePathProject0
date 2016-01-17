package com.example.wilsonsu.todoapp.utils;

import java.util.Comparator;

/**
 * Created by wilsonsu on 1/11/16.
 */
public class ToDoItem implements Comparable<ToDoItem> {
    Long id;
    public String title;
    public String description;
    public int priority;
    public String dueDate;
    public ToDoItem() {

    }

    public ToDoItem(String title, String description, int priority, String dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
    }

//    @Override
//    public int compare(ToDoItem lhs, ToDoItem rhs) {
//        return lhs.priority - rhs.priority;
//    }

    @Override
    public int compareTo(ToDoItem another) {
        return this.priority - another.priority;
    }
}
