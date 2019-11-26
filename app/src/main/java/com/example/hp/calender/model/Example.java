package com.example.hp.calender.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Example {

    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items)
    {
        this.items = items;
    }

}