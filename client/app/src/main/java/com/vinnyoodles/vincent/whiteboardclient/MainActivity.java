package com.vinnyoodles.vincent.whiteboardclient;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.widget.Toast;

import com.vinnyoodles.vincent.whiteboardclient.log.MyLog;
import com.vinnyoodles.vincent.whiteboardclient.utils.MsgEvent;

import java.net.URISyntaxException;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;

public class MainActivity extends AppCompatActivity implements FragmentCallback {
    private CanvasFragment canvasFragment;
    private RoomFragment roomFragment;
    private Socket socketInstance;
    private String userName;
    private NetworkReceiver networkReceiver;
    private boolean receiverRegistered = false;
    private AudioHelper audioHelper;
    private static Context context;
    public static Context getContext() {
        return context;
    }//定义全局的Context
    // Screen dimensions
    private double width;
    private double height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[]{android.Manifest.permission.RECORD_AUDIO};
            ActivityCompat.requestPermissions(this, permissions, 1);
        }

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = (double) size.x;
        height = (double) size.y;
        register();
        audioHelper = new AudioHelper();
        if (savedInstanceState != null) {
            userName = savedInstanceState.getString(Constants.USER_NAME_KEY);
            if (savedInstanceState.getBoolean(Constants.IS_STREAMING_KEY)) {
                audioHelper.startStream(getSocketInstance());
            }
        }
//        EventBus.getDefault().post(new MsgEvent("Write Log!!!"));
//        MyLog.v("test","Write Log ~~");
        // Show the room fragment to request a room name.
        if (userName == null)
            setupRoomFragment();
        else
            setupCanvasFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (canvasFragment != null)
            canvasFragment.saveBitmap();
        unregister();
        stopRecording();
        getSocketInstance().disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (canvasFragment != null)
            canvasFragment.saveBitmap();
        unregister();
    }

    @Override
    protected void onResume() {
        super.onResume();
        register();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (userName != null)
            outState.putString(Constants.USER_NAME_KEY, userName);

        if (audioHelper != null) {
            outState.putBoolean(Constants.IS_STREAMING_KEY, audioHelper.isStreaming());
        }
    }

    public void enterRoom(String user) {
        userName = user;
        FragmentManager manager = getFragmentManager();
        Fragment room = manager.findFragmentByTag(Constants.RETAINED_ROOM_FRAGMENT);
        if (room != null) {
            manager.beginTransaction().remove(room).commit();
        }
        setupCanvasFragment();

        getSocketInstance().emit(Constants.JOIN_ROOM_EVENT, user);
    }

    public void connected() {
        getSocketInstance().connect();
        if (canvasFragment != null)
            canvasFragment.addListeners();
    }

    public void disconnected() {
        getSocketInstance().disconnect();
        if (canvasFragment != null)
            canvasFragment.removeListeners();
    }

    public Socket getSocketInstance() {
        if (socketInstance == null) {
            try {
                socketInstance = IO.socket(Constants.SERVER_URL);
            } catch (URISyntaxException e) {
            }
        }
        return socketInstance;
    }

    public void startRecording() {
        audioHelper.startStream(getSocketInstance());
    }

    public void stopRecording() {
        audioHelper.stopStream();
    }

    private void register() {
        if (networkReceiver == null)
            networkReceiver = new NetworkReceiver(this);

        if (!receiverRegistered) {
            registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            receiverRegistered = true;
        }
    }

    private void unregister() {
        if (networkReceiver != null && receiverRegistered) {
            unregisterReceiver(networkReceiver);
            receiverRegistered = false;
        }
    }

    private void setupCanvasFragment() {
        FragmentManager fm = getFragmentManager();
        canvasFragment = (CanvasFragment) fm.findFragmentByTag(Constants.RETAINED_CANVAS_FRAGMENT);

        if (canvasFragment == null) {
            canvasFragment = new CanvasFragment();
            Bundle bundle = new Bundle();
            bundle.putDouble(Constants.WIDTH, width);
            bundle.putDouble(Constants.HEIGHT, height);
            canvasFragment.setArguments(bundle);
            fm.beginTransaction().add(R.id.frame, canvasFragment, Constants.RETAINED_CANVAS_FRAGMENT).commit();
        }
        canvasFragment.setCallback(this);
    }

    public void onFragmentLoaded() {
        if (audioHelper.isStreaming()) {
            canvasFragment.markAsRecording();
        }
    }

    private void setupRoomFragment() {
        FragmentManager fm = getFragmentManager();
        roomFragment = (RoomFragment) fm.findFragmentByTag(Constants.RETAINED_ROOM_FRAGMENT);

        if (roomFragment == null) {
            roomFragment = new RoomFragment();
            fm.beginTransaction().add(R.id.frame, roomFragment, Constants.RETAINED_ROOM_FRAGMENT).commit();
        }
        roomFragment.setCallback(this);
    }

//    //事件回调函数
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(MsgEvent event) {
//        Toast.makeText(getApplicationContext(), "onEventMainThread", Toast.LENGTH_LONG).show();//Toast打印
//        if (sb.toString().length() > 7777) {
//            sb = new StringBuffer();
//        }
//        Date date = new Date(System.currentTimeMillis());
//        sb.append(sd.format(date) + "_" + event.getMsg()).append("\r\n");
//        msg.setText(sb.toString());
//        sv.fullScroll(View.FOCUS_DOWN);
////        Toast.makeText(this,event.getMsg(),Toast.LENGTH_SHORT).show();
//    }
}
