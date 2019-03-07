package sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import com.example.ddd.planegame.R;
import object.DrawBoat;

import java.util.HashMap;

public class GameSoundPool {
     private DrawBoat drawBoat;
     private SoundPool soundPool;
     private HashMap<Integer, Integer> map;
     private Context context;
     public boolean soundeffect = true;//音效播放开关
     public GameSoundPool(Context context){
         this.context = context;
         this.drawBoat = drawBoat;
        map = new HashMap<Integer,Integer>();
        soundPool = new SoundPool(8,AudioManager.STREAM_MUSIC,0);
        map.put(1, soundPool.load(context, R.raw.get, 1));
        map.put(2, soundPool.load(context, R.raw.crash, 1));
     }

     public void playSound(int sound)
     {
         if(soundeffect)
         {
             AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
             float stramVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
             float stramMaxVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
             float volume = stramVolumeCurrent/stramMaxVolumeCurrent;
             soundPool.play(map.get(sound), volume, volume, 1, 0, 1.0f);
         }
     }
}
