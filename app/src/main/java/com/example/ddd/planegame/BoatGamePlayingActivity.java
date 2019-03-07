package com.example.ddd.planegame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import object.BgSwitch;
import sound.GameSoundPool;
import view.BoatGamePlayingView;

public class BoatGamePlayingActivity extends AppCompatActivity implements View.OnClickListener {
    private BoatGamePlayingView surfaceView;
    private TextView textScore;
    private int score = 0;
    public BgSwitch bgSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_boat_game_playing);
        surfaceView = (BoatGamePlayingView)findViewById(R.id.surfaceView);
        textScore = (TextView)findViewById(R.id.textScore);
        findViewById(R.id.rl_more).setOnClickListener((View.OnClickListener) this);

        handler.sendEmptyMessage(0);
        /*启动播放背景音乐的服务*/
        Intent intent = new Intent(BoatGamePlayingActivity.this, BoatGamePlayingBgM.class);
        startService(intent);
        bgSwitch = new BgSwitch();
    }

    /**
     * 设置的响应事件:弹出窗口
     */
    private GameSoundPool gameSoundPool;
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.rl_more){
            gameSoundPool = surfaceView.getGameSoundPool();
            SettingWindow settingWindow = new SettingWindow(this, gameSoundPool, bgSwitch);
            settingWindow.showPopupWindow(findViewById(R.id.rl_more));
        }
    }

    /**
     * 设置消息处理机制，不断获取从surfaceview中传来的分数的信息，并判断游戏是否结束
     */
    public  Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            score = surfaceView.getScore();
            textScore.setText("Score " + score);
            if(score < 0)
            {
                promptFailure();
            }
            if(score >= 100)
            {
                promptWin();
            }
            handler.sendEmptyMessage(0);
        }
    };
    /*弹出赢了的对话框，并终止游戏进行*/
    private void promptWin()
    {
        final AlertDialog.Builder promptDialog = new AlertDialog.Builder(this);
        promptDialog.setTitle("你赢了！");
        promptDialog.setMessage("您的游戏得分为：" +
                score + "\n" );
        promptDialog.setPositiveButton("我知道了！", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(BoatGamePlayingActivity.this, BoatGameStartActivity.class));
                Intent intent = new Intent(BoatGamePlayingActivity.this,BoatGamePlayingBgM.class);
                stopService(intent);
                finish();
            }
        });
        promptDialog.show();
        surfaceView.ifPause = true;
    }

    private void promptFailure()
    {
        final AlertDialog.Builder promptDialog = new AlertDialog.Builder(this);
        promptDialog.setTitle("你输了！");
        promptDialog.setMessage("您的游戏得分为：" +
                score + "\n" );
        promptDialog.setPositiveButton("我知道了！", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO:
                dialog.dismiss();
                startActivity(new Intent(BoatGamePlayingActivity.this, BoatGameStartActivity.class));
                Intent intent = new Intent(BoatGamePlayingActivity.this,BoatGamePlayingBgM.class);
                stopService(intent);
                finish();
            }
        });
        promptDialog.show();
        surfaceView.ifPause = true;//暂停游戏界面的绘制
    }
    @Override
    public void onStop()
    {
        super.onStop();
        Intent intent = new Intent(BoatGamePlayingActivity.this,BoatGamePlayingBgM.class);
        stopService(intent);
        bgSwitch.bgswitch = false;
    }

   protected void onDestroy() {
       super.onDestroy();
   }
}
