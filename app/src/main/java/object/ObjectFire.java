package object;

import com.example.ddd.planegame.R;

public class ObjectFire {
    public int centerX,centerY;
    //已绘制到第几张图片
    public int picFlag = 0;
    /**
     * 返回火花图片的ID
     */
    public int getPicID()
    {
        if(picFlag == 0)
            return R.drawable.fire0;
        else if(picFlag == 1)
            return R.drawable.fire1;
        else if(picFlag == 2)
            return R.drawable.fire2;
        else
            return R.drawable.fire3;
    }
}
