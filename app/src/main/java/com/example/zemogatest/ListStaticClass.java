package com.example.zemogatest;


import com.example.zemogatest.models.Post;

import java.util.ArrayList;

public class ListStaticClass {

    private static ListStaticClass sSoleInstance;
    private static ArrayList<Post> data;
    private static Boolean isUpdated;

    private ListStaticClass(){}  //private constructor.

    public static ListStaticClass getInstance(){
        if (sSoleInstance == null){ //if there is no instance available... create new one
            sSoleInstance = new ListStaticClass();
            data = new ArrayList<>();
            isUpdated = false;
        }

        return sSoleInstance;
    }

    public Boolean getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(Boolean isUpdated) {
        ListStaticClass.isUpdated = isUpdated;
    }

    public ArrayList<Post> getData() {
        return data;
    }

    public void setData(ArrayList<Post> data) {
        ListStaticClass.data = data;
    }

    public void deleteData() {
        data.clear();
    }

}
