package com.example.study_paint.draw;

import android.util.Log;

public class Pen {
    public static final int STATE_START = 0;
    public static final int STATE_MOVE = 1;

    float x, y;
    int moveStatus;
    int color;
    int size;

    public Pen(float x, float y, int moveStatus, int color, int size){
        this.x = x;
        this.y = y;
        this.moveStatus = moveStatus;
        this.color = color;
        this.size = size;

//        Log.e("팬", "x : "+x + "\ty : "+y + "\tstatus : "+moveStatus + "\tcolor : "+color + "\tsize : "+size );
    }
    public boolean isMove(){
        return moveStatus == STATE_MOVE;
    }
}
