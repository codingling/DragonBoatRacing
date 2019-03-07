package object;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import com.example.ddd.planegame.R;
import sound.GameSoundPool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DrawBoat  {
    private int screenWidth = 0;
    private int screenHeigth = 0;
    private int score = 0;
    private Context context;
    private GameSoundPool gameSoundPool;//声音池对象

    private ArrayList<Bitmap> bitmapBoatList = new ArrayList<Bitmap>();
    private DrawObject drawObject;
    private Canvas canvas;
    private int boatDist = 0;
    private Rect boatRect = null;
    /**
     * 手指移动偏移量
     */
    private int offsetX = 0;
    private int offsetY = 0;

    /**
     * 爆炸火花合集
     */
    private List<ObjectFire> ObjectFireList = new ArrayList<>();

    /**
     * 爆炸火花图片合集
     */
    private List<Bitmap> fireBitmaps = new ArrayList<>();

    public DrawBoat(Context context) {
    //获取屏幕的宽和高
        this.context = context;
        if (screenWidth == 0 && screenHeigth == 0) {
            //用于根据给定的资源ID从指定资源中解析创建Bitmap对象
            bitmapBoatList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.boat4));
            bitmapBoatList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.boat2));
            bitmapBoatList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.boat3));
            Resources resources = context.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            screenWidth = dm.widthPixels;
            screenHeigth = dm.heightPixels;
            boatDist = screenWidth / 5;
            DrawFires(context);//初始化火花图片集合
            initGameSoundPool(context);//初始化特效声音集合
        }
    }

    public void DrawFires(Context context)
    {
         Bitmap bitmap0 = BitmapFactory.decodeResource(context.getResources(),R.drawable.fire0);
         Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.fire1);
         Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.fire2);
         Bitmap bitmap3 = BitmapFactory.decodeResource(context.getResources(),R.drawable.fire3);

         fireBitmaps.add(bitmap0);
         fireBitmaps.add(bitmap1);
         fireBitmaps.add(bitmap2);
         fireBitmaps.add(bitmap3);
     }

     public void initGameSoundPool(Context context)
     {
         gameSoundPool = new GameSoundPool(context);
     }

    public void onDraw(Canvas canvas ,DrawObject drawObject, int num) {
        this.canvas = canvas;
        this.drawObject = drawObject;
        Bitmap bitmapBoat = (Bitmap) bitmapBoatList.get(num);
        boatRect = new Rect(
                (((screenWidth - boatDist) / 2) + offsetX) > 0 ? Math.min((((screenWidth - boatDist) / 2) + offsetX), screenWidth - bitmapBoat.getWidth()) : 0
                 , (screenHeigth - boatDist * 2 + offsetY) > 0 ? Math.min((screenHeigth - boatDist * 2 + offsetY), screenHeigth - bitmapBoat.getHeight()) : 0
                 , ((screenWidth + boatDist) / 2 + offsetX) > bitmapBoat.getWidth() ? Math.min((screenWidth + boatDist) / 2 + offsetX, screenWidth) : bitmapBoat.getWidth()
                 , (screenHeigth - boatDist + offsetY) > bitmapBoat.getHeight() ? Math.min((screenHeigth - boatDist + offsetY), screenHeigth) : bitmapBoat.getHeight()
                );
        canvas.drawBitmap(bitmapBoat, null, boatRect, null);
        drawFires();//绘制爆炸火花
        isCrashing();//判断是否发生碰撞

    }

    /**
     * 返回船的矩形
     */
    public Rect getBoatRect() {
        return boatRect;
    }

    boolean canMove = false;
    int downX = 0;
    int downY = 0;

    /**
     * 用户手指移动龙舟
     */
    public void onTouch(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                downX = (int) event.getX();//得到手指所在的位置
                downY = (int) event.getY();
                //判断手指按下的点不在飞机上面  重置
                if (boatRect.contains(downX, downY)) {
                    canMove = true;
                } else {
                    canMove = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:

                int moveX = (int) event.getX();//moveX
                int moveY = (int) event.getY();

                if (canMove) {
                    offsetX = offsetX + (moveX - downX);//累积起来的偏移量
                    offsetY = offsetY + (moveY - downY);
                }
                downX = moveX;
                downY = moveY;
                break;

            case MotionEvent.ACTION_UP:
                canMove = false;
                break;
        }
    }

    /**
     * 判断龙舟是否和粽子或者石头发生碰撞
     */
    public  void isCrashing()
    {
        List<EnemyObject> enemyObjectsList = drawObject.getEnemyObjectList();
        for (EnemyObject enemyObject: enemyObjectsList) {
            if(boatRect != null && boatRect.contains(enemyObject.x + enemyObject.bitmap.getWidth() / 2, enemyObject.y + enemyObject.bitmap.getHeight() / 2 ))
            {
                enemyObject.ifDiead = true;
                if(enemyObject.kind == 1)
                {//碰撞物体为石头
                    addFireBragound(enemyObject.rect.centerX(), enemyObject.rect.centerY());
                    score -= 1;
                    //添加声音
                    gameSoundPool.playSound(2);//播放碰撞石头的声音
                }
                else
                {//碰撞物体为粽子
                    score += 2;
                    //添加声音
                    gameSoundPool.playSound(1);//播放捡到粽子的声音
                }
            }
        }
    }

    private int fireFreshCoutn = 0;
    private int fireFreshNum = fireFreshCoutn;
    /**
     * 绘制爆炸火花
     */
    private void drawFires() {
        if (ObjectFireList.size() > 0)
        {
            if(fireFreshNum == fireFreshCoutn)
            {
                fireFreshNum--;
                Iterator<ObjectFire> iterator = ObjectFireList.iterator();
                while(iterator.hasNext())
                {
                    ObjectFire fire = iterator.next();
                    for( ;fire.picFlag < fireBitmaps.size(); )
                    {
                        Bitmap bitmapFire = fireBitmaps.get(fire.picFlag);
                        Rect rect = new Rect(fire.centerX - bitmapFire.getWidth() / 2
                                , fire.centerY - bitmapFire.getHeight() / 2
                                , fire.centerX + bitmapFire.getWidth() / 2
                                , fire.centerY + bitmapFire.getHeight() / 2);

                        canvas.drawBitmap(bitmapFire, null, rect, null);
                        fire.picFlag += 1;
                    }
                    iterator.remove();//删除该火花
                }
             }
             else{//实现空一次显示一次爆炸
                fireFreshNum--;
                if(fireFreshNum < 0)
                    fireFreshNum = fireFreshCoutn;
            }
        }
    }

    private void  addFireBragound(int x, int y)
    {
        ObjectFire fire = new ObjectFire();
        fire.centerX = x;
        fire.centerY = y;
        ObjectFireList.add(fire);
    }

    /**
     * 返回分数
     * */
    public int getScore()
    {
        return score;
    }

    public void onDestroy() {
        for (Bitmap bitmapBoat: bitmapBoatList) {
            bitmapBoat.recycle();//强制对象回收自己
        }
    }
    /**
     * 返回GameSoundPool对象
     */
    public GameSoundPool getGameSoundPool()
    {
        return gameSoundPool;
    }
}
