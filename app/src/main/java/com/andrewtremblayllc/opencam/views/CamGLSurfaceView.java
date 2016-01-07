package com.andrewtremblayllc.opencam.views;

import android.opengl.GLSurfaceView;

/**
 * Created on 1/7/16.
 */
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.opengl.GLSurfaceView;

import com.andrewtremblayllc.opencam.helpers.CameraHelper;
import com.andrewtremblayllc.opencam.helpers.DebugHelper;
import com.andrewtremblayllc.opencam.renderers.CamRenderer;

public class CamGLSurfaceView extends GLSurfaceView  {
    CamRenderer mRenderer;
    Camera mCamera;

    public CamGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        mRenderer = new CamRenderer(context);
        setRenderer(mRenderer);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, final MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    queueEvent(new Runnable(){
                        public void run() {
                            mRenderer.setPosition(event.getX() / getWidth(),
                                    event.getY() / getHeight());
                        }});
                    return true;
                }
                return false;
            }
        });

    }


    @Override
    public void onPause() {
        super.onPause();
        mCamera.stopPreview();
        mCamera.release();
    }

    @Override
    public void onResume() {
        mCamera = Camera.open();
        Camera.Parameters p = mCamera.getParameters();
        // No changes to default camera parameters
        Log.d(DebugHelper.TAG, CameraHelper.cameraParamsToString(p));

        mCamera.setParameters(p);

        queueEvent(new Runnable(){
            public void run() {
                mRenderer.setCamera(mCamera);
            }});

        super.onResume();
    }


}
