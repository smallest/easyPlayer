package cn.jx.easyplayer;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import cn.jx.easyplayerlib.EasyPlayer;
import cn.jx.easyplayerlib.events.VideoEventListener;
import cn.jx.easyplayerlib.player.EasyMediaPlayer;
import cn.jx.easyplayerlib.view.EasyVideoView;

//native-lib:|EasyMediaPlayer:|easyplayer-lib:|AbstractMediaPlayer:|OpenGLRenderer:|EasyVideoView:
public class MainActivity extends AppCompatActivity{

    private static final String TAG = MainActivity.class.getSimpleName();

    private boolean mStoragePermissionReady = false;
    private boolean mIsStarted = false;
    private EasyVideoView mEasyVideoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        mEasyVideoView = (EasyVideoView) findViewById(R.id.easy_video_view);

        Button pause = (Button) findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStoragePermissionReady) {
                    if (!mIsStarted) {
                        startPlay();
                    } else {
                        Log.d(TAG, "has started already!");
                    }
                } else {
                    Log.d(TAG, "has no permission to read external storage!");
                }
            }
        });
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE"};

    public void  verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有读的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.READ_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有读的权限，去申请读的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult, requestCode="+requestCode);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            mStoragePermissionReady = ActivityCompat.checkSelfPermission(this,
                    "android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
//        togglePaused();
    }


//    public native void togglePaused();

    private void startPlay() {
        String folderurl = Environment.getExternalStorageDirectory().getPath();
        String inputurl = folderurl+"/jack.mp4";
        mEasyVideoView.setVideoPath(inputurl);
        mEasyVideoView.start();
        mIsStarted = true;
    }
}
