package com.example.alumniapp.Home;

import android.widget.ImageView;
import android.widget.TextView;

public class Home {

    public int imgname;
    public String name;
    public String time;
    public String posttext;
    public int postimg;
    public int comment;

    public int getImgname() {
        return imgname;
    }

    public void setImgname(int imgname) {
        this.imgname = imgname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPosttext() {
        return posttext;
    }

    public void setPosttext(String posttext) {
        this.posttext = posttext;
    }

    public int getPostimg() {
        return postimg;
    }

    public void setPostimg(int postimg) {
        this.postimg = postimg;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }
}
