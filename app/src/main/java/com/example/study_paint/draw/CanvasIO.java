package com.example.study_paint.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CanvasIO {
    private String fileName = "draw_file.jpg";
    private static CanvasIO instance;

    public static CanvasIO getInstance(){
        if (instance == null) {
            synchronized (CanvasIO.class) {
                if (instance == null)
                    return instance = new CanvasIO();
            }
        }
        return instance;
    }
    private CanvasIO() {

    }

    public void saveBitmap(Context context, Bitmap bitmap){
        try {
            FileOutputStream out = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public Bitmap openBitmap(Context context){
        Bitmap result = null;

        try {
            FileInputStream in = context.openFileInput(fileName);
            result = BitmapFactory.decodeStream(in);
            in.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }
}
