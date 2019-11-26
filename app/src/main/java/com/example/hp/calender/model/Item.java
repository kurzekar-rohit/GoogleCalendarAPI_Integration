package com.example.hp.calender.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("start")
    @Expose
    private Start start;
    @SerializedName("end")
    @Expose
    private End end;

    @SerializedName("genre_ids")


    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSummary() {

        return summary;
    }

    public void setSummary(String summary) {

        this.summary = summary;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Start getStart() {
        return start;
    }

    public void setStart(Start start) {

        this.start = start;
    }

    public End getEnd() {
        return end;
    }

    public void setEnd(End end) {

        this.end = end;
    }


}
