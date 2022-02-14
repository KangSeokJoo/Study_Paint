package com.example.study_paint.draw;

import static com.example.study_paint.draw.Pen.STATE_START;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.study_paint.R;

import java.util.ArrayList;

public class DrawingMode extends Fragment {

    private LinearLayout ll_drawContents, ll_create, ll_erase, ll_save, ll_load;

    private DrawCanvas drawCanvas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_touchmode,container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }


    private void init(View view){
        ll_drawContents = view.findViewById(R.id.draw_contents);
        ll_create = view.findViewById(R.id.create);
        ll_erase = view.findViewById(R.id.eraser);
        ll_save = view.findViewById(R.id.save);
        ll_load = view.findViewById(R.id.load);

        drawCanvas = new DrawCanvas(requireContext());

        ll_drawContents.addView(drawCanvas);
        clickListener();
    }
    private void clickListener(){
        ll_create.setOnClickListener(v -> {
            Log.e("그리기", "눌름 , Mode :"+drawCanvas.MODE_PEN);
            drawCanvas.selectTool(drawCanvas.MODE_PEN);
        });

        ll_erase.setOnClickListener(v -> {
            Log.e("지우개", "눌름 , Mode :"+drawCanvas.MODE_ERASER);
            drawCanvas.selectTool(drawCanvas.MODE_ERASER);
        });
        ll_save.setOnClickListener(v->{
            CanvasIO.getInstance().saveBitmap(requireContext(), drawCanvas.getCurrentCanvas());
            Toast.makeText(requireContext(), "성공적으로 저장했습니다.", Toast.LENGTH_SHORT).show();
            Log.e("저장", "눌름 , currentCanvas :"+drawCanvas.getCurrentCanvas());
        });
        ll_load.setOnClickListener(v->{
            drawCanvas.loadDrawImage = CanvasIO.getInstance().openBitmap(requireContext());
            Toast.makeText(requireContext(), "성공적으로 불러왔습니다.", Toast.LENGTH_SHORT).show();
            Log.e("불러오기", "눌름");
            drawCanvas.invalidate();
        });
    }
    class DrawCanvas extends View{

        public final int MODE_PEN = 1;
        public final int MODE_ERASER = 2;
        final int PEN_SIZE = 3;
        final int ERASER_SIZE = 100;

        ArrayList<Pen> drawCommandList = new ArrayList<>();       //그리기 경로가 기록된 리스트
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);           //펜
        Bitmap loadDrawImage = null;                              //호출된 이전 그림
        int color = Color.BLACK;                                  //현재 펜 색상
        int size = PEN_SIZE;                                      //현재 펜 크기

        public DrawCanvas(Context context) {
            super(context);
            Log.e("초기화 확인", "color : " + color + " size : " + size);
        }

        public DrawCanvas(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            Log.e("초기화 확인2", "color : " + color + " size : " + size);
        }

        public DrawCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            Log.e("초기화 확인3", "color : " + color + " size : " + size);
        }

        private Bitmap getCurrentCanvas(){
            Bitmap bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888); // ARGB_8888 투명처리가능 4바이트 (4444는 api 29 > deprecated), RGBA_F16 (8바이트)
            this.draw(new Canvas(bitmap));
            return bitmap;
        }

        private void selectTool(int toolMode){
            if (toolMode == MODE_PEN){
                this.color = Color.BLACK;
                size = PEN_SIZE;
            }else {
                this.color = Color.WHITE;
                size = ERASER_SIZE;
            }
            paint.setColor(color);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(Color.WHITE);

            if (loadDrawImage != null){ // 매트릭스 : 행렬 (숫자 기호 등) // LEFT,TOP 으로 바뀜
                canvas.drawBitmap(loadDrawImage,0,0,null);
            }

            for (int i=0; i < drawCommandList.size(); i++){
                Pen p = drawCommandList.get(i);
                paint.setColor(p.color);
                paint.setStrokeWidth(p.size);
                paint.setAntiAlias(true);

                if (p.isMove()){
                    Pen prevPen = drawCommandList.get(i - 1);
                    canvas.drawLine(prevPen.x,prevPen.y,p.x,p.y,paint);
                }
            }

        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int state = event.getAction() == MotionEvent.ACTION_DOWN ? STATE_START : Pen.STATE_MOVE; // 삼항연산자 터치가 되면 true false 값에 의해 0, 1

            Log.e("터치시작", "1");
//            switch (event.getAction()){
//                case MotionEvent.ACTION_DOWN:
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    break;
//                case MotionEvent.ACTION_UP:
//                    drawCommandList.add(new Pen(event.getX(), event.getY(), STATE_UP, color, size));
//                    invalidate();
//                    break;
//            }

            drawCommandList.add(new Pen(event.getX(), event.getY(), state, color, size));
            invalidate(); // 무효화 : 터치이벤트에서 화면 갱신이라고 생각하면 편함
            return true;
        }
    }
}
