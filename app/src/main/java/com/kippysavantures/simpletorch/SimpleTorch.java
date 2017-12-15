package com.kippysavantures.simpletorch;

import android.app.Activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;

import android.hardware.Camera;

import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;

import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.List;


public class SimpleTorch extends Activity {
    public Camera camera = Camera.open();
//    public NotificationManager notificationManager;
    private ImageView torch;
    private int buttonState=1, skinApply;
    boolean isFlashOn = false;
    FrameLayout screen;
    Camera.Parameters p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_torch);
        screen = (FrameLayout)findViewById(R.id.frameTorchLayout);
        new TorchTaskOn().execute();
        keepScreenOn();
        torch = (ImageView) findViewById(R.id.torchView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        torch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (buttonState){
                    case 1:
                        new TorchTaskOff().execute();
                        pictureOff();
                        backColorOnVersOff();
                        disableScreenOn();
                        break;
                    case 2:
                        new TorchTaskOn().execute();
                        pictureOn();
                        backColorOffVersOn();
                        keepScreenOn();
                        break;
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //setNotificationOn();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
    super.onDestroy();
        if (camera != null){
            camera.release();
            camera = null;
        }
}

    //Orange = #FF7F00
    //Bleu = #4FA1CA

    /*public void setNotificationOn() {
        Notification.Builder mBuilder =
                new Notification.Builder(this)
                .setSmallIcon(R.drawable.notiftorch)
                .setContentText("Eteindre")
                .setOngoing(true)
                .setAutoCancel(true)
                .setContentTitle(tickerText);

        Intent resultIntent = new Intent(this,SimpleTorchOff.class);

        PendingIntent pendingIntent = PendingIntent.getService(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(mId,mBuilder.build());
    }*/



    public void pictureOn() {
        torch.setImageResource(R.drawable.bouton_on);
        buttonState = 1;
        isFlashOn = true;
    }
    public void backColorOffVersOn(){
        ColorDrawable[] color = {new ColorDrawable(Color.argb(255,46,170,220)), new ColorDrawable(Color.argb(255,242,148,0))};
        TransitionDrawable trans = new TransitionDrawable(color);
        screen.setBackground(trans);
        trans.startTransition(500);
    }

    public void backColorOnVersOff(){
        ColorDrawable[] color = {new ColorDrawable(Color.argb(255,242,148,0)), new ColorDrawable(Color.argb(255,46,170,220))};
        TransitionDrawable trans = new TransitionDrawable(color);
        screen.setBackground(trans);
        trans.startTransition(500);
    }

    public void pictureOff() {
        torch.setImageResource(R.drawable.bouton_off);
        buttonState = 2;
        isFlashOn = false;
    }
    public void keepScreenOn() {
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    public void disableScreenOn(){
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    public boolean hasFlash() {
        if (camera == null) {
            return false;
        }

        Camera.Parameters parameters = camera.getParameters();

        if (parameters.getFlashMode() == null) {
            return false;
        }

        List<String> supportedFlashModes = parameters.getSupportedFlashModes();
        if (supportedFlashModes == null || supportedFlashModes.isEmpty() || supportedFlashModes.size() == 1 && supportedFlashModes.get(0).equals(Camera.Parameters.FLASH_MODE_OFF)) {
            return false;
        }

        return true;
    }


    private class TorchTaskOn extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
                p = camera.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(p);
                camera.startPreview();
                return null;
        }
    }

    private class TorchTaskOff extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground (Void... arg0) {
                p = camera.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(p);
                camera.stopPreview();
                return null;
        }
    }
}