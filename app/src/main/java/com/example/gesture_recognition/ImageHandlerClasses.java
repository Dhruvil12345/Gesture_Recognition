package com.example.gesture_recognition;

import android.content.Context;
import android.hardware.Camera;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class ImageHandlerClasses extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private float mDist;
    public ImageHandlerClasses(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            Camera.Parameters p = mCamera.getParameters();
            p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            mCamera.setParameters(p);
            mCamera.setPreviewDisplay(holder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
            mCamera.autoFocus(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        this.mCamera.stopPreview();
        this.mCamera.release();
    }
    public boolean onTouchEvent(MotionEvent event)
    {
        Camera.Parameters params = mCamera.getParameters();
        int action = event.getAction();
        if(event.getPointerCount() > 1)
        {
            if(action == MotionEvent.ACTION_POINTER_DOWN){
                mDist = getFingerSpacing(event);
            }else if(action == MotionEvent.ACTION_MOVE && params.isZoomSupported()){
                mCamera.cancelAutoFocus();
                handleZoom(event,params);
            }
        }
        else
        {
            if(action == MotionEvent.ACTION_UP){
                handleFocus(event,params);
            }
        }
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            mCamera.autoFocus(null);
        }
        return true;
    }
    private void handleFocus(MotionEvent event, Camera.Parameters params) {
        int pointerId = event.getPointerId(0);
        int pointerIndex = event.findPointerIndex(pointerId);
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);

        List<String> supportedFocusModes = params.getSupportedFocusModes();
        if (supportedFocusModes != null && supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean b, Camera camera) {
                    // currently set to auto-focus on single touch
                }
            });
        }
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        mCamera.stopPreview();
        Camera.Parameters p = mCamera.getParameters();
        p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        if (mHolder.getSurface() == null){
            return;
        }
        try {
            mCamera.stopPreview();
        } catch (Exception e) {

        }
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            mCamera.autoFocus(null);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleZoom(MotionEvent event, Camera.Parameters params) {
        int maxZoom = params.getMaxZoom();
        int zoom = params.getZoom();
        float newDist = getFingerSpacing(event);
        if (newDist > mDist) {
            //zoom in
            if (zoom < maxZoom)
                zoom++;
        } else if (newDist < mDist) {
            //zoom out
            if (zoom > 0)
                zoom--;
        }
        mDist = newDist;
        params.setZoom(zoom);
        mCamera.setParameters(params);
    }

    private float getFingerSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x*x + y*y);
    }
}