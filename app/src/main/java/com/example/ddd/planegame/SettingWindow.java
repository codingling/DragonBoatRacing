package com.example.ddd.planegame;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import object.BgSwitch;
import sound.GameSoundPool;

public class SettingWindow extends PopupWindow {
    private View contentView;
    Activity context;
    BgSwitch bgSwitch;

    public SettingWindow(final Activity context, final GameSoundPool gameSoundPool, final BgSwitch bgSwitch)
    {//初始化了窗口及其动画，并设置了点击事件
        this.context = context;
        this.bgSwitch = bgSwitch;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.popup_window, null);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w / 2 + 40);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
        /**
         *
         */
        contentView.findViewById(R.id.bgsound).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //do something you need here
                Intent intent = new Intent(context,BoatGamePlayingBgM.class);
                boolean bgswitch = bgSwitch.bgswitch;
                if(bgswitch)
                {
                    context.stopService(intent);
                }
                else
                    context.startService(intent);
                bgSwitch.bgswitch = !bgswitch;
                SettingWindow.this.dismiss();
            }
        });
        /**
         * 当点击切换音效开关时对应的响应事件
         */
        contentView.findViewById(R.id.soundeffect).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // do something before signing out
                gameSoundPool.soundeffect = !(gameSoundPool.soundeffect);
                SettingWindow.this.dismiss();
            }
        });
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 5);
        } else {
            this.dismiss();
        }
    }
}

