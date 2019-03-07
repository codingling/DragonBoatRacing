package object;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import com.example.ddd.planegame.R;

public class DrawBackground{

    private Bitmap backGround = null;
    private Rect rectBp = null;
    private Rect rectBg = null;
    private Rect rectBg2 = null;
    private int screenWidth = 0;
    private int screenHeigth = 0;
    //偏移量
    private int offsetbg = 0;

    public DrawBackground(Context context) {
        //如果未获取屏幕的宽高则获取宽高
        if (screenWidth == 0 && screenHeigth == 0) {
            backGround = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg2);
            Resources resources = context.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            screenWidth = dm.widthPixels;//得到屏幕的宽度
            screenHeigth = dm.heightPixels;//得到屏幕的高度
        }

    }
    /*绘制滚动屏幕方法*/
    public int bgspeed ;//屏幕滚动的速度
    public  void onDraw(Canvas canvas) {
        //判断偏移量
        if (offsetbg >= screenHeigth) {
            offsetbg = 0;
        } else {
            offsetbg = offsetbg + bgspeed;
        }
        rectBg = new Rect(0, offsetbg, screenWidth, screenHeigth + offsetbg);
        rectBg2 = new Rect(0, -screenHeigth + offsetbg, screenWidth, offsetbg);
        canvas.drawBitmap(backGround, null, rectBg, null);
        canvas.drawBitmap(backGround, null, rectBg2, null);
    }

    public void onDestroy() {
        backGround.recycle();
    }
}

