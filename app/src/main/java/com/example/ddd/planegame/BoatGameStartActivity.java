package com.example.ddd.planegame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class BoatGameStartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boat_game_start);
    }
    /*点击开发者信息按钮，对应的响应事件*/
    public void onWriter(View view)
    {
        final AlertDialog.Builder promptDialog = new AlertDialog.Builder(this);
        promptDialog.setTitle("开发者信息");
        promptDialog.setMessage("数字媒体技术151班\n" +
                "6103315025\n" +
                "邓 辉");
        promptDialog.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO:
                dialog.dismiss();
            }
        });
        promptDialog.show();
    }
   /*点击游戏规则按钮，对应的响应事件，弹出警告对话框*/
    public void onPrompt(View view)
    {
        final AlertDialog.Builder promptDialog = new AlertDialog.Builder(this);
        promptDialog.setTitle("游戏规则");
        promptDialog.setMessage("1、玩家通过左右滑动手指控制龙舟左右移动\n" +
                "2、碰到礁石-1分，捡到粽子+2分\n" +
                "3、如果分数超过100，则游戏胜利，如果低于0，则游戏失败");
        promptDialog.setPositiveButton("朕已批阅！", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO:
                dialog.dismiss();
            }
        });
        promptDialog.show();

    }
    /*点击开始游戏按钮，跳转到游戏进行界面*/
    public void enterGame(View view)
    {
        Intent intent = new Intent();
        intent.setClass(BoatGameStartActivity.this,
                BoatGamePlayingActivity.class);
        startActivity(intent);
    }

    protected void onDestroy()
    {
        super.onDestroy();
        //unregisterReceiver(exitReceiver);
    }
}
