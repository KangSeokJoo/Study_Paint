package com.example.study_paint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.TextView;

import com.example.study_paint.draw.DrawingMode;
import com.example.study_paint.edit.EditMode;

public class MainActivity extends AppCompatActivity {

    private DrawingMode drawingMode;
    private EditMode editMode;
    private TextView tv_modeEdit, tv_modeDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        drawingMode = new DrawingMode();
        tv_modeEdit = findViewById(R.id.mode_edit);
        tv_modeDraw = findViewById(R.id.mode_draw);
        clickListener();
    }

    private void clickListener(){
        tv_modeEdit.setOnClickListener(v->{
            changeMode(tv_modeEdit, tv_modeDraw);
            getSupportFragmentManager().beginTransaction().replace(R.id.selected_fragment, editMode).addToBackStack(null).commit();
        });
        tv_modeDraw.setOnClickListener(v->{
            changeMode(tv_modeDraw, tv_modeEdit);
            getSupportFragmentManager().beginTransaction().replace(R.id.selected_fragment, drawingMode).addToBackStack(null).commit();
        });
    }

    private void changeMode(TextView selectText, TextView otherText){
        selectText.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        selectText.setTextColor(ContextCompat.getColor(this,R.color.theme_color));

        otherText.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_disable));
        otherText.setTextColor(ContextCompat.getColor(this,R.color.disable));
    }
}