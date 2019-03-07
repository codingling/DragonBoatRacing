package view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import object.DrawBackground;
import object.DrawBoat;
import object.DrawGrass;
import object.DrawObject;
import sound.GameSoundPool;

public class BoatGamePlayingView extends SurfaceView
        implements SurfaceHolder.Callback,  View.OnTouchListener{
    /**
     * 游戏是否继续
     */
    private boolean ifStop = false;
    /**
     * 游戏是否暂停
     */
    public boolean ifPause = false;
    private SurfaceHolder holder = null;
    /**
     * 画笔
     */
    private Canvas canvas = null;
    /**
     * 绘制背景用的类
     */
    private DrawBackground drawBackground = null;
    /**
     * 绘制龙舟用的类
     */
    private DrawBoat drawBoat = null;
    /**
     * 绘制粽子或者石头用的类
     */
    private DrawObject drawObject = null;

    /**
     * 绘制草的类
     */
    private DrawGrass drawGrass = null;

    public BoatGamePlayingView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        holder = this.getHolder();
        holder.addCallback(this);
        setZOrderOnTop(true);//surfaceview必须置于窗口最顶层才可以绘图
        setZOrderMediaOverlay(true);//防止surfaceview遮挡其他控件

        drawBackground = new DrawBackground(context);
        drawObject = new DrawObject(context);
        drawBoat = new DrawBoat(context);
        drawGrass = new DrawGrass(context);

        this.setOnTouchListener(this);
    }

    public BoatGamePlayingView(Context context)
    {
        super(context);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        ifPause = false;
        ifStop = false;
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        //启动线程，开始绘制
        Thread1 thread1 = new Thread1();
        thread1.start();
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        ifPause = true;
    }
    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        ifPause = false;
        ifStop = true;

        drawBackground.onDestroy();
        drawBoat.onDestroy();
        drawObject.onDestroy();
        drawGrass.onDestroy();
    }

    int boatnum = 0;
    int grassnum = 0;
    class Thread1 extends Thread {
        //private int num = 0;
        @Override
        public void run() {
            while (!ifStop) {
                if (!ifPause) {
                    //锁定画布
                    canvas = holder.lockCanvas();
                    if (canvas != null) {
                        //绘制背景
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //设置背景的滚动速度，粽子或者石头的产生速度
                        drawBackground.bgspeed = 2 + 8 * (drawBoat.getScore()) / 100;
                        drawObject.freshCount = 100 - (drawBoat.getScore());

                        drawBackground.onDraw(canvas);

                        //解锁画布并提交改变
                        drawObject.onDraw(canvas);
                        if(drawBoat.getScore() < 50)
                        {
                            drawBoat.onDraw(canvas, drawObject,boatnum / 20);
                            boatnum ++ ;
                            if(boatnum == 60)
                                boatnum = 0;
                        }
                        else
                        {
                            if(boatnum >= 30)
                                boatnum = 0;
                            drawBoat.onDraw(canvas, drawObject,boatnum / 10);
                            boatnum ++ ;
                        }
                        drawGrass.onDraw(canvas, grassnum/10);
                        grassnum++;
                        if(grassnum == 20)
                            grassnum = 0;
                        //解锁画布并提交改变
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        drawBoat.onTouch(event);
        return true;
    }

    public int getScore()
    {
        return drawBoat.getScore();
    }

    public GameSoundPool getGameSoundPool()
    {
        return drawBoat.getGameSoundPool();
    }
}
