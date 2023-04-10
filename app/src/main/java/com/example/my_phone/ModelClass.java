package com.example.my_helper;

public class ModelClass {

    private int imageview1;
    private String textview;

    ModelClass(int imageview,String textview1)
    {
        this.imageview1 = imageview;
        this.textview = textview1;
    }

    public int getImageview() {
        return imageview1;
    }

    public String getTextview1() {
        return textview;
    }
}
