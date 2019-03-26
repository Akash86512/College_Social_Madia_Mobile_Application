package com.example.alumniapp.Home;

import com.example.alumniapp.R;

import java.util.ArrayList;

public class HomeCollection {

    public static ArrayList<Home> getHome()
    {
        ArrayList<Home> events=new ArrayList<>();

        Home s=new Home();
s.setName("Ramakant shakya");
s.setImgname(R.drawable.android);
s.setTime("16 hour ago");
s.setPostimg(R.drawable.android);
s.setPosttext("Android");
events.add(s);


        s=new Home();
        s.setName("Ram");
        s.setImgname(R.drawable.p);
        s.setTime("16 hour ago");
        s.setPostimg(R.drawable.p);
        s.setPosttext("core java");
        events.add(s);


        s=new Home();
        s.setName("Mohan");
        s.setImgname(R.drawable.nagendra);
        s.setTime("16 hour ago");
        s.setPostimg(R.drawable.nagendra);
        s.setPosttext("jnanagni");
        events.add(s);
        return events;
    }
}
