package lizz.zhengzhou.screentimer;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btn_open, btn_close;
    DevicePolicyManager policyManager;
    ComponentName adminReceiver;
    boolean IsServiceStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_open = (Button) findViewById(R.id.btn_open);
        btn_close = (Button) findViewById(R.id.btn_close);

        final Intent intent = new Intent(this, BackGroundService.class);

        adminReceiver = new ComponentName(MainActivity.this, ScreenOffAdminReceiver.class);

        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!IsServiceStarted) {
                    boolean admin = policyManager.isAdminActive(adminReceiver);
                    if (admin) {
                        startService(intent);
                        IsServiceStarted = true;
                    } else {
                        Toast.makeText(MainActivity.this, "没有设备管理权限",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsServiceStarted) {
                    stopService(intent);
                    IsServiceStarted = false;
                }
            }
        });

        policyManager =  (DevicePolicyManager) MainActivity.this.getSystemService(Context.DEVICE_POLICY_SERVICE);

        Intent policyIntent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        policyIntent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,  adminReceiver);
        policyIntent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"开启后就可以使用锁屏功能了...");

        startActivityForResult(policyIntent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(policyManager.isAdminActive(adminReceiver)){//判断超级管理员是否激活

            Toast.makeText(MainActivity.this,"设备已被激活",
                    Toast.LENGTH_LONG).show();

        }else{

            Toast.makeText(MainActivity.this,"设备没有被激活",
                    Toast.LENGTH_LONG).show();

        }
    }
}
