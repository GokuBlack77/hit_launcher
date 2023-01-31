package com.polytron.mylauncher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.polytron.mylauncher.models.CustomViewGroup;
import com.polytron.mylauncher.screens.fragments.HomeScreenFragment;

public class MainActivity extends AppCompatActivity {
//    final View view = LayoutInflater.inflate(R.layout.fragment_home_screen, , false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        loadFragment(new HomeScreenFragment());
        NotificationManagerCompat.from(this).cancelAll();

//        if(Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
//            StringBuilder sb = new StringBuilder();
//            sb.append("package:");
//            sb.append(getPackageName());
//            startActivityForResult(new
//                    Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION",
//                    Uri.parse(sb.toString())),123);
//        }

//        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//        intent.setData(Uri.parse("package:" + getPackageName()));
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);

        try{
            disablePullNotificationTouch();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }
//
//    public void checkPermission() {
//        if (!Settings.canDrawOverlays(this)) {
//            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                    Uri.parse("package:" + getPackageName()));
//            startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
//        }
//    }

    private void disablePullNotificationTouch() {
        WindowManager manager = ((WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                // this is to enable the notification to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.height = (int) (25 * getResources()
                .getDisplayMetrics().scaledDensity);
//        localLayoutParams.format = PixelFormat.RGBX_8888;
        CustomViewGroup view = new CustomViewGroup(this);
        manager.addView(view, localLayoutParams);
    }

//    protected void onResume() {
//        super.onResume();
//        View decorView = getWindow().getDecorView();
//        // Hide the status bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        ActivityManager activityManager = (ActivityManager) getApplicationContext()
//                .getSystemService(Context.ACTIVITY_SERVICE);
//
//        activityManager.moveTaskToFront(getTaskId(), 0);
//    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        loadFragment(new HomeScreenFragment());
    }


}