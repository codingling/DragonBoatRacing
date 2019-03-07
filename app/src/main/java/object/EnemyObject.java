package object;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class EnemyObject {
    /*石头或者粽子的的位置*/
    public int x, y;
    /*石头或粽子的移动速度*/
    public int offsetY = 0;
    /*物体的位图*/
    public Bitmap bitmap;
    /*物体的矩形*/
    public Rect rect;
    /*物体是否被船击中*/
    public boolean ifDiead = false;
    /*物体的类别*/
    public int kind = 0;
}
