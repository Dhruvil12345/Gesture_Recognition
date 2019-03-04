package com.example.gesture_recognition;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageClass extends Activity {

    private Camera mCamera;
    private ImageHandlerClasses imageHandlerClasses;
    private FrameLayout cameraPreview;
    private Button captureButton, closeButton;
    private ImageView showPreview;
    private Bitmap bitmap2 = null;
    private String imgData = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_preview);

        mCamera = getCameraInstance();
        imageHandlerClasses = new ImageHandlerClasses(this, mCamera);
        cameraPreview = (FrameLayout) findViewById(R.id.CameraPreview);
        showPreview = (ImageView) findViewById(R.id.show_preview);
        cameraPreview.addView(imageHandlerClasses);
        captureButton = (Button) findViewById(R.id.capture);
        closeButton = (Button) findViewById(R.id.close);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera.takePicture(null, null, pictureCallback);
            }
        });
    }

    public void onStart() {
        super.onStart();
        createDirectory("Expression");
    }

    private static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
        }
        return c;
    }

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            compressImage(bitmap);
            if (bitmap == null) {
                Toast.makeText(ImageClass.this, "Capture image is empty", Toast.LENGTH_LONG).show();
                return;
            }
            camera.startPreview();
        }
    };

    public void compressImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] byteArray = stream.toByteArray();

        imgData = convertImageToBase64(byteArray);
        MysqlDatabaseHandlerClasses mysql = new MysqlDatabaseHandlerClasses(autoFilenameGiver(),imgData,FilecreatedAt());

        //MysqlDatabaseHandlerClasses mysql = new MysqlDatabaseHandlerClasses();
        //mysql.insertData(autoFilenameGiver(),imgData,FilecreatedAt());
        //Toast.makeText(getApplicationContext(),"Request Sent",Toast.LENGTH_LONG).show();

        Bitmap compressBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        bitmap2 = Bitmap.createBitmap(compressBitmap, 0, 0, compressBitmap.getWidth(), compressBitmap.getHeight(), matrix, true);
        showPreview.setImageBitmap(bitmap2);
        saveImageToDirectory(bitmap2, autoFilenameGiver());
        showPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImageClass.this, viewImage.class);
                intent.putExtra("Image", bitmap2);
                startActivity(intent);
            }
        });
    }

    public void createDirectory(String Directoryname) {
        String myFolder = Environment.getExternalStorageDirectory() + "/" + Directoryname;
        File file = new File(myFolder);
        if (!file.exists())
            if (!file.mkdir()) {
                Toast.makeText(getApplicationContext(), "Folder Created Successfully", Toast.LENGTH_LONG).show();
            }
    }

    public String autoFilenameGiver() {
        SimpleDateFormat s = new SimpleDateFormat("dd-MM-YYYY_HH-mm-ss");
        String format = s.format(new Date());
        String concat = format + ".jpg";
        return concat;
    }

    public String FilecreatedAt(){
        SimpleDateFormat s = new SimpleDateFormat("dd-MM-YYYY_HH-mm-ss");
        String format = s.format(new Date());
        return format;
    }

    public void saveImageToDirectory(Bitmap bitmap, String filename) {

        String extr = Environment.getExternalStorageDirectory().toString() + File.separator + "Expression";
        File myPath = new File(extr, filename);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String convertImageToBase64(byte[] bytearray){
        String imgEncode = Base64.encodeToString(bytearray,Base64.DEFAULT);
        return imgEncode;
    }
}
    /*public Bitmap setResolution(Bitmap bitmap,int width,int height){
        int width1 = bitmap.getWidth();
        int height2 = bitmap.getHeight();
        float scaleWidth = ((float)width)/width1;
        float scaleHeight = ((float)height)/height2;
        Matrix matrix = new Matrix();
        matrix.postScale(width1,height2);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap,0,0,width1,height2,matrix,false);
        return resizedBitmap;
    }*/
    /*
    public void createBackButtonForSmallImageView(){

    }
    public void saveImageToMySQL(){

    }
    public void saveImageToLocalSystem(){


    }
    */
