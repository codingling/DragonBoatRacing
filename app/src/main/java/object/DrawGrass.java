package object;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import com.example.ddd.planegame.R;

import java.util.ArrayList;

public class DrawGrass {
    private int screenWidth = 0;
    private int screenHeigth = 0;
    private Context context;
    private int offset = 0;
    private ArrayList<Bitmap> bitmapGrassList = new ArrayList<Bitmap>();
    private Canvas canvas;

    public DrawGrass(Context context) {//初始化位图集合
        //获取屏幕的宽和高
        this.context = context;
        if (screenWidth == 0 && screenHeigth == 0) {
            //用于根据给定的资源ID从指定资源中解析创建Bitmap对象
            bitmapGrassList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.bg2grass1));
            bitmapGrassList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.bg2grass2));
            Resources resources = context.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            screenWidth = dm.widthPixels;
            screenHeigth = dm.heightPixels;
        }
    }

    public void onDraw(Canvas canvas, int num) {
        this.canvas = canvas;
        Bitmap bitmapGrass = (Bitmap) bitmapGrassList.get(num);//实现草的随风飘动

        if(offset > screenHeigth)//实现草的向下移动
            offset = 0;
        Rect rect1 = new Rect(0,offset,screenWidth, offset +screenHeigth);//用来显示的矩形
        Rect rect2 = new Rect(0, -screenHeigth + offset, screenWidth, offset);
        canvas.drawBitmap(bitmapGrass, null, rect1, null);
        canvas.drawBitmap(bitmapGrass,null,rect2, null);
            offset += 2;
    }
    public void onDestroy() {
        for (Bitmap bitmapGrass: bitmapGrassList) {
            bitmapGrass.recycle();//强制对象回收自己
        }
    }
}
