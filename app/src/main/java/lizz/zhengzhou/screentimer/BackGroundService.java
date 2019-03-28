package lizz.zhengzhou.screentimer;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class BackGroundService extends Service {

    final String tag = "BackGroundService";
    DevicePolicyManager policyManager;
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
        policyManager = (DevicePolicyManager) this.getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
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
                            if(screen)
                            {
                                turnOffScreen();
                            }else{
                                turnOnScreen();
                            }
                        }

                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        return super.onStartCommand(intent, flags, startId);
    }


    public void turnOnScreen() {
        Log.v(tag, "ON!");

        if (powerManager != null) {
            wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, tag + ":wakeLockTag");
            wakeLock.acquire();
            wakeLock.release();
        }

    }

    public void turnOffScreen() {
        policyManager.lockNow();
    }
}
