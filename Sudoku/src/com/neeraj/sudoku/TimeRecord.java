package com.neeraj.sudoku;

import java.io.*;

public class TimeRecord implements Comparable<TimeRecord>,Serializable {
    private String name;
    private long time;
    TimeRecord(String nm,long tm){
        name=nm;
        time=tm;
    }
    @Override
    public int compareTo(TimeRecord tr){
        if(time>tr.getTime()){
            return 1;
        }
        else if(time==tr.getTime()){
            return 0;
        }
        else{
            return -1;
        }
    }
    public String getName(){
        return name;
    }
    public long getTime(){
        return time;
    }
}
