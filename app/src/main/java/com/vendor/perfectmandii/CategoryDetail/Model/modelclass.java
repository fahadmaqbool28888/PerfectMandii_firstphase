package com.vendor.perfectmandii.CategoryDetail.Model;

import org.json.JSONArray;

public class modelclass
{
    public modelclass(String level1_category, JSONArray array) {
        this.level1_category = level1_category;
        this.array = array;
    }

    public String getLevel1_category() {
        return level1_category;
    }

    public void setLevel1_category(String level1_category) {
        this.level1_category = level1_category;
    }

    public JSONArray getArray() {
        return array;
    }

    public void setArray(JSONArray array) {
        this.array = array;
    }

    public String level1_category;
    public JSONArray array;

    public modelclass() {
    }




}
