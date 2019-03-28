package lizz.zhengzhou.screentimer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class BackGroundService extends Service {

    final String tag = "BackGroundService";
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

    public BackGroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(tag, "OnBind");
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(tag, "onCreate");
        super.onCreate();

        powerManager = (PowerManager) this.getApplicationContext().getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, tag + ":wakeLockTag");
        wakeLock.acquire();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(tag, "onStartCommand");

        new Thread() {
            @Override
            public void run() {

                while (true) {
                    try {

                        Log.i(tag, "DoSomething");

                        if (powerManager != null) {
                            boolean screen = powerManager.isScreenOn();
                            Log.i(tag, "isScreenOn：" + screen);
                        }

                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }

        super.onDestroy();
    }
}
