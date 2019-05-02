package com.example.android.flashlight;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hides the top bar
        getSupportActionBar().hide();

        ToggleButton b = (ToggleButton)findViewById(R.id.tButton);

        b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//Checks if toggle button is on or off
                if (isChecked) {
//checks if device has flashlight
                    if (getApplication().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
//checks if API 23 or Higher (Android M, 6.0),
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                            String cameraId = null;
                            try {
                                cameraId = camManager.getCameraIdList()[0];
                                camManager.setTorchMode(cameraId, true);   //Turn ON
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Camera camera = Camera.open();
                            Camera.Parameters parameters = camera.getParameters();
                            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                            camera.setParameters(parameters);
                            camera.startPreview();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Flash not Availble", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        try {
                            String cameraId;
                            CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                            if (camManager != null) {
                                cameraId = camManager.getCameraIdList()[0];
                                camManager.setTorchMode(cameraId, false); //Turn OFF
                            }
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Camera camera = Camera.open();
                        Camera.Parameters parameters = camera.getParameters();
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        camera.setParameters(parameters);
                        camera.stopPreview();
                        camera.release();
                    }
                }
            }
        });

    }
}