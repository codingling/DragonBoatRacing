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
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class DrawObject{
    /**
     * 所有物体的合集
     */
    private List<EnemyObject> listPlanes = new ArrayList<EnemyObject>();
    /**
     ** 物体的种类
     */
    private List<Bitmap> listBitmap = new ArrayList<Bitmap>();
    /**
     * 物体的速度
     */
    private int ememySpeed[] = {5, 8, 11};
    private int screenWidth = 0;
    private int screenHeigth = 0;
    /**
     * 随机数
     */
    private Random random = new Random();
    /**
     * 生产物体的速度
     */
    public int freshCount = 100;
    private int freshNum = 0;
    /**
     * 最多一次性生产几个物体
     */
    private int maxMake = 4;

    private Canvas canvas;

    public DrawObject(Context context) {

        if (screenWidth == 0 && screenHeigth == 0) {
            Bitmap ememyPlane_1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.dumpling);
            Bitmap ememyPlane_2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.stone);

            listBitmap.add(ememyPlane_1);
            listBitmap.add(ememyPlane_2);

            Resources resources = context.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            screenWidth = dm.widthPixels;
            screenHeigth = dm.heightPixels;

        }

    }
   /*绘制列表中的物体*/
    public void onDraw(Canvas canvas) {
        this.canvas = canvas;
        if (listPlanes.size() > 0) {
            Iterator<EnemyObject> iteror = listPlanes.iterator();

            while (iteror.hasNext()) {
                EnemyObject plane = iteror.next();

                if (plane.y > screenHeigth || plane.ifDiead) {
                    iteror.remove();
                } else {
                    plane.y += plane.offsetY;
                    plane.rect = new Rect(plane.x, plane.y,
                            plane.x + plane.bitmap.getWidth(),
                            plane.y + plane.bitmap.getHeight());
                    canvas.drawBitmap(plane.bitmap, null, plane.rect, null);
                }
            }
        }
//每绘制完列表中所有的物体，freshNum自动减一，绘制freshCount次之后，调用函数向列表中添加物体
        if (freshNum == 0) {
            addEmemyPlane();
            freshNum = freshCount;
        }
        freshNum--;
    }

    /**
     * 生产物体
     */
    private void addEmemyPlane() {
        //随机出此次要生产物品的数量
        int makeNum = random.nextInt(maxMake) + 1;
        if(makeNum == 1)
            makeNum = 2;
        //循环生产物品
        for (int i = 0; i < makeNum; i++) {
            //随机出此物品的样式
            int choiceNum = random.nextInt(listBitmap.size());
            Bitmap bimapPlane = listBitmap.get(choiceNum);
            //创建一个物体并随机出它的创建位置
            EnemyObject plane = new EnemyObject();
            plane.y = -bimapPlane.getHeight();
            plane.x = random.nextInt(screenWidth - bimapPlane.getWidth());
            plane.offsetY = ememySpeed[choiceNum];
            plane.bitmap = bimapPlane;
            plane.kind =  choiceNum;
            listPlanes.add(plane);
        }
    }

    public List<EnemyObject> getEnemyObjectList() {
        return listPlanes;
    }
    public void onDestroy() {
        for (Bitmap bitmap : listBitmap) {
            bitmap.recycle();
        }
    }
}
